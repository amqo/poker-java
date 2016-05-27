/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven;

import com.amqo.pokermaven.core.Card;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
public class Poker {

    private static final Logger LOGGER = LoggerFactory.getLogger(Poker.class);

    private static void insert(Set<Card> cards, Card card) {
        if (!cards.contains(card)) {
            LOGGER.info("insertamos la carta: {}", card);
            cards.add(card);
        } else {
            LOGGER.info("la carta: {} ya estaba en el conjunto", card);
        }
    }

    public static void main(String[] args) {
        Set<Card> cards = new HashSet<>();
        Card[] cards2Insert = {
            new Card(Card.Suit.CLUB, Card.Rank.ACE),
            new Card(Card.Suit.CLUB, Card.Rank.TWO),
            new Card(Card.Suit.CLUB, Card.Rank.TRHEE),
            new Card(Card.Suit.CLUB, Card.Rank.ACE),
            //new Card(Card.Suit.CLUB, null)
        };
        for (Card card : cards2Insert) {
            insert(cards, card);
        }
    }

}
