package io.dmly.invoicer;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.dmly.invoicer.controller.AbstractController;
import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.exception.handler.AuthorizationExceptionHandler;
import io.dmly.invoicer.utils.HttpResponseProvider;
import io.dmly.invoicer.utils.HttpResponseWriter;
import io.dmly.invoicer.exception.handler.impl.AuthorizationExceptionHandlerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class InvoicerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicerApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthorizationExceptionHandler authorizationExceptionHandler(HttpResponseProvider httpResponseProvider,
																	   HttpResponseWriter httpResponseWriter) {

		List<Class<?>> knownExceptions = List.of(
				ApiException.class,
				DisabledException.class,
				LockedException.class,
				BadCredentialsException.class,
				InvalidClaimException.class,
				TokenExpiredException.class
		);

		return new AuthorizationExceptionHandlerImpl(httpResponseProvider, httpResponseWriter, knownExceptions);
	}

}
