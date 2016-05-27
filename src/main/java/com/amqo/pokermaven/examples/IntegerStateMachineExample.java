/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.examples;

import com.amqo.pokermaven.game.StateMachine;
import com.amqo.pokermaven.game.StateMachineInstance;
import com.amqo.pokermaven.interfaces.IState;

/**
 *
 * @author alberto
 */
public class IntegerStateMachineExample {

    public static void main(String[] args) {
        StateMachine<Integer> sm = new StateMachine<>();
        IntState state1 = new IntState("State 1");
        IntState state2 = new IntState("State 2");
        IntState state3 = new IntState("State 3");
        IntState state4 = new IntState("State 4");
        sm.setInitState(state1);
        sm.addTransition(state1, state2, (n) -> (n % 2) == 0);
        sm.addTransition(state1, state3, (n) -> (n % 3) == 0);
        sm.setDefaultTransition(state1, state4);
        StateMachineInstance<Integer> smi = sm.startInstance(6);
    }

    private static class IntState implements IState<Integer> {

        private String name;

        public IntState(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean execute(Integer context) {
            return true;
        }
    }
}
