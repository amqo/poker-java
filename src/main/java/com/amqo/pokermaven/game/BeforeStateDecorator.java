/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

import com.amqo.pokermaven.interfaces.IState;

/**
 *
 * @author alberto
 */
public class BeforeStateDecorator<T> implements IState<T> {

    private final IState<T> state;
    private final Runnable listener;
    private boolean executed = true;

    public BeforeStateDecorator(IState<T> state, Runnable listener) {
        this.state = state;
        this.listener = listener;
    }

    @Override
    public String getName() {
        return state.getName();
    }

    @Override
    public boolean execute(T context) {
        if (executed) {
            listener.run();
        }
        executed = state.execute(context);
        return executed;
    }
}
