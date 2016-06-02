/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.states;

import com.amqo.pokermaven.game.IStateTrigger;
import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.PlayerEntity;
import java.util.ArrayList;
import java.util.List;
import org.junit.runner.notification.RunListener.ThreadSafe;

/**
 *
 * @author alberto
 */
@ThreadSafe
public class EndHandTrigger implements IStateTrigger<ModelContext> {

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
