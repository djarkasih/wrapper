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
public class Rules {
    
    public static String buildQiscusEmail(Application app, User user) {
        //userid_46955_6282213620002@kiwari-prod.com
        String email = "userid_" +
               user.getId() + "_" +
               user.getMobileNumber() + "@" +
               app.getQiscusSdkAppID();
        
        if (email.contains("+"))
            email = email.replace("+", "");
        
        if (!email.contains(".com")) {
            email = email + ".com";
        }
        
        return email;
    }
    
}
