/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.states;

import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.PlayerEntity;
import com.amqo.pokermaven.interfaces.IState;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alberto
 */
public class EndState implements IState<ModelContext> {

    public static final String NAME = "EndHand";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean execute(ModelContext model) {
        PlayerEntity dealerPlayer = model.getPlayer(model.getDealer());
        List<PlayerEntity> players = model.getPlayers();
        List<PlayerEntity> nextPlayers = new ArrayList<>(players.size());
        int i = 0;
        int dealerIndex = 0;
        for (PlayerEntity p : players) {
            if (p.getChips() > 0) {
                nextPlayers.add(p);
                i++;
            }
            if (dealerPlayer == p) {
                dealerIndex = i - 1;
            }
        }
        model.setDealer(dealerIndex);
        model.setPlayers(nextPlayers);
        return true;
    }
}
