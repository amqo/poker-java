/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

/**
 *
 * @author alberto
 */
public class AfterTriggerDecorator<T> implements IStateTrigger<T> {

    private final IStateTrigger<T> trigger;
    private final Runnable listener;

    public AfterTriggerDecorator(IStateTrigger<T> state, Runnable listener) {
        this.trigger = state;
        this.listener = listener;
    }

    @Override
    public boolean execute(T context) {
        boolean result = trigger.execute(context);
        if (result) {
            listener.run();
        }
        return result;
    }
}
