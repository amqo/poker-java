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
@FunctionalInterface
public interface IStateTrigger<T> {

    public boolean execute(T context);
}
