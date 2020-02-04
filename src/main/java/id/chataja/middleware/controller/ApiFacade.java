/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.middleware.controller;

import id.chataja.middleware.model.RequestData;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author ahmad
 */
@RestController
public class ApiFacade {
    
//    Logger logger = LoggerFactory.getLogger(ApiFacade.class);
    
    @Value("${wrapper.target.baseurl}")
    private String baseUrl;
    
    private Map<String, String> extractRequestHeaders(HttpServletRequest req) {

        Map<String,String> headers = new HashMap();
        
        Collections.list(req.getHeaderNames()).forEach(name -> {
            String value = req.getHeader(name);
            if (value != null)
                headers.put(name, value);
        });
        
        return headers;

    }
    
    private Map<String,Object> forwardRequest(RequestData reqd) {
        
        RestTemplate rest = new RestTemplate();

        String url = baseUrl + reqd.getRequestURI();
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        reqd.getQueries().forEach((String name, String[] values) -> {
            builder.queryParam(name, (Object[]) values);
        });
        URI uri = builder.build().toUri();
        
        HttpHeaders headers = new HttpHeaders();
        reqd.getHeaders().forEach((String name, String value)->{
            headers.add(name, value);
        });
        
        if (headers.containsKey("accept-encoding"))
            headers.remove("accept-encoding");
        
        HttpEntity entity;
        if ((reqd.getMethod() == HttpMethod.GET) || (reqd.getMethod() == HttpMethod.DELETE))
            entity = new HttpEntity(headers);
        else
            entity = new HttpEntity(reqd.getBody(),headers);
        
        ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
        ResponseEntity<Map<String,Object>> res = rest.exchange(
            uri.toString(), 
            reqd.getMethod(), 
            entity, 
            typeRef
        );
        
        return res.getBody();
    }
    
    @GetMapping(value="/info")
    public ResponseEntity<Map<String,Object>> showInfo() {
        
        Map<String, Object> infos = new HashMap();
        infos.put("baseUrl", this.baseUrl);
        
        return new ResponseEntity(infos,HttpStatus.OK);
        
    }

    @GetMapping(value="/**")
    public ResponseEntity<Map<String,Object>> getMethodHandler(
           HttpServletRequest req) {
        
        RequestData reqd = new RequestData(
            HttpMethod.GET,
            req.getRequestURI(),
            this.extractRequestHeaders(req),
            req.getParameterMap()
        );
        
//        logger.info("reqd = " + reqd.toString());
        
        Map<String,Object> res = this.forwardRequest(reqd);
        
        return new ResponseEntity(res,HttpStatus.OK);

    }
    
    @PostMapping(value="/**")
    public ResponseEntity<Map<String,Object>> postMethodHandler(
           HttpServletRequest req,
           @RequestBody Map<String, Object> body) {
        
        RequestData reqd = new RequestData(
            HttpMethod.POST,
            req.getRequestURI(),
            this.extractRequestHeaders(req),
            req.getParameterMap()
        );
        reqd.setBody(body);
                
//        logger.info("reqd = " + reqd.toString());
        
        Map<String,Object> res = this.forwardRequest(reqd);
        
        return new ResponseEntity(res,HttpStatus.OK);

    }

}
