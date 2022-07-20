package ru.rakhcheev.tasket.api.tasketapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.rakhcheev.tasket.api.tasketapi.dto.user.UserLoginDTO;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.setFilterProcessesUrl("/login");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            UserLoginDTO userData = new ObjectMapper().readValue(request.getInputStream(), UserLoginDTO.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userData.getLogin(), userData.getPassword(), new ArrayList<>()
            ));
        } catch (IOException e) {
            throw new NullPointerException("Недостаточно данных в запросе");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        ArrayList<String> authorities = new ArrayList<>();
        authResult.getAuthorities().forEach(x -> authorities.add(x.getAuthority()));
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withClaim("Role", authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTConfiguration.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JWTConfiguration.SECRET.getBytes()));
        response.addHeader(JWTConfiguration.HEADER_STRING, JWTConfiguration.TOKEN_PREFIX + token);

    }
}
