/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.util.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ahmad
 */
public class Envelope {
    
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    private final Date timestamp;
    private final boolean success;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message = null;

    public Envelope(boolean success, int code) {
        this.timestamp = Calendar.getInstance().getTime();
        this.success = success;
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return formatter.format(timestamp);
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
}
