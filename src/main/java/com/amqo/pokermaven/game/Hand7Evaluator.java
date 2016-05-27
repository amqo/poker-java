/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

import com.amqo.pokermaven.core.Card;
import com.amqo.pokermaven.interfaces.IHandEvaluator;
import static com.amqo.pokermaven.utils.TexasHoldEmUtil.COMMUNITY_CARDS;
import static com.amqo.pokermaven.utils.TexasHoldEmUtil.PLAYER_CARDS;
import java.util.List;

/**
 *
 * @author alberto
 */
public class Hand7Evaluator {

    public static final int TOTAL_CARDS = PLAYER_CARDS + COMMUNITY_CARDS;
    private final int[] combinatorialBuffer = new int[COMMUNITY_CARDS];
    private final Combination combinatorial
            = new Combination(COMMUNITY_CARDS, TOTAL_CARDS);
    private final IHandEvaluator evaluator;
    private final Card[] evalBuffer = new Card[COMMUNITY_CARDS];
    private final Card[] cards = new Card[TOTAL_CARDS];
    private int communityCardsValue = 0;

    public Hand7Evaluator(IHandEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setCommunityCards(List<Card> cc) {
        int i = 0;
        for (Card card : cc) {
            evalBuffer[i] = card;
            cards[i++] = card;
        }
        communityCardsValue = evaluator.eval(evalBuffer);
    }

    public int eval(Card c0, Card c1) {
        cards[COMMUNITY_CARDS] = c0;
        cards[COMMUNITY_CARDS + 1] = c1;
        return evalCards();
    }

    static Card[] copy(Card[] src, Card[] target, int[] positions) {
        int i = 0;
        for (int p : positions) {
            target[i++] = src[p];
        }
        return target;
    }

    private int evalCards() {
        combinatorial.clear();
        combinatorial.next(combinatorialBuffer);
        int result = communityCardsValue;
        while (combinatorial.hasNext()) {
            result = Math.max(result, evaluator.eval(
                    copy(cards, evalBuffer, combinatorial.next(combinatorialBuffer))));
        }
        return result;
    }
}
