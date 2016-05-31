/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.states;

import com.amqo.pokermaven.core.Deck;
import com.amqo.pokermaven.game.Settings;
import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.ModelUtil;
import com.amqo.pokermaven.game.engine.model.PlayerEntity;
import com.amqo.pokermaven.interfaces.IState;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.BetCommandType;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.GameState;
import static com.amqo.pokermaven.utils.TexasHoldEmUtil.MIN_PLAYERS;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.PlayerState;
import static com.amqo.pokermaven.utils.TexasHoldEmUtil.PlayerState.ALL_IN;
import java.util.List;

/**
 *
 * @author alberto
 */
public class InitHandState implements IState<ModelContext> {

    public static final String NAME = "InitHand";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean execute(ModelContext model) {
        Deck deck = model.getDeck();
        deck.shuffle();
        Settings settings = model.getSettings();
        model.setGameState(GameState.PRE_FLOP);
        model.clearCommunityCard();
        model.setRound(model.getRound() + 1);
        if (model.getRound() % settings.getRounds4IncrementBlind() == 0) {
            settings.setSmallBind(2 * settings.getSmallBind());
        }
        model.setPlayersAllIn(0);
        model.setHighBet(0L);
        List<PlayerEntity> players = model.getPlayers();
        for (PlayerEntity p : players) {
            p.setState(PlayerState.READY);
            p.setHandValue(0);
            p.setBet(0);
            p.showCards(false);
            p.setCards(deck.obtainCard(), deck.obtainCard());
        }
        int numPlayers = model.getNumPlayers();
        model.setActivePlayers(numPlayers);
        int dealerIndex = (model.getDealer() + 1) % numPlayers;
        model.setDealer(dealerIndex);
        model.setPlayerTurn((dealerIndex + 1) % numPlayers);
        if (numPlayers > MIN_PLAYERS) {
            compulsoryBet(model, settings.getSmallBind());
        }
        compulsoryBet(model, settings.getBigBind());
        return true;
    }

    private void compulsoryBet(ModelContext model, long chips) {
        int turn = model.getPlayerTurn();
        PlayerEntity player = model.getPlayer(turn);
        if (player.getChips() <= chips) {
            player.setState(PlayerState.ALL_IN);
            ModelUtil.playerBet(model, player, BetCommandType.ALL_IN, player.getChips());
        } else {
            ModelUtil.playerBet(player, chips);
        }
        model.setHighBet(chips);
        model.setPlayerTurn((turn + 1) % model.getNumPlayers());
    }
}
