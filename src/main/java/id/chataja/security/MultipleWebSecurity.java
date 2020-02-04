/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.chataja.security.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author ahmad
 */

@EnableWebSecurity
public class MultipleWebSecurity {
    
    @Configuration
    @Order(1)                                                        
    public static class TokenServiceWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        
        @Autowired
        private ObjectMapper mapper;
        
        @Autowired
        private TokenService tokenService;
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, TokenService.TOKEN_SERVICE_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                //.addFilterBefore(new ExceptionHandlerFilter(), JwtAuthorizationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), mapper, tokenService),UsernamePasswordAuthenticationFilter.class)
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

//    @Configuration                                                   
//    public static class OtherWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//        http
//            .antMatcher("/**").
//            .addFilterBefore(new JwtAuthorizationFilter(authenticationManager()))
//            // this disables session creation on Spring Security
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        }
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        
        return source;
        
    }
    
}
