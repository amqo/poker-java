/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

import com.amqo.pokermaven.interfaces.IChecker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alberto
 */
public class StateMachine<S extends Enum, T> {

    private static final IStateTrigger<?> DEFAULT_TRIGGER = (c) -> true;
    private S initState = null;
    private final Map<S, IStateTrigger<T>> triggersByState = new HashMap<>();
    private final Map<S, S> defaultTransition = new HashMap<>();
    private final Map<S, List<Transition<S, T>>> transitions = new HashMap<>();

    List<Transition<S, T>> getTransitionsByOrigin(S state) {
        List<Transition<S, T>> result = transitions.get(state);
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    public void setInitState(S initState) {
        this.initState = initState;
    }

    public void setTrigger(S state, IStateTrigger<T> trigger) {
        triggersByState.put(state, trigger);
    }

    public IStateTrigger<T> getTrigger(S state) {
        IStateTrigger<T> result = triggersByState.get(state);
        if (result == null) {
            result = (IStateTrigger<T>) DEFAULT_TRIGGER;
        }
        return result;
    }

    public S getDefaultTransition(S origin) {
        return defaultTransition.get(origin);
    }

    public void setDefaultTransition(S origin, S target) {
        this.defaultTransition.put(origin, target);
    }

    public void addTransition(Transition<S, T> transition) {
        S origin = transition.getOrigin();
        List<Transition<S, T>> listTransitions = transitions.get(origin);
        if (listTransitions == null) {
            listTransitions = new ArrayList<>();
            transitions.put(origin, listTransitions);
        }
        listTransitions.add(transition);
    }

    public void addTransition(S origin, S target, IChecker<T> checker) {
        addTransition(new Transition<>(origin, target, checker));
    }

    public StateMachineInstance<S, T> startInstance(T data) {
        return new StateMachineInstance(data, this, initState).execute();
    }
}
