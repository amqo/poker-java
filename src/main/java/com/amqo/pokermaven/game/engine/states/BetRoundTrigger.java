/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.states;

import com.amqo.pokermaven.game.BetCommand;
import com.amqo.pokermaven.game.IStateTrigger;
import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.ModelUtil;
import com.amqo.pokermaven.game.engine.model.PlayerEntity;
import com.amqo.pokermaven.utils.TexasHoldEmUtil;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.BetCommandType;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.PlayerState;
import java.util.EnumMap;
import java.util.Map;
import org.junit.runner.notification.RunListener.ThreadSafe;

/**
 *
 * @author alberto
 */
@ThreadSafe
public class BetRoundTrigger implements IStateTrigger<ModelContext> {

    @FunctionalInterface
    private interface BetChecker {

        public boolean check(ModelContext model, PlayerEntity player, BetCommand bet);
    }

    private static final Map<BetCommandType, BetChecker> CHECKERS = buildBetCommandChecker();

    private static Map<BetCommandType, BetChecker> buildBetCommandChecker() {
        Map<BetCommandType, BetChecker> result = new EnumMap<>(BetCommandType.class);
        result.put(BetCommandType.FOLD, (m, p, b) -> true);
        result.put(BetCommandType.TIMEOUT, (m, p, b) -> false);
        result.put(BetCommandType.ERROR, (m, p, b) -> false);
        result.put(BetCommandType.RAISE, (m, p, b) -> b.getChips() > (m.getHighBet() - p.getBet()) && b.getChips() < p.getChips());
        result.put(BetCommandType.ALL_IN, (m, p, b) -> {
            b.setChips(p.getChips());
            return p.getChips() > 0;
        });

        result.put(BetCommandType.CALL, (c, p, b) -> {
            b.setChips(c.getHighBet() - p.getBet());
            return c.getHighBet() > c.getSettings().getBigBind();
        });

        result.put(BetCommandType.CHECK, (c, p, b) -> {
            b.setChips(c.getHighBet() - p.getBet());
            return b.getChips() == 0 || c.getHighBet() == c.getSettings().getBigBind();
        });
        return result;
    }

    @Override
    public boolean execute(ModelContext model) {
        boolean result = false;
        int playerTurn = model.getPlayerTurn();
        PlayerEntity player = model.getPlayer(playerTurn);
        BetCommand command = player.getBetCommand();
        if (command != null) {
            BetCommand resultCommand = command;
            player.setBetCommand(null);
            long betChips = 0;
            BetCommandType commandType = command.getType();
            if (CHECKERS.get(commandType).check(model, player, command)) {
                betChips = command.getChips();
                player.setState(TexasHoldEmUtil.convert(command.getType()));
            } else {
                commandType = BetCommandType.FOLD;
                player.setState(PlayerState.FOLD);
                if (command.getType() == BetCommandType.TIMEOUT) {
                    resultCommand = new BetCommand(BetCommandType.TIMEOUT);
                } else {
                    resultCommand = new BetCommand(BetCommandType.ERROR);
                }
                ModelUtil.incrementErrors(player, model.getSettings());
            }
            ModelUtil.playerBet(model, player, commandType, betChips);
            model.lastResultCommand(player, resultCommand);
            model.setPlayerTurn(ModelUtil.nextPlayer(model, playerTurn));
            result = true;
        }
        return result;
    }
}
