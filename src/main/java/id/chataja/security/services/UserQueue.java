/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.services;

import id.chataja.security.jpa.UserDataRepository;
import id.chataja.security.model.UserData;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author ahmad
 */
@Service
public class UserQueue {    
    
    Logger logger = LoggerFactory.getLogger(UserQueue.class);
    
    @Autowired
    private UserDataRepository userDataRepository;
    
    private static BlockingQueue<UserData> queue = new LinkedBlockingDeque();
    
    public void push(UserData data) {
        
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
    @Scheduled(fixedRate = 30_000)
    private void processQueue() {
        if (queue.size() > 0) {
            
            UserData data = queue.poll();
            if (data != null) {
                
                if (! userDataRepository.existsByEmail(data.getEmail())) {
                    userDataRepository.save(data);
                    logger.info("[queue] " + data.toString() + " saved.");
                }
                
            }
            
        }
    }
    
}
