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
public enum ConnectorGameEventType {
    CREATE_GAME,
    ADD_PLAYER,
    INIT_GAME,
    BET_COMMAND,
    TIMEOUT,
    EXIT,
}
