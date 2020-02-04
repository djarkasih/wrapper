/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.configs;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author ahmad
 */
@Configuration
public class JwtConfig {
    
    @Value("${wrapper.token.algorithm}")
    private String algolName;
    
    @Value("${wrapper.token.secretkey}")
    private String secretKey;

    @Bean
    public Key buildKey() {
        
        Key key = null;
        
        try {
            SignatureAlgorithm algol = SignatureAlgorithm.forName(this.algolName);

            String base64Key = DatatypeConverter.printBase64Binary(this.secretKey.getBytes());
            byte[] secret = DatatypeConverter.parseBase64Binary(base64Key);
            
            //byte[] secret = DatatypeConverter.parseBase64Binary(this.secretKey.getBytes());
            
            key = new SecretKeySpec(secret, algol.getJcaName());
        } catch (SignatureException ex) {
            
        }
        
        return key;
        
    }
    
}
