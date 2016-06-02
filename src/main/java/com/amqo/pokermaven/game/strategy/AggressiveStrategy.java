/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.strategy;

import com.amqo.pokermaven.game.BetCommand;
import com.amqo.pokermaven.game.GameInfo;
import com.amqo.pokermaven.game.PlayerInfo;
import com.amqo.pokermaven.utils.TexasHoldEmUtil;

/**
 *
 * @author alberto
 */
public class AggressiveStrategy implements IStrategy {

    private final String name;

    public AggressiveStrategy(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BetCommand getCommand(GameInfo<PlayerInfo> state) {
        return new BetCommand(TexasHoldEmUtil.BetCommandType.ALL_IN);
    }
}
