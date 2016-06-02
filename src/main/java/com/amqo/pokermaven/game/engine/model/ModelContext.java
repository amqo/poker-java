/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.engine.model;

import com.amqo.pokermaven.core.Card;
import com.amqo.pokermaven.core.Deck;
import com.amqo.pokermaven.game.BetCommand;
import com.amqo.pokermaven.game.GameInfo;
import com.amqo.pokermaven.game.Settings;
import com.amqo.pokermaven.utils.TexasHoldEmUtil;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.GameState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author alberto
 */
public class ModelContext {

    private final GameInfo<PlayerEntity> gameInfo = new GameInfo<>();
    private final HashMap<String, PlayerEntity> playersByName;
    private int activePlayers;
    private long highBet;
    private Deck deck;
    private int playersAllIn;
    private BetCommand lastBetCommand;
    private PlayerEntity lastPlayerBet;
    private int bets = 0;
    private HashMap<String, Double> scores;
    private List<PlayerEntity> allPlayers = new ArrayList<>(TexasHoldEmUtil.MAX_PLAYERS);

    public ModelContext(Settings settings) {
        this.gameInfo.setSettings(settings);
        this.gameInfo.setPlayers(new ArrayList<>(TexasHoldEmUtil.MAX_PLAYERS));
        this.playersByName = new HashMap<>(settings.getMaxPlayers());
        this.highBet = 0;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int getPlayersAllIn() {
        return playersAllIn;
    }

    public void setPlayersAllIn(int playersAllIn) {
        this.playersAllIn = playersAllIn;
    }

    public int getNumPlayers() {
        return gameInfo.getNumPlayers();
    }

    public boolean addPlayer(String playerName) {
        PlayerEntity player = new PlayerEntity();
        player.setName(playerName);
        player.setChips(gameInfo.getSettings().getPlayerChip());
        this.playersByName.put(playerName, player);
        this.allPlayers.add(player);
        return this.gameInfo.addPlayer(player);
    }

    public List<PlayerEntity> getAllPlayers() {
        return new ArrayList<>(allPlayers);
    }

    public long getHighBet() {
        return highBet;
    }

    public void setHighBet(long highBet) {
        this.highBet = highBet;
    }

    public int getDealer() {
        return gameInfo.getDealer();
    }

    public void setDealer(int dealer) {
        this.gameInfo.setDealer(dealer);
    }

    public int getRound() {
        return gameInfo.getRound();
    }

    public void setRound(int round) {
        this.gameInfo.setRound(round);
    }

    public void setGameState(GameState gameState) {
        this.gameInfo.setGameState(gameState);
    }

    public GameState getGameState() {
        return gameInfo.getGameState();
    }

    public String getPlayerTurnName() {
        String result = null;
        int turnPlayer = gameInfo.getPlayerTurn();
        if (turnPlayer >= 0) {
            result = gameInfo.getPlayer(turnPlayer).getName();
        }
        return result;
    }

    public int getPlayerTurn() {
        return gameInfo.getPlayerTurn();
    }

    public void setPlayerTurn(int playerTurn) {
        this.gameInfo.setPlayerTurn(playerTurn);
    }

    public Settings getSettings() {
        return gameInfo.getSettings();
    }

    public List<Card> getCommunityCards() {
        return gameInfo.getCommunityCards();
    }

    public void setCommunityCards(List<Card> communityCards) {
        gameInfo.setCommunityCards(communityCards);
    }

    public List<PlayerEntity> getPlayers() {
        return this.gameInfo.getPlayers();
    }

    public PlayerEntity getPlayer(int player) {
        return this.gameInfo.getPlayer(player);
    }

    public void setPlayers(List<PlayerEntity> newPlayers) {
        this.gameInfo.setPlayers(newPlayers);
        this.playersByName.clear();
        newPlayers.stream().forEach(p -> this.playersByName.put(p.getName(), p));
    }

    public PlayerEntity getPlayerByName(String playerName) {
        return playersByName.get(playerName);
    }

    public int getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(int activePlayers) {
        this.activePlayers = activePlayers;
    }

    public void lastResultCommand(PlayerEntity player, BetCommand resultCommand) {
        this.lastPlayerBet = player;
        this.lastBetCommand = resultCommand;
    }

    public BetCommand getLastBetCommand() {
        return lastBetCommand;
    }

    public void setLastBetCommand(BetCommand resultLastBetCommand) {
        this.lastBetCommand = resultLastBetCommand;
    }

    public PlayerEntity getLastPlayerBet() {
        return lastPlayerBet;
    }

    public void setLastPlayerBet(PlayerEntity lastPlayerBet) {
        this.lastPlayerBet = lastPlayerBet;
    }

    public int getBets() {
        return bets;
    }

    public void setBets(int bets) {
        this.bets = bets;
    }

    public int addCommunityCards(int numCards) {
        boolean added = true;
        int i = 0;
        while (i < numCards && added) {
            added = gameInfo.addCommunityCard(deck.obtainCard());
            if (added) {
                i++;
            }
        }
        return i;
    }

    public void clearCommunityCard() {
        gameInfo.clearCommunityCard();
    }

    @Override
    public String toString() {
        return "{class:'ModelContext', gameInfo:" + gameInfo + ", activePlayers:" + activePlayers + ", highBet:" + highBet + ", deck:" + deck + ", playersAllIn:" + playersAllIn + ", lastBetCommand:" + lastBetCommand + ", lastPlayerBet:" + lastPlayerBet + ", bets:" + bets + '}';
    }

    public void setScores(HashMap<String, Double> scores) {
        this.scores = scores;
    }

    public HashMap<String, Double> getScores() {
        return new HashMap<>(scores);
    }
}
