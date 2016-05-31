/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.states;

import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.PlayerEntity;
import com.amqo.pokermaven.interfaces.IState;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.PlayerState;
import java.util.List;

/**
 *
 * @author alberto
 */
public class WinnerState implements IState<ModelContext> {

    public static final String NAME = "Winner";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean execute(ModelContext model) {
        List<PlayerEntity> players = model.getPlayers();
        players.stream()
                .filter(p -> p.isActive() || p.getState() == PlayerState.ALL_IN)
                .findFirst()
                .get()
                .addChips(players
                        .stream()
                        .mapToLong(p -> p.getBet())
                        .sum());
        return true;
    }
}
