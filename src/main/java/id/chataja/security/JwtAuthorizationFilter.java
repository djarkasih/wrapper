/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.chataja.security.model.TokenData;
import id.chataja.security.services.TokenService;
import id.chataja.util.NetworkHelper;
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
    
//    Logger myLogger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private ObjectMapper mapper;
    private TokenService tokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, ObjectMapper mapper, TokenService tokenService) {
        super(authenticationManager);
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) throws ExpiredJwtException, MalformedJwtException, UnsupportedJwtException, SignatureException, IllegalArgumentException, DifferentClientException {
        
        String token = req.getHeader(TokenService.AUTHORIZATION_HEADER);
//        myLogger.info("token = " + token);
        
        TokenData data = null;
        boolean isValid = false;
        
        if (token != null) {
            try {
                String address = NetworkHelper.getClientAddress(req);
                data = tokenService.readTokenData(token.replace(TokenService.TOKEN_PREFIX, ""));
//                myLogger.info("tokenData = " + data.toString());
                isValid = data.getClientAddress().equalsIgnoreCase(address);
                if (!isValid) {
                    throw new DifferentClientException("Expecting " + data.getClientAddress() + " instead of " + address);
                }
                
            } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException ex) {
                throw ex;
            }            
        }
        
        if (isValid && (data != null)) {
            return new UsernamePasswordAuthenticationToken(data.getEmail(), null, new ArrayList<>());
        }
        
        return null;
        
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(TokenService.AUTHORIZATION_HEADER);

//        logger.info("uri = " + req.getRequestURI());
        
        if (header == null || !header.startsWith(TokenService.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        
        UsernamePasswordAuthenticationToken authentication = null;
        Map<String,String> msgs = null;
        try {
            authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException | DifferentClientException ex) {
            msgs = new HashMap();
            
            msgs.put("exception", ex.getClass().getCanonicalName());
            msgs.put("message", ex.getMessage());
        }

        if (authentication == null) {
            
            ErrorEnvelope env = new ErrorEnvelope(HttpStatus.BAD_REQUEST.value());
            env.setError(msgs);
            
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.setContentType("application/json");
            
            PrintWriter out = res.getWriter(); 
            out.print(this.mapper.writeValueAsString(env));
            out.flush();

            return; 
        }
        
        chain.doFilter(req, res);

    }

}
