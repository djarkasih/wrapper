/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.middleware.service;

import java.util.Map;
import org.springframework.http.HttpHeaders;

/**
 *
 * @author ahmad
 */
public interface RequestInterceptor {
    
    public void addHeaders(Map<String,String> headers);
            
    public void injectHeaders(HttpHeaders headers);
    
}
