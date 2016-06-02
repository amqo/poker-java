/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

import com.amqo.pokermaven.interfaces.IChecker;
import com.amqo.pokermaven.interfaces.IState;

/**
 *
 * @author alberto
 */
public class Transition<S extends Enum, T> {

    private final S origin;
    private final S target;
    private final IChecker<T> checker;

    public Transition(S origin, S target, IChecker<T> checker) {
        this.origin = origin;
        this.target = target;
        this.checker = checker;
    }

    public S getOrigin() {
        return origin;
    }

    public S getTarget() {
        return target;
    }

    public IChecker<T> getChecker() {
        return checker;
    }
}
