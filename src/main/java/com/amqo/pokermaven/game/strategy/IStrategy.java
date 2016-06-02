/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.strategy;

import com.amqo.pokermaven.game.BetCommand;
import com.amqo.pokermaven.core.Card;
import com.amqo.pokermaven.game.GameInfo;
import com.amqo.pokermaven.game.PlayerInfo;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alberto
 */
@FunctionalInterface
public interface IStrategy {

    public String getName();

    public default BetCommand getCommand(GameInfo<PlayerInfo> state) {
        return null;
    }

    public default void initHand(GameInfo<PlayerInfo> state) {
    }
    
    public default void endHand(GameInfo<PlayerInfo> state) {
    }
    
    public default void endGame(Map<String, Double> scores) {
    }

    public default void check(List<Card> communityCards) {
    }

    public default void onPlayerCommand(String player, BetCommand betCommand) {
    }
}
