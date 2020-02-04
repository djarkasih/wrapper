/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.services;

import id.chataja.security.model.UserData;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;

/**
 *
 * @author ahmad
 */
public interface TokenService {
    
    public final static String TOKEN_PREFIX = "Bearer ";
    public final static String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_SERVICE_URL = "/token";
    
    public String createToken(UserData data) throws InvalidKeyException;
    public UserData readTokenData(String token) throws ExpiredJwtException, MalformedJwtException, UnsupportedJwtException, SignatureException, IllegalArgumentException;
    
}
