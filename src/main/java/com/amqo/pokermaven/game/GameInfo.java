/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

import com.amqo.pokermaven.core.Card;
import com.amqo.pokermaven.utils.TexasHoldEmUtil;
import static com.amqo.pokermaven.utils.TexasHoldEmUtil.COMMUNITY_CARDS;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alberto
 */
public class GameInfo<P extends PlayerInfo> {

    private int round;
    private int dealer;
    private int playerTurn;
    private TexasHoldEmUtil.GameState gameState;
    private final List<Card> communityCards = new ArrayList<Card>(COMMUNITY_CARDS);
    private List<P> players;
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getDealer() {
        return dealer;
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public TexasHoldEmUtil.GameState getGameState() {
        return gameState;
    }

    public void setGameState(TexasHoldEmUtil.GameState gameState) {
        this.gameState = gameState;
    }

    public List<Card> getCommunityCards() {
        return new ArrayList<>(communityCards);
    }

    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards.clear();
        this.communityCards.addAll(communityCards);
    }

    public List<P> getPlayers() {
        return new ArrayList<>(players);
    }

    public void setPlayers(List<P> players) {
        this.players = new ArrayList<>(players);
    }

    public P getPlayer(int index) {
        return players.get(index);
    }

    public int getNumPlayers() {
        return players.size();
    }

    public boolean addPlayer(P player) {
        return this.players.add(player);
    }

    public boolean removePlayer(P player) {
        return this.players.remove(player);
    }

    public boolean addCommunityCard(Card card) {
        boolean result = false;
        if (communityCards.size() < TexasHoldEmUtil.COMMUNITY_CARDS) {
            result = communityCards.add(card);
        }
        return result;
    }

    public void clearCommunityCard() {
        this.communityCards.clear();
    }
}
