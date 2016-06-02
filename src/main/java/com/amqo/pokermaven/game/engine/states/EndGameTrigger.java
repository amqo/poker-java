/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.states;

import com.amqo.pokermaven.game.IStateTrigger;
import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.PlayerEntity;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.runner.notification.RunListener.ThreadSafe;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
@ThreadSafe
public class EndGameTrigger implements IStateTrigger<ModelContext> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EndGameTrigger.class);

    @Override
    public boolean execute(ModelContext model) {
        HashMap<String, Double> scores = new HashMap<>();
        List<PlayerEntity> players = model.getAllPlayers();
        players.stream().filter(p -> p.getChips() > 0).forEach(p -> {
            p.setLastRoundChips(p.getChips());
            p.setRoudsSurvival(1 + p.getRoudsSurvival());
        });
        int totales = players.size();
        players.sort((p0, p1) -> Integer.compare(p0.getRoudsSurvival(), p1.getRoudsSurvival()));
        int lastIndex = 0;
        PlayerEntity pe = players.get(0);
        int lastRoundSurvival = pe.getRoudsSurvival();
        long chips = pe.getLastRoundChips();
        for (int i = 1; i < totales; i++) {
            pe = players.get(i);
            if (lastRoundSurvival < pe.getRoudsSurvival()) {
                calculateScores(scores, players, chips, lastIndex, i);
                chips = pe.getLastRoundChips();
                lastRoundSurvival = pe.getRoudsSurvival();
                lastIndex = i;
            } else {
                chips += pe.getLastRoundChips();
            }
        }
        calculateScores(scores, players, chips, lastIndex, totales);
        double scoreTotal = scores.values().stream().reduce(0., (accumulator, item) -> accumulator + item);
        LOGGER.debug("score: ", (scoreTotal / totales));
        for (PlayerEntity player : players) {
            LOGGER.debug("{}. score: {}, rounds: {}, last Chips: {}", player.getName(), scores.get(player.getName()), player.getRoudsSurvival(), player.getLastRoundChips());
        }
        model.setScores(scores);
        model.setCommunityCards(Collections.emptyList());
        return true;
    }

    private static void calculateScores(Map<String, Double> scores, List<PlayerEntity> players, double chips, int start, int end) {
        double parts = players.size() - 1.0;
        double min = start / parts;
        double max = (end - 1) / parts;
        if (start == (end - 1)) {
            scores.put(players.get(start).getName(), max);
        } else {
            double range = max - min;
            for (int i = start; i < end; i++) {
                PlayerEntity pe = players.get(i);
                scores.put(pe.getName(), min + range * (pe.getLastRoundChips() / chips));
            }
        }
    }
}
