/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

/**
 *
 * @author alberto
 */
public final class Card {

    public static enum Suit {
        SPADE, HEART, DIAMOND, CLUB
    }

    public static final String STRING_RANK_CARDS = "23456789TJQKA";
    public static enum Rank {
        TWO, TRHEE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }
    
    @Override
    public String toString() {
        int rankValue = rank.ordinal();
        return STRING_RANK_CARDS
                .substring(rankValue, rankValue + 1)
                .concat(suit.name().substring(0, 1));
    }
}
