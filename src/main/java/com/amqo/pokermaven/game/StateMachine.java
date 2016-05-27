/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

import com.amqo.pokermaven.interfaces.IChecker;
import com.amqo.pokermaven.interfaces.IState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alberto
 */
public class StateMachine<T> {

    private IState<T> initState = null;
    private final Map<String, IState<T>> defaultTransition = new HashMap<>();
    private final Map<String, List<Transition<T>>> transitions = new HashMap<>();

    public StateMachine() {
    }

    List<Transition<T>> getTransitionsByOrigin(IState<T> state) {
        List<Transition<T>> result = transitions.get(state.getName());
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    public void setInitState(IState<T> initState) {
        this.initState = initState;
    }

    public IState<T> getDefaultTransition(IState<T> origin) {
        return defaultTransition.get(origin.getName());
    }

    public void setDefaultTransition(IState<T> origin, IState<T> target) {
        this.defaultTransition.put(origin.getName(), target);
    }

    public void addTransition(Transition<T> transition) {
        IState<T> origin = transition.getOrigin();
        List<Transition<T>> listTransitions = transitions.get(origin.getName());
        if (listTransitions == null) {
            listTransitions = new ArrayList<>();
            transitions.put(origin.getName(), listTransitions);
        }
        listTransitions.add(transition);
    }

    public void addTransition(IState<T> origin, IState<T> target, IChecker<T> checker) {
        addTransition(new Transition<>(origin, target, checker));
    }

    public StateMachineInstance<T> startInstance(T data) {
        return new StateMachineInstance(data, this, initState).execute();
    }
}
