/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.controller;

import id.chataja.security.model.TokenData;
import id.chataja.security.services.TokenService;
import id.chataja.util.rest.SimpleEnvelope;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ahmad
 */
@RestController
public class CredentialEndpoint {
    
    @Autowired
    private TokenService tokenService;
    
    @GetMapping(value="/infoq")
    public ResponseEntity<Map<String,Object>> showInfo() {
        
        Map<String, Object> infos = new HashMap();
        infos.put("msg", "Hello World");
        
        return new ResponseEntity(infos,HttpStatus.OK);
        
    }

    @PostMapping(value="/token")
    public ResponseEntity<SimpleEnvelope> createToken(@RequestBody TokenData data) {
        
        Map<String, String> out = new HashMap();

        String token = tokenService.createToken(data);
        out.put("userid", data.getEmail());
        out.put("token", token);
        
        SimpleEnvelope env = new SimpleEnvelope(out,true,HttpStatus.OK.value());
        
        HttpHeaders headers = new HttpHeaders();
        headers.set(TokenService.AUTHORIZATION_HEADER, TokenService.TOKEN_PREFIX + token);

        return new ResponseEntity(env,headers,HttpStatus.OK);
        
    } 
    
}