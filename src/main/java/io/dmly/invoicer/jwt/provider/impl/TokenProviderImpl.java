package io.dmly.invoicer.jwt.provider.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.jwt.provider.TokenProvider;
import io.dmly.invoicer.model.InvoicerUserDetails;
import io.dmly.invoicer.model.Role;
import io.dmly.invoicer.model.User;
import io.dmly.invoicer.service.RoleService;
import io.dmly.invoicer.service.UserService;
import io.dmly.invoicer.utils.PermissionUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {
    public static final String AUTHORITIES = "authorities";

    private final RoleService roleService;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret:YUnhnded898b8HUY76g78}")
    private String secret;

    @Value("${jwt.access.token.expiration.period:25920000}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.token.expiration.period:51840000}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.issuer:Invoicer app}")
    private String issuer;

    @Value("${jwt.audience:}")
    private String audience;

    @Override
    public String getAccessToken(User user) {
        return JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withIssuedAt(new Date())
                .withSubject(user.getEmail())
                .withArrayClaim(AUTHORITIES, getClaimsFromUser(user))
                .withExpiresAt(DateUtils.addSeconds(new Date(), accessTokenExpirationPeriod.intValue()))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    @Override
    public String getRefreshToken(User user) {
        return JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withIssuedAt(new Date())
                .withSubject(user.getEmail())
                .withExpiresAt(DateUtils.addSeconds(new Date(), accessTokenExpirationPeriod.intValue()))
                .sign(getAlgorithm());
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public Authentication getAuthentication(String email, List<GrantedAuthority> authorities, HttpServletRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(email), null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    @Override
    public boolean isTokenValid(String email, String token) {
        return StringUtils.isNoneEmpty(email) && !isTokenExpired(token);
    }

    @Override
    public String getSubject(String token, HttpServletRequest request) {
        try {
            return getJwtVerifier().verify(token).getSubject();
        } catch (TokenExpiredException e) {
            request.setAttribute("expiredMessage", e.getMessage());
        } catch (InvalidClaimException e) {
            request.setAttribute("invalidClaim", e.getMessage());
        } catch (Exception e) {
            log.error("Cannot get subject", e);
            throw new ApiException("Cannot get subject", e);
        }

        return null;
    }

    private boolean isTokenExpired(String token) {
        return getJwtVerifier()
                .verify(token)
                .getExpiresAt()
                .before(new Date());
    }

    private String[] getClaimsFromUser(User user) {
        return PermissionUtils.getAuthorities(roleService.getRoleByUserId(user.getId()).getPermission())
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }

    private String[] getClaimsFromToken(String token) {
        return getJwtVerifier()
                .verify(token)
                .getClaim(AUTHORITIES)
                .asArray(String.class);
    }

    private JWTVerifier getJwtVerifier() {
        try {
            return JWT.require(getAlgorithm()).withIssuer(issuer).build();
        } catch (JWTVerificationException e) {
            log.error("Token cannot be verified", e);
            throw new ApiException("Token cannot be verified", e);
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret.getBytes());
    }
}
