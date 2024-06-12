package com.greencomm.Publications.Config;


import com.greencomm.Publications.Jwt.jwtServices;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private jwtServices jwtServices; // Instancia de JwtServices

    // Constructor para inyección de dependencias
    @Autowired
    public JwtAuthenticationFilter(jwtServices jwtServices) {
        this.jwtServices = jwtServices;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);

            if (!"null".equals(jwtToken) && !jwtToken.trim().isEmpty()) {
                System.out.println(jwtToken);

                try {
                    if (jwtServices.validateToken(jwtToken)) {
                        System.out.println("Hasta aca todo bien 1");
                        username = jwtServices.getUserNameFromToken(jwtToken);
                        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    else {
                        System.out.println("Sesion cerrada");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión cerrada o token inválido");
                        return; // Stop further processing
                    }
                } catch (IllegalArgumentException e){
                    logger.error("No se puede obtener el token JWT", e);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token JWT incorrecto");
                    return; // Stop further processing
                } catch (ExpiredJwtException e){
                    logger.error("El token JWT ha caducado", e);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "El token JWT ha caducado");
                    return; // Stop further processing
                }
            } else {
                System.out.println("Token is either 'null' or empty.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No se ha proporcionado token o el token está vacío");
                return; // Stop further processing
            }
        } else {
            logger.warn("El token JWT no comienza con Bearer String");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Formato de token incorrecto");
            return; // Stop further processing
        }

        filterChain.doFilter(request, response);
    }
}
