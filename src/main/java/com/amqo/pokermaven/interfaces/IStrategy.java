/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.interfaces;

import com.amqo.pokermaven.game.BetCommand;
import com.amqo.pokermaven.core.Card;
import com.amqo.pokermaven.game.GameInfo;
import com.amqo.pokermaven.game.PlayerInfo;
import java.util.List;

/**
 *
 * @author alberto
 */
public interface IStrategy {

    public String getName();

    public BetCommand getCommand(GameInfo<PlayerInfo> state);

    public default void updateState(GameInfo<PlayerInfo> state) {
    }

    public default void check(List<Card> communityCards) {
    }

    public default void onPlayerCommand(String player, BetCommand betCommand) {
    }
}
