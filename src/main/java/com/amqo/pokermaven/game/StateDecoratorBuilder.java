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
public class StateDecoratorBuilder<T> {

    private IStateTrigger<T> trigger;

    private StateDecoratorBuilder(IStateTrigger<T> state) {
        this.trigger = state;
    }

    public static <T> StateDecoratorBuilder<T> create(IStateTrigger<T> state) {
        return new StateDecoratorBuilder<>(state);
    }

    public StateDecoratorBuilder<T> after(Runnable r) {
        this.trigger = new AfterTriggerDecorator<>(trigger, r);
        return this;
    }

    public StateDecoratorBuilder<T> before(Runnable r) {
        this.trigger = new BeforeTriggerDecorator<>(trigger, r);
        return this;
    }

    public IStateTrigger<T> build() {
        return trigger;
    }

    public static <T> IStateTrigger<T> after(IStateTrigger<T> state, Runnable r) {
        return new AfterTriggerDecorator<>(state, r);
    }

    public static <T> IStateTrigger<T> before(IStateTrigger<T> state, Runnable r) {
        return new BeforeTriggerDecorator<>(state, r);
    }
}
