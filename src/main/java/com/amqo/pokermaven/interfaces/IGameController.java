/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.interfaces;

import com.amqo.pokermaven.game.strategy.IStrategy;
import com.amqo.pokermaven.game.GameException;
import com.amqo.pokermaven.game.Settings;

/**
 *
 * @author alberto
 */
public interface IGameController {

    public void setSettings(Settings settings);

    public boolean addStrategy(IStrategy strategy);

    public void start() throws GameException;

    public void waitFinish();
}
