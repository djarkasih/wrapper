/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author ahmad
 */
@Entity
@Table(name = "applications")
public class Application {
    
    public final static String QISCUS_SDK_APP_ID = "QISCUS-SDK-APP-ID";
    public final static String QISCUS_SDK_SECRET = "QISCUS-SDK-SECRET";
    
    private Integer id;
    private String qiscusSdkAppID;
    private String qiscusSdkSecret;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "app_id", nullable=false)
    public String getQiscusSdkAppID() {
        return qiscusSdkAppID;
    }

    public void setQiscusSdkAppID(String qiscusSdkAppID) {
        this.qiscusSdkAppID = qiscusSdkAppID;
    }

    @Column(name = "qiscus_sdk_secret", nullable=false)
    public String getQiscusSdkSecret() {
        return qiscusSdkSecret;
    }

    public void setQiscusSdkSecret(String qiscusSdkSecret) {
        this.qiscusSdkSecret = qiscusSdkSecret;
    }
    
}
