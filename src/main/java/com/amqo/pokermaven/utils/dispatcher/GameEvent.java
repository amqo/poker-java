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
public class GameEvent<E extends Enum> {

    private E type;
    private String source;
    private Object payload;

    public GameEvent(E type, String source) {
        this(type, source, null);
    }

    public GameEvent(E type, String source, Object payload) {
        this.source = source;
        this.type = type;
        this.payload = payload;
    }

    public String getSource() {
        return source;
    }

    public E getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }
}
