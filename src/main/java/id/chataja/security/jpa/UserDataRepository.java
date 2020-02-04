/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.jpa;

import id.chataja.security.model.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ahmad
 */
@Repository
public interface UserDataRepository extends CrudRepository<UserData,Long> {
    
    long countByClientId(String clientId);
    public boolean existsByEmail(String email);
    
}
