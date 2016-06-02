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
@FunctionalInterface
public interface TimeoutNotifier {

    public void notify(Long timeoutId);
}
