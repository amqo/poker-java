/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.controller;

/**
 *
 * @author alberto
 */
public enum PokerEventType {
    INIT_HAND,
    END_HAND,
    END_GAME,
    BET_COMMAND,
    CHECK,
    GET_COMMAND,
    EXIT
}
