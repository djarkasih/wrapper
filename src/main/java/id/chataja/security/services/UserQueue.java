/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.services;

import id.chataja.security.jpa.ApplicationRepository;
import id.chataja.security.model.TokenData;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import id.chataja.security.jpa.UserRepository;
import id.chataja.security.jpa.UserRoleRepository;
import id.chataja.security.model.Application;
import id.chataja.security.model.Client;
import id.chataja.security.model.Rules;
import id.chataja.security.model.User;
import id.chataja.security.model.UserRole;
import java.time.LocalDateTime;

/**
 *
 * @author ahmad
 */
@Service
public class UserQueue {    
    
    Logger logger = LoggerFactory.getLogger(UserQueue.class);
    
    @Autowired
    private Client client;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private ApplicationRepository appRepo;
    
    @Autowired
    private UserRoleRepository userRoleRepo;
    
    private static BlockingQueue<TokenData> queue = new LinkedBlockingDeque();
    
    public void push(TokenData data) {
        
        try {
            queue.put(data);
        } catch (InterruptedException ex) {
            logger.error("BlockingQueue Error : " + ex.getMessage());
        }
        
    }
    
    public long length() {
        return queue.size();
    }
    
    @Async
    @Scheduled(fixedRate = 15_000)
    private void processQueue() {
        if (queue.size() > 0) {
            
            TokenData data = null;
            
            try {
                data = queue.poll(1,TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(UserQueue.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            logger.info("data = " + data);
            
            if (data != null) {
                
                User user = userRepo.findByMobileNumber(data.getMobileNumber());
                
                if (user == null) {
                    
                    try {
                        
                        Application app = appRepo.findById(client.getAppId()).get();
                        
                        user = new User();
                        user.setAppId(client.getAppId());
                        user.setMobileNumber(data.getMobileNumber());
                        user.setEmail(data.getEmail());
                        user.setFullname(data.getFullname());
                        user.setQiscusToken("QISCUSTOKEN");
                        user.setQiscusEmail("YOUSHOULDNOTREADTHIS");
                        user.setCreatedAt(LocalDateTime.now());
                        user.setUpdatedAt(user.getCreatedAt());
                        
                         userRepo.save(user);
                         user.setQiscusEmail(Rules.buildQiscusEmail(app, user));
                         userRepo.save(user);

                    } catch (Exception ex) {
                        
                        logger.info("[saving user] ex = " + ex.getClass().getCanonicalName());
                        logger.info("ex = " + ex.getMessage());
                        if (ex.getCause() != null) {
                            logger.info("cause = " + ex.getCause().getMessage());
                        }

                    }
                    
                }
                
                if (user != null) {
                    
                    try {
                        
                        UserRole userRole = new UserRole();
                        userRole.setRoleId(client.getRoleId());
                        userRole.setUserId(user.getId());
                        userRole.setCreatedAt(LocalDateTime.now());
                        userRole.setUpdatedAt(userRole.getCreatedAt());

                        userRoleRepo.save(userRole);

                        logger.info("userRole = " + userRole);

                    } catch (Exception ex) {
                        
                        logger.info("[saving role] ex = " + ex.getClass().getCanonicalName());
                        logger.info("ex = " + ex.getMessage());
                        if (ex.getCause() != null) {
                            logger.info("cause = " + ex.getCause().getMessage());
                        }
                        
                    }
                    
                }
                
            }
            
        }
    }
    
}
