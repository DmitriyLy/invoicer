package io.dmly.invoicer.controller;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.entitymapper.UserDtoMapper;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.model.form.LoginForm;
import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDtoMapper userDtoMapper;

    @PostMapping(path = "/login")
    public ResponseEntity<HttpResponse> login(@RequestBody LoginForm loginForm) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        UserDto userDto = userDtoMapper.fromUser(userService.getUserByEmail(loginForm.getEmail()));
        return ResponseEntity.ok(HttpResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .data(Map.of("user", userDto))
                .message("Login success")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping(path = "/register")
    public ResponseEntity<HttpResponse> register(@RequestBody @Valid User user) {
        UserDto userDto = userDtoMapper.fromUser(userService.createUser(user));
        return ResponseEntity.created(getUrI(userDto)).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userDto))
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    private URI getUrI(UserDto userDto) {
        try {
            return new URI(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/user/get/" + userDto.id())
                    .toUriString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}
