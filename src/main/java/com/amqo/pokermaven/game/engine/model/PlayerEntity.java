/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.model;

import com.amqo.pokermaven.game.BetCommand;
import com.amqo.pokermaven.game.PlayerInfo;

/**
 *
 * @author alberto
 */
public class PlayerEntity extends PlayerInfo {

    private int handValue = 0;
    private BetCommand betCommand;
    private boolean showCards;

    public PlayerEntity() {
    }

    public boolean showCards() {
        return showCards;
    }

    public void showCards(boolean showCards) {
        this.showCards = showCards;
    }

    public BetCommand getBetCommand() {
        return betCommand;
    }

    public void setBetCommand(BetCommand betCommand) {
        this.betCommand = betCommand;
    }

    public int getHandValue() {
        return handValue;
    }

    public void setHandValue(int handValue) {
        this.handValue = handValue;
    }
}
