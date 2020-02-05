/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author ahmad
 */
@Entity
public class UserData implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @JsonIgnore
    private String clientId = "CA";
    @JsonIgnore
    @Transient
    private String clientAddress = "EMPTY";
    @Column(unique=true, nullable=false)
    @Email(message = "email should be in valid email address format")
    private String email;
    @NotBlank(message = "mobileNumber is mandatory")
    private String mobileNumber;
    @Size(min = 8, max = 64, message = "fullname must be between 8 and 64 characters")
    private String fullname;

    public UserData() {        
    }

    public Long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "UserData{" + "id=" + id + ", clientId=" + clientId + ", clientAddress=" + clientAddress + ", email=" + email + ", mobileNumber=" + mobileNumber + ", fullname=" + fullname + '}';
    }
    
}
