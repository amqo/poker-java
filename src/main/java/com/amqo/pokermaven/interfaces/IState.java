/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.interfaces;

/**
 *
 * @author alberto
 */
public interface IState<T> {

    public String getName();

    public boolean execute(T context);
}
