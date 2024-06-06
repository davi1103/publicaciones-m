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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
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

        final String token = request.getHeader("Authorization");
        System.out.println(token);
        String username = null;
        String jwtToken = null;

        if (token != null && token.startsWith("Bearer ")){
            jwtToken = token.substring(7);
            System.out.println(jwtToken);

            try {
                if (jwtServices.validateToken(jwtToken)) { // Asume que este método verifica la validez del token.
                    username = jwtServices.getUserNameFromToken(jwtToken); // Extrae el nombre de usuario del token.
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // Define roles según tus necesidades

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
                else {
                    logger.warn("Sesion cerrada");
                }
            }   catch (IllegalArgumentException e){
                logger.error("No se puede obtener el token JWT");
            }catch (ExpiredJwtException e){
                logger.error("El token JWT ha caducado");
                System.out.println("El token JWT ha caducado");
            }
        } else {
            logger.warn("El token JWT no comienza con Bearer String");
        }

        filterChain.doFilter(request, response);
        System.out.println("Completo");
    }
}
