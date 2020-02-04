/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.chataja.util.rest;

/**
 *
 * @author ahmad
 */
public class SimpleEnvelope extends Envelope {
    
    private final Object payload;

    public SimpleEnvelope(Object payload, boolean success, int code) {
        super(success, code);
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }
    
}
