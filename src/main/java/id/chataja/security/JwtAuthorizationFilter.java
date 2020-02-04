/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.chataja.security.model.TokenData;
import id.chataja.security.services.TokenService;
import id.chataja.util.rest.ErrorEnvelope;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author ahmad
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    
    Logger myLogger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private TokenService tokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) throws ExpiredJwtException, MalformedJwtException, UnsupportedJwtException, SignatureException, IllegalArgumentException {
        
        String token = req.getHeader(TokenService.AUTHORIZATION_HEADER);
        myLogger.info("token = " + token);
        
        if (token != null) {
            try {
                
                TokenData data = tokenService.readTokenData(token.replace(TokenService.TOKEN_PREFIX, ""));
                myLogger.info("tokenData = " + data.toString());
                return new UsernamePasswordAuthenticationToken(data.getEmail(), null, new ArrayList<>());
                
            } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException ex) {
                throw ex;
            }            
        }
        
        return null;
        
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(TokenService.AUTHORIZATION_HEADER);

        logger.info("uri = " + req.getRequestURI());
        
        if (header == null || !header.startsWith(TokenService.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        
        UsernamePasswordAuthenticationToken authentication = null;
        Map<String,String> msgs = null;
        try {
            authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException ex) {
            msgs = new HashMap();
            
            msgs.put("exception", ex.getClass().getCanonicalName());
            msgs.put("message", ex.getMessage());
        }

        if (authentication == null) {
            
            ErrorEnvelope env = new ErrorEnvelope(HttpStatus.BAD_REQUEST.value());
            env.setError(msgs);
            
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.setContentType("application/json");

            ObjectMapper mapper = new ObjectMapper();
            
            PrintWriter out = res.getWriter(); 
            out.print(mapper.writeValueAsString(env));
            out.flush();

            return; 
        }
        
        chain.doFilter(req, res);

    }

}
