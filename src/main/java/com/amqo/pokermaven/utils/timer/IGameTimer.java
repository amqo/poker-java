/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.utils.timer;

/**
 *
 * @author alberto
 */
public interface IGameTimer extends Runnable {

    public void exit();

    public long getTime();

    public void changeTimeoutId(Long timeoutId);

    public void setTime(long time);

    public void setNotifier(TimeoutNotifier notifier);
}
