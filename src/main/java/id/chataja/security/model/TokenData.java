/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.model;

/**
 *
 * @author ahmad
 */
public class TokenData {
    
    private final String email;
    private final String mobileNumber;
    private final String fullname;

    public TokenData(String email, String mobileNumber, String fullname) {
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getFullname() {
        return fullname;
    }

    @Override
    public String toString() {
        return "TokenData{" + "email=" + email + ", mobileNumber=" + mobileNumber + ", fullname=" + fullname + '}';
    }
    
}
