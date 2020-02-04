/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.services;

import id.chataja.security.model.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author ahmad
 */
@Service
public class JwtTokenService implements TokenService {

    private final Key key;
    @Value("#{'${wrapper.token.timeout}'.replace('_','')}")
    private long timeout;
    
    @Autowired
    public JwtTokenService(Key key) {
        this.key = key;
    }

    @Override
    public String createToken(UserData data) throws InvalidKeyException {
        
        Date now = Date.from(Instant.now());
        Date expiredAt = Date.from(Instant.ofEpochMilli(now.toInstant().toEpochMilli()+ timeout));

        JwtBuilder builder = Jwts.builder();
        
        builder.setSubject(data.getEmail());
        builder.setIssuer("CAW");
        //builder.setIssuedAt(Date.from(Instant.now()));
        builder.setExpiration(expiredAt);
        builder.claim("clientAddress", data.getClientAddress());
        //builder.claim("fullname", data.getFullname());
        //builder.claim("mobileNo", data.getMobileNumber());
        
        String token = null;
        try {
            builder.signWith(this.key);
            token = builder.compact();
        } catch (InvalidKeyException ex) {
            throw ex;
        }

        return token;
        
    }
    
    @Override
    public UserData readTokenData(String token) throws ExpiredJwtException, MalformedJwtException, UnsupportedJwtException, SignatureException, IllegalArgumentException {
        
        UserData data = null;
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(this.key).parseClaimsJws(token);
            
            data = new UserData();
            data.setEmail(claims.getBody().getSubject());
            data.setClientAddress(claims.getBody().get("clientAddress").toString());
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException ex) {
            throw ex;
        }
        
        return data;
        
    }
        
}
