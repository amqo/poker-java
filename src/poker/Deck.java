/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author alberto
 */
public class Deck {

    private final List<Card> cards;
    private int index = 0;

    public Deck() {
        this.cards = getAllCards();
    }

    public Card obtainCard() {
        Card result = null;
        if (index < cards.size()) {
            result = cards.get(index);
            index++;
        }
        return result;
    }

    public void shuffle() {
        index = 0;
        Collections.shuffle(cards);
    }

    public static List<Card> getAllCards() {
        int numCards = Card.Suit.values().length * Card.Rank.values().length;
        List<Card> result = new ArrayList<>(numCards);
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                result.add(new Card(suit, rank));
            }
        }
        return result;
    }
}
