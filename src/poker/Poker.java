/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
public class Poker {

    private static final Logger LOG = LoggerFactory.getLogger(Poker.class);
    
    public static void main(String[] args) {
        Card card = new Card(Card.Suit.CLUB, Card.Rank.ACE);
        LOG.info("As de tréboles: {}", card);
    }

}
