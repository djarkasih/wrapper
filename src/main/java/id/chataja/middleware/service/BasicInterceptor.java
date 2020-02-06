/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.middleware.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author ahmad
 */
@Service
public class BasicInterceptor implements RequestInterceptor {

    private final Map<String,String> headers = new HashMap();
    
    @Override
    public void addHeaders(Map<String, String> headers) {

        headers.forEach((key,value)->{
            this.headers.put(key,value);
        });
        
    }

    @Override
    public void injectHeaders(HttpHeaders httpHeaders) {

        this.headers.forEach((key,value)->{
            if (httpHeaders.containsKey(key))
                httpHeaders.remove(key);
            httpHeaders.add(key, value);
        });
        
    }
    
}
