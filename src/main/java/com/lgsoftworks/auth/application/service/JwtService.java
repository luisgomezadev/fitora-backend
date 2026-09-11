package com.lgsoftworks.auth.application.service;

import com.lgsoftworks.auth.infrastructure.config.JwtProperties;
import com.lgsoftworks.auth.domain.model.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(AuthenticatedUser user) {

        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put(
                "userId",
                user.getUserId().toString()
        );

        extraClaims.put(
                "gymId",
                user.getGymId().toString()
        );

        extraClaims.put(
                "role",
                user.getRole()
        );

        return generateToken(
                extraClaims,
                user.getUsername()
        );
    }

    public String generateToken(UserDetails userDetails) {

        if (!(userDetails instanceof AuthenticatedUser user)) {
            throw new IllegalArgumentException(
                    "Invalid authenticated user"
            );
        }

        return generateToken(user);
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            String username
    ) {

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtProperties.getExpiration()
                        )
                )
                .signWith(
                        getSignInKey(),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {

        try {

            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {

            return e.getClaims();

        } catch (
                UnsupportedJwtException
                | MalformedJwtException
                | SignatureException
                | IllegalArgumentException e
        ) {

            throw new RuntimeException(
                    "JWT token inválido o mal formado",
                    e
            );
        }
    }

    private Key getSignInKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(
                        jwtProperties.getSecretKey()
                );

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean canTokenBeRenewed(String token) {

        try {

            Claims claims = extractAllClaims(token);

            Date expiration = claims.getExpiration();

            long currentTime = System.currentTimeMillis();

            return !expiration.before(new Date(currentTime))
                    || expiration.getTime()
                    + jwtProperties.getRefreshWindow() <= currentTime;

        } catch (Exception e) {

            return true;
        }
    }

    public String renewToken(
            String token,
            UserDetails userDetails
    ) {

        if (canTokenBeRenewed(token)) {

            throw new IllegalArgumentException(
                    "The JWT couldn't be renewed"
            );
        }

        return generateToken(userDetails);
    }
}