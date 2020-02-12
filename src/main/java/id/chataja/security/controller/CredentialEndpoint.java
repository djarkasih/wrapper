/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.controller;

import id.chataja.security.model.TokenData;
import id.chataja.security.services.TokenService;
import id.chataja.security.services.UserQueue;
import id.chataja.util.NetworkHelper;
import id.chataja.util.rest.SimpleEnvelope;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import id.chataja.security.jpa.UserRoleRepository;
import id.chataja.security.model.Client;

/**
 *
 * @author ahmad
 */
@RestController
public class CredentialEndpoint {

    @Autowired
    private Client client;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserRoleRepository repo;
    
    @Autowired
    private UserQueue queue;
        
    @GetMapping(value="/info")
    public ResponseEntity<Map<String,Object>> showInfo() {
        
        Map<String, Object> infos = new HashMap();
        infos.put(client.getClientName() + " user(s) : ", repo.countByRoleId(client.getRoleId()) + " user(s)");
        infos.put("User queue length : ", queue.length() + " job(s)");
        
        return new ResponseEntity(infos,HttpStatus.OK);
        
    }

    @PostMapping(value="/token")
    public ResponseEntity<SimpleEnvelope> createToken(HttpServletRequest req, @RequestBody @Valid TokenData data) {
        
        Map<String, String> out = new HashMap();
        data.setClientAddress(NetworkHelper.getClientAddress(req));
        
        queue.push(data);

        String token = tokenService.createToken(data);
        out.put("userid", data.getEmail());
        out.put("token", token);
        
        SimpleEnvelope env = new SimpleEnvelope(out,true,HttpStatus.OK.value());
        
        return new ResponseEntity(env,HttpStatus.OK);
        
    } 
    
}
