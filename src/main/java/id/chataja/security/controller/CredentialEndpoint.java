/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.controller;

import id.chataja.security.jpa.UserDataRepository;
import id.chataja.security.model.UserData;
import id.chataja.security.services.TokenService;
import id.chataja.security.services.UserQueue;
import id.chataja.util.NetworkHelper;
import id.chataja.util.rest.SimpleEnvelope;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private Environment env;

    @Value("${wrapper.client.appid}")
    private String appId;

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserDataRepository repo;
    
    @Autowired
    private UserQueue queue;
        
    @GetMapping(value="/info")
    public ResponseEntity<Map<String,Object>> showInfo() {
        
        Map<String, Object> infos = new HashMap();
        infos.put(this.appId + " user(s) : ", repo.countByClientId(this.appId) + " user(s)");
        infos.put("User queue length : ", queue.length() + " job(s)");
        
        return new ResponseEntity(infos,HttpStatus.OK);
        
    }

    @GetMapping(value="/users")
    public ResponseEntity<SimpleEnvelope> getUsers() {
        
        Set<String> profiles = Set.of(env.getActiveProfiles());
        
        Map<String, Object> data = new HashMap();
        data.put(this.appId + " user(s) : ", repo.countByClientId(this.appId) + " user(s)");
        List<UserData> users = null;
        
        if (profiles.contains("dev")) {
            users = repo.findByClientId(this.appId);
            
            data.put("data", users);
        }
        
        SimpleEnvelope env = new SimpleEnvelope(data,true,HttpStatus.OK.value());
        
        return new ResponseEntity(env,HttpStatus.OK);
        
    }

    @PostMapping(value="/token")
    public ResponseEntity<SimpleEnvelope> createToken(HttpServletRequest req, @RequestBody @Valid UserData data) {
        
        Map<String, String> out = new HashMap();
        data.setClientId(this.appId);
        data.setClientAddress(NetworkHelper.getClientAddress(req));
        
        queue.push(data);

        String token = tokenService.createToken(data);
        out.put("userid", data.getEmail());
        out.put("token", token);
        
        SimpleEnvelope env = new SimpleEnvelope(out,true,HttpStatus.OK.value());
        
        return new ResponseEntity(env,HttpStatus.OK);
        
    } 
    
}
