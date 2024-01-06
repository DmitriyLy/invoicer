package io.dmly.invoicer.controller;

import io.dmly.invoicer.entitymapper.UserDtoMapper;
import io.dmly.invoicer.jwt.provider.TokenProvider;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.form.LoginForm;
import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDtoMapper userDtoMapper;
    private final TokenProvider tokenProvider;

    @PostMapping(path = "/login")
    public ResponseEntity<HttpResponse> login(@RequestBody LoginForm loginForm) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        User user = userService.getUserByEmail(loginForm.getEmail());
        HttpResponse userResponse;

        if (user.isUsingMfa()) {
            userService.sendVerificationCode(user);
            userResponse = getUserResponse(user, "Verification code has been sent", HttpStatus.OK);
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
                .body(getUserResponse(storedUser,"User created", HttpStatus.CREATED));
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        User user = userService.getUserByEmail((String) authentication.getPrincipal());
        return ResponseEntity
                .ok(getUserResponse(user, "User profile provided", HttpStatus.OK));
    }

    @GetMapping(path = "/verify/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyByCode(@PathVariable(name = "email") String email,
                                                     @PathVariable(name = "code") String code) {
        User user = userService.getUserByEmailAndValidCode(email, code);
        return ResponseEntity
                .ok(getLoginSuccessUserResponse(user));
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
        return Map.of(
                "access_token", tokenProvider.getAccessToken(user),
                "refresh_token", tokenProvider.getRefreshToken(user)
        );
    }

    private HttpResponse getLoginSuccessUserResponse(User user) {
        return getUserResponse(user, getTokensMap(user), "Login success", HttpStatus.OK);
    }

    private HttpResponse getUserResponse(User user, String message, HttpStatus status) {
        return getUserResponse(user, Collections.emptyMap(), message, status);
    }

    private HttpResponse getUserResponse(User user, Map<String, Object> data, String message, HttpStatus status) {
        Optional<User> optionalUser = Optional.ofNullable(user);

        HttpResponse response = HttpResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .message(message)
                .status(status)
                .statusCode(status.value())
                .build();

        if (optionalUser.isPresent() || MapUtils.isNotEmpty(data)) {
            Map<String, Object> responseData = new HashMap<>(data);
            optionalUser.ifPresent(value -> responseData.put("user", userDtoMapper.fromUser(value)));
            response.setData(responseData);
        }

        return response;
    }

}
