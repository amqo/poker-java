/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.interfaces;

import com.amqo.pokermaven.Card;

/**
 *
 * @author alberto
 */
public interface IHandEvaluator {

    public int eval(Card[] cards);
}