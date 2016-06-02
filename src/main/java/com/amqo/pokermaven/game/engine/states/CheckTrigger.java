/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.states;

import com.amqo.pokermaven.game.IStateTrigger;
import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.ModelUtil;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.GameState;
import static com.amqo.pokermaven.utils.TexasHoldEmUtil.PlayerState.READY;
import org.junit.runner.notification.RunListener.ThreadSafe;

/**
 *
 * @author alberto
 */
@ThreadSafe
public class CheckTrigger implements IStateTrigger<ModelContext> {

    private static final GameState[] GAME_STATE = GameState.values();
    private static final int[] OBATIN_CARDS = {3, 1, 1, 0, 0};

    private int indexByGameState(GameState gameState) {
        int i = 0;
        while (i < GAME_STATE.length && GAME_STATE[i] != gameState) {
            i++;
        }
        return i;
    }

    @Override
    public boolean execute(ModelContext model) {
        int indexGameState = indexByGameState(model.getGameState());
        if (OBATIN_CARDS[indexGameState] > 0) {
            model.addCommunityCards(OBATIN_CARDS[indexGameState]);
        }
        model.setGameState(GAME_STATE[indexGameState + 1]);
        model.setBets(0);
        model.getPlayers().stream().filter(p -> p.isActive()).forEach(p -> p.setState(READY));
        model.setPlayerTurn(ModelUtil.nextPlayer(model, model.getDealer()));
        model.setLastBetCommand(null);
        model.setLastPlayerBet(null);
        return true;
    }
}
