/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.middleware.error;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author ahmad
 */
@RestControllerAdvice
public class ProxyErrorHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String,Object>> handleOtherExceptions(Exception ex, 
            HttpServletRequest req,
            HttpServletResponse res) {
        
        Map<String,Object> infos = new HashMap();
        
        infos.put("uri :", req.getRequestURI());
        infos.put("response status : ", res.getStatus());
        infos.put("response content type : ", res.getContentType());
        infos.put("msg : ", ex.getMessage());
        
        return new ResponseEntity<>(infos,HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

}
