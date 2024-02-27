package io.dmly.invoicer.controller;

import io.dmly.invoicer.entitymapper.UserDtoMapper;
import io.dmly.invoicer.jwt.provider.TokenProvider;
import io.dmly.invoicer.model.InvoicerUserDetails;
import io.dmly.invoicer.model.Role;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.form.*;
import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.service.RoleService;
import io.dmly.invoicer.service.UserService;
import io.dmly.invoicer.utils.HttpResponseProvider;
import io.dmly.invoicer.utils.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static io.dmly.invoicer.security.constant.SecurityConstants.ACCESS_TOKEN;
import static io.dmly.invoicer.security.constant.SecurityConstants.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDtoMapper userDtoMapper;
    private final TokenProvider tokenProvider;
    private final HttpResponseProvider httpResponseProvider;
    private final TokenExtractor tokenExtractor;
    private final RoleService roleService;

    @PostMapping(path = "/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        User user = getUserFromAuthentication(authentication);

        HttpResponse userResponse;

        if (user.isUsingMfa()) {
            userService.sendVerificationCode(user);
            userResponse = getResponse(user, "Verification code has been sent", HttpStatus.OK);
        } else {
            userResponse = getLoginSuccessUserResponse(user);
        }

        return ResponseEntity
                .ok(userResponse);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<HttpResponse> register(@RequestBody @Valid User user) {
        User storedUser = userService.createUser(user);
        return ResponseEntity
                .created(getUrI(storedUser.getId()))
                .body(getResponse(storedUser, "User created", HttpStatus.CREATED));
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return ResponseEntity
                .ok(getResponse(
                        user,
                        roleService.getRoles(),
                        "User profile provided",
                        HttpStatus.OK));
    }

    @PatchMapping("/update")
    public ResponseEntity<HttpResponse> update(@RequestBody @Valid UpdateUserDetailsForm userDetailsForm) {
        User user = userService.updateUserRetails(userDetailsForm);
        return ResponseEntity
                .ok(getResponse(user, "User profile updated", HttpStatus.OK));
    }

    //Used for 2FA
    @GetMapping(path = "/verify/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyByCode(@PathVariable(name = "email") String email,
                                                     @PathVariable(name = "code") String code) {
        User user = userService.getUserByEmailAndValidCode(email, code);
        return ResponseEntity
                .ok(getLoginSuccessUserResponse(user));
    }

    @GetMapping(path = "/reset-password/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok(getResponse("Reset password link sent to your email", HttpStatus.OK));
    }

    @GetMapping(path = "/verify-password-reset/{key}")
    public ResponseEntity<HttpResponse> verifyPasswordReset(@PathVariable String key) {
        User user = userService.verifyPasswordReset(key);
        return ResponseEntity.ok(getResponse(user, "Please specify your new password", HttpStatus.OK));
    }

    @PostMapping(path = "/change-password/{key}")
    public ResponseEntity<HttpResponse> changePassword(@PathVariable String key, @RequestBody ChangePasswordForm changePasswordData) {
        userService.changePassword(key, changePasswordData);
        return ResponseEntity.ok(getResponse("Your password successfully changed. Please login", HttpStatus.OK));
    }

    @PostMapping(path = "/activate/account/{email}")
    public ResponseEntity<HttpResponse> activateAccount(@PathVariable String email) {
        userService.activateAccount(email);
        return ResponseEntity.ok(getResponse("Your account successfully activated. Please login", HttpStatus.OK));
    }

    @PostMapping(path = "/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        Optional<String> refreshToken = tokenExtractor.extractToken(request);

        if (refreshToken.isEmpty()) {
            return new ResponseEntity<>(getResponse("Cannot get refresh token from request", HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        }

        String email = tokenProvider.getSubject(refreshToken.get(), request);

        if (!tokenProvider.isTokenValid(email, refreshToken.get())) {
            return new ResponseEntity<>(getResponse("Refresh token is invalid", HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserByEmail(email);

        tokenProvider.getAccessToken(user);

        return new ResponseEntity<>(
                getResponse(
                        user,
                        Collections.emptyList(),
                        getTokensMap(tokenProvider.getAccessToken(user), refreshToken.get()),
                        "Access token updated.",
                        HttpStatus.OK
                ),
                HttpStatus.OK
        );
    }

    @PatchMapping(path = "/update/password")
    public ResponseEntity<HttpResponse> updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordForm form) {
        userService.updatePassword(getUserFromAuthentication(authentication).getId(), form);
        return ResponseEntity.ok(getResponse("Your password successfully updated.", HttpStatus.OK));
    }

    @PatchMapping(path = "/update/role/{roleName}")
    public ResponseEntity<HttpResponse> updateRole(Authentication authentication, @PathVariable String roleName) {
        Long id = getUserFromAuthentication(authentication).getId();
        roleService.updateUserRole(id, roleName);
        return ResponseEntity.ok(
                getResponse(
                        userService.get(id),
                        roleService.getRoles(),
                        "Your roles successfully updated.",
                        HttpStatus.OK
                )
        );
    }

    @PatchMapping(path = "/update/account/settings")
    public ResponseEntity<HttpResponse> updateAccountSettings(Authentication authentication, @RequestBody UpdateAccountSettingsForm form) {
        Long id = getUserFromAuthentication(authentication).getId();
        userService.updateAccountSettings(id, form);
        return ResponseEntity.ok(
                getResponse(
                        userService.get(id),
                        roleService.getRoles(),
                        "Account settings successfully updated.",
                        HttpStatus.OK
                )
        );
    }

    @PatchMapping(path = "/toggle/mfa")
    public ResponseEntity<HttpResponse> toggleMfa(Authentication authentication) {
        Long id = getUserFromAuthentication(authentication).getId();
        userService.toggleMfa(id);
        return ResponseEntity.ok(
                getResponse(
                        userService.get(id),
                        roleService.getRoles(),
                        "MFA settings successfully updated.",
                        HttpStatus.OK
                )
        );
    }

    private URI getUrI(Long userId) {
        try {
            return new URI(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/user/get/" + userId)
                    .toUriString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getTokensMap(User user) {
        return getTokensMap(tokenProvider.getAccessToken(user), tokenProvider.getRefreshToken(user));
    }

    private Map<String, Object> getTokensMap(String accessToken, String refreshToke) {
        return Map.of(
                ACCESS_TOKEN, accessToken,
                REFRESH_TOKEN, refreshToke
        );
    }

    private HttpResponse getLoginSuccessUserResponse(User user) {
        return getResponse(user, null, getTokensMap(user), "Login success", HttpStatus.OK);
    }

    private HttpResponse getResponse(User user, String message, HttpStatus status) {
        return getResponse(user, null, Collections.emptyMap(), message, status);
    }

    private HttpResponse getResponse(String message, HttpStatus status) {
        return getResponse(null, null, Collections.emptyMap(), message, status);
    }

    private HttpResponse getResponse(User user, Collection<Role> roles, String message, HttpStatus status) {
        return getResponse(user, roles, Collections.emptyMap(), message, status);
    }

    private HttpResponse getResponse(User user, Collection<Role> roles, Map<String, Object> data, String message, HttpStatus status) {
        Map<String, Object> responseData = new HashMap<>();

        Optional.ofNullable(user).ifPresent(value -> responseData.put("user", userDtoMapper.fromUser(value)));
        if (CollectionUtils.isNotEmpty(roles)) {
            responseData.put("roles", roles);
        }

        if (MapUtils.isNotEmpty(data)) {
            responseData.putAll(data);
        }

        return httpResponseProvider.getHttpResponse(responseData, message, StringUtils.EMPTY, status);
    }

    private User getUserFromAuthentication(Authentication authentication) {
        return ((InvoicerUserDetails) authentication.getPrincipal()).getUser();
    }

}
