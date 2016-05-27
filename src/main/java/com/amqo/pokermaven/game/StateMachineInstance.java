/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

import com.amqo.pokermaven.interfaces.IState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
public class StateMachineInstance<T> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineInstance.class);

    private final T context;
    private final StateMachine<T> parent;
    private IState<T> state;
    private boolean finish;
    private boolean pause;

    public StateMachineInstance(T context, StateMachine<T> parent, IState<T> state) {
        this.context = context;
        this.parent = parent;
        this.state = state;
        this.finish = false;
    }

    public boolean isFinish() {
        return finish;
    }

    public StateMachineInstance<T> execute() {
        this.pause = false;
        while (state != null && !pause) {
            state = executeState();
        }
        finish = state == null;
        return this;
    }

    public T getContext() {
        return context;
    }

    private IState<T> executeState() {
        LOGGER.info("state \"" + state.getName() + "\" executing...");
        pause = !state.execute(context);
        IState<T> result = state;
        if (!pause) {
            LOGGER.info("state \"" + state.getName() + "\" [executed]");
            for (Transition<T> transition : parent.getTransitionsByOrigin(state)) {
                if (transition.getChecker().check(context)) {
                    return transition.getTarget();
                }
            }
            result = parent.getDefaultTransition(state);
            LOGGER.info("execute finish");
        } else {
            LOGGER.info("state \"" + state.getName() + "\" [paused]");
        }
        return result;
    }
}
