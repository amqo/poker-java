/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.states;

import com.amqo.pokermaven.core.Card;
import com.amqo.pokermaven.game.Hand7Evaluator;
import com.amqo.pokermaven.game.IStateTrigger;
import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.PlayerEntity;
import com.amqo.pokermaven.utils.HandEvaluator;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.PlayerState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.runner.notification.RunListener.ThreadSafe;

/**
 *
 * @author alberto
 */
@ThreadSafe
public class ShowDownTrigger implements IStateTrigger<ModelContext> {

    private List<PlayerEntity> calculateHandValue(List<Card> communityCards, List<PlayerEntity> players) {
        Hand7Evaluator evaluator = new Hand7Evaluator(new HandEvaluator());
        evaluator.setCommunityCards(communityCards);
        players.stream().forEach(p -> p.setHandValue(evaluator.eval(p.getCard(0), p.getCard(1))));
        return players;
    }

    @Override
    public boolean execute(ModelContext model) {
        List<PlayerEntity> players = calculateHandValue(model.getCommunityCards(), model.getPlayers());
        Map<Long, List<PlayerEntity>> indexByBet = players.stream()
                .filter(p -> p.getBet() > 0)
                .collect(Collectors.groupingBy(p -> p.getBet()));
        List<Long> inverseSortBets = new ArrayList<>(indexByBet.keySet());
        Collections.sort(inverseSortBets, (l0, l1) -> Long.compare(l1, l0));

        Iterator<Long> it = inverseSortBets.iterator();
        List<PlayerEntity> lastPlayers = indexByBet.get(it.next());
        while (it.hasNext()) {
            List<PlayerEntity> currentPlayers = indexByBet.get(it.next());
            currentPlayers.addAll(lastPlayers);
            lastPlayers = currentPlayers;
        }

        Set<Long> bet4Analysis = players.stream()
                .filter(p -> p.getState() == PlayerState.ALL_IN)
                .map(p -> p.getBet()).collect(Collectors.toSet());
        bet4Analysis.add(inverseSortBets.get(0));
        long accumulateChips = 0L;
        long lastBet = 0L;
        while (!inverseSortBets.isEmpty()) {
            Long bet = inverseSortBets.remove(inverseSortBets.size() - 1);
            List<PlayerEntity> currentPlayers = indexByBet.get(bet);
            accumulateChips += (bet - lastBet) * currentPlayers.size();
            if (bet4Analysis.contains(bet)) {
                Collections.sort(currentPlayers, (p0, p1) -> p1.getHandValue() - p0.getHandValue());
                List<PlayerEntity> winners = new ArrayList<>(currentPlayers.size());
                currentPlayers.stream()
                        .filter(p -> p.getState() != PlayerState.OUT)
                        .peek(p -> p.showCards(true))
                        .filter(p -> winners.isEmpty() || p.getHandValue() == winners.get(0).getHandValue())
                        .forEach(winners::add);
                long chips4Player = accumulateChips / winners.size();
                winners.stream().forEach(p -> p.addChips(chips4Player));
                int remain = (int) accumulateChips % winners.size();
                if (remain > 0) {
                    Collections.shuffle(winners);
                    winners.stream().limit(remain).forEach(p -> p.addChips(1));
                }
                accumulateChips = 0L;
            }
            lastBet = bet;
        }
        return true;
    }
}
