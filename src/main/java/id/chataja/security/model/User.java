/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.security.model;

import java.time.LocalDateTime;
import java.util.Objects;
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
@Table(name = "users")
public class User {

    private Integer id;
    private String mobileNumber;
    private String fullname;
    private String email;
    private Integer appId;
    private String qiscusToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String qiscusEmail;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "phone_number", unique=true, nullable=false)
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

    @Column(nullable=false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "application_id", nullable=false)
    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Column(name = "qiscus_token", nullable=false)
    public String getQiscusToken() {
        return qiscusToken;
    }

    public void setQiscusToken(String qiscusToken) {
        this.qiscusToken = qiscusToken;
    }

    @Column(name = "created_at", nullable=false, columnDefinition = "TIMESTAMP")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "updated_at", nullable=false, columnDefinition = "TIMESTAMP")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getQiscusEmail() {
        return qiscusEmail;
    }

    public void setQiscusEmail(String qiscusEmail) {
        this.qiscusEmail = qiscusEmail;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.mobileNumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.mobileNumber, other.mobileNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", mobileNumber=" + mobileNumber + ", fullname=" + fullname + ", email=" + email + ", appId=" + appId + ", qiscusToken=" + qiscusToken + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", qiscusEmail=" + qiscusEmail + '}';
    }
    
}
