package ru.rakhcheev.tasket.api.tasketapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JWTConfiguration.HEADER_STRING);

        if (header == null || !header.startsWith(JWTConfiguration.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request, response);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(JWTConfiguration.HEADER_STRING);
        if (token == null) return null;

        try {
            DecodedJWT decodedJWT =  JWT.require(Algorithm.HMAC512(JWTConfiguration.SECRET.getBytes())).build()
                    .verify(token.replace(JWTConfiguration.TOKEN_PREFIX, ""));
            String user = decodedJWT.getSubject();
            List<SimpleGrantedAuthority> list = decodedJWT.getClaim("Role").asList(SimpleGrantedAuthority.class);
            return new UsernamePasswordAuthenticationToken(user, null, list);
        } catch (Exception e) {
            return null;
        }
    }
}
