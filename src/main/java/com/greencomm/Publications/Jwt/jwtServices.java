package com.greencomm.Publications.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

@Service
public class jwtServices implements Serializable {

    @Value("${jwt.secret}")
    private  String secretKey;

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Boolean validateToken(String token) {
        System.out.println("Validar token correcto");
        return (!isTokenExpired(token) );
    }

    private Boolean isTokenExpired(String token) {
        System.out.println("Comienzo del metodo isTokenExpiredCorrecto");
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        System.out.println("Comienzo del metodo getExpiration correcto");
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        System.out.println("Comienzo del metodo GetClaims");
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        System.out.println("Comienzo del metodo GetAllClaims");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

    }
}


