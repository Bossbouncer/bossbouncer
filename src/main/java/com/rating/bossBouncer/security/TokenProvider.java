package com.rating.bossBouncer.security;

import com.rating.bossBouncer.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(appProperties.getAuth().getTokenSecret())),SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(appProperties.getAuth().getTokenSecret()))).build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(appProperties.getAuth().getTokenSecret()))).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
