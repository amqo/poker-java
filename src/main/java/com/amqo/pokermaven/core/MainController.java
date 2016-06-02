/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.core;

import com.amqo.pokermaven.game.GameException;
import com.amqo.pokermaven.game.Settings;
import com.amqo.pokermaven.game.controller.GameController;
import com.amqo.pokermaven.game.strategy.IStrategy;
import com.amqo.pokermaven.game.strategy.RandomStrategy;
import com.amqo.pokermaven.gui.TexasHoldEmView;
import com.amqo.pokermaven.interfaces.IGameController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author alberto
 */
public final class MainController {

    private static final int PLAYERS = 10;

    public static void main(String[] args) throws InterruptedException, GameException {
        IStrategy strategyMain = new RandomStrategy("RandomStrategy-0");
        TexasHoldEmView texasHoldEmView = new TexasHoldEmView(strategyMain);
        texasHoldEmView.setVisible(true);
        strategyMain = texasHoldEmView.getStrategy();
        List<IStrategy> strategies = new ArrayList<>();
        strategies.add(strategyMain);
        for (int i = 1; i < PLAYERS; i++) {
            strategies.add(new RandomStrategy("RandomStrategy-" + String.valueOf(i)));
        }
        Collections.shuffle(strategies);
        Settings settings = new Settings();
        settings.setMaxErrors(3);
        settings.setMaxPlayers(PLAYERS);
        settings.setMaxRounds(1000);
        settings.setTime(500);
        settings.setPlayerChip(5000L);
        settings.setRounds4IncrementBlind(20);
        settings.setSmallBind(settings.getPlayerChip() / 100);
        IGameController controller = new GameController();
        controller.setSettings(settings);
        for (IStrategy strategy : strategies) {
            controller.addStrategy(strategy);
        }
        controller.start();
    }
}
