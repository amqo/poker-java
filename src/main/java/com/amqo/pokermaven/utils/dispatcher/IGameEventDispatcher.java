/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.utils.dispatcher;

/**
 *
 * @author alberto
 */
public interface IGameEventDispatcher<E extends Enum> extends Runnable {

    public void dispatch(GameEvent<E> event);

    public void exit();
}
