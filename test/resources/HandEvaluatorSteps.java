/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertEquals;
import poker.Card;
import poker.Card.Rank;
import poker.Card.Suit;
import poker.HandEvaluator;
import poker.Hands;
import poker.IHandEvaluator;

/**
 *
 * @author alberto
 */
public class HandEvaluatorSteps {

    private static final String[] VALORES = {"mano0", "iguales", "mano1"};
    private IHandEvaluator handEvaluator;
    private String resultado;

    @Given("^un IHandEvaluator$")
    public void un_IHandEvaluator() throws Throwable {
        handEvaluator = new HandEvaluator();
    }

    @When("^calculamos la comparacion entre (.*) y (.*)$")
    public void calculamos_la_comparacion(String hand0, String hand1) throws Throwable {
        int evalhand0 = handEvaluator.eval(fromString2Cards(hand0));
        int evalhand1 = handEvaluator.eval(fromString2Cards(hand1));
        int diferencia = evalhand1 - evalhand0;
        if (diferencia != 0) {
            diferencia = Math.abs(diferencia) / diferencia;
        }
        resultado = VALORES[diferencia + 1];
    }
    
    private Card[] fromString2Cards(String hand) {
        Card[] result = new Card[Hands.CARDS];
        String[] cards = hand.split(" ");
        int index = 0;
        for (String card : cards) {
            int rankIndex = Card.STRING_RANK_CARDS.indexOf(card.charAt(0));
            Rank rank = Card.Rank.values()[rankIndex];
            int suitIndex = Card.STRING_SUIT_CARDS.indexOf(card.charAt(1));
            Suit suit = Card.Suit.values()[suitIndex];
            result[index] = new Card(suit, rank);
        }
        return result;
    }

    @Then("^el resultado esperado es (.*)$")
    public void el_resultado_esperado_es(String expResult) throws Throwable {
        assertEquals(expResult, resultado);
    }
}
