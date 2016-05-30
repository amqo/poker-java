/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.utils.dispatcher;

/**
 *
 * @author alberto
 */
public class GameEvent {

    private String type;
    private String source;
    private Object payload;

    public GameEvent() {
    }

    public GameEvent(String type, String source) {
        this.source = source;
        this.type = type;
    }

    public GameEvent(String type, String source, Object payload) {
        this.source = source;
        this.type = type;
        this.payload = payload;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
