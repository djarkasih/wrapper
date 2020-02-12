/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.jpa;

import id.chataja.security.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ahmad
 */
public interface UserRepository extends CrudRepository<User,Integer> {

    public User findByMobileNumber(String mobileNumber);
    
}
