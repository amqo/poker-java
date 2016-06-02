/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game.controller;

import com.amqo.pokermaven.core.Deck;
import com.amqo.pokermaven.game.BetCommand;
import com.amqo.pokermaven.game.IStateTrigger;
import com.amqo.pokermaven.game.Settings;
import com.amqo.pokermaven.game.StateDecoratorBuilder;
import com.amqo.pokermaven.game.StateMachine;
import com.amqo.pokermaven.game.StateMachineInstance;
import com.amqo.pokermaven.game.engine.model.ModelContext;
import com.amqo.pokermaven.game.engine.model.ModelUtil;
import com.amqo.pokermaven.game.engine.states.BetRoundTrigger;
import com.amqo.pokermaven.game.engine.states.CheckTrigger;
import com.amqo.pokermaven.game.engine.states.EndGameTrigger;
import com.amqo.pokermaven.game.engine.states.EndHandTrigger;
import com.amqo.pokermaven.game.engine.states.InitHandTrigger;
import com.amqo.pokermaven.game.engine.states.PokerStates;
import com.amqo.pokermaven.game.engine.states.ShowDownTrigger;
import com.amqo.pokermaven.game.engine.states.WinnerTrigger;
import com.amqo.pokermaven.utils.TexasHoldEmUtil;
import com.amqo.pokermaven.utils.dispatcher.GameEvent;
import com.amqo.pokermaven.utils.dispatcher.IGameEventDispatcher;
import com.amqo.pokermaven.utils.timer.IGameTimer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
public class StateMachineConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineConnector.class);

    private final StateMachine<PokerStates, ModelContext> texasStateMachine = buildStateMachine();
    private final Map<String, IGameEventDispatcher> playersDispatcher;
    final IGameTimer timer;
    ModelContext model;
    IGameEventDispatcher<ConnectorGameEventType> system;
    StateMachineInstance<PokerStates, ModelContext> instance;
    private long timeoutId = 0;
    private Map<String, Double> scores;

    public StateMachineConnector(IGameTimer timer, Map<String, IGameEventDispatcher> playersDispatcher) {
        this.playersDispatcher = playersDispatcher;
        this.timer = timer;
    }

    public void setSystem(IGameEventDispatcher<ConnectorGameEventType> system) {
        this.system = system;
    }

    public void createGame(Settings settings) {
        if (model == null) {
            LOGGER.debug("createGame: {}", settings);
            model = new ModelContext(settings);
            model.setDealer(-1);
        }
    }

    public void addPlayer(String playerName) {
        if (model != null) {
            LOGGER.debug("addPlayer: \"{}\"", playerName);
            model.addPlayer(playerName);
        }
    }

    public void startGame() {
        LOGGER.debug("startGame");
        if (instance == null && model != null) {
            model.setDeck(new Deck());
            instance = texasStateMachine.startInstance(model);
            model.setDealer(0);
            execute();
        }
    }

    public void betCommand(String playerName, BetCommand command) {
        LOGGER.debug("betCommand: {} -> {}", playerName, command);
        if (instance != null && playerName.equals(model.getPlayerTurnName())) {
            BetCommand betCommand = command;
            if (betCommand == null) {
                betCommand = new BetCommand(TexasHoldEmUtil.BetCommandType.ERROR);
            }
            model.getPlayerByName(playerName).setBetCommand(betCommand);
            execute();
        }
    }

    public void timeOutCommand(Long timeoutId) {
        LOGGER.debug("timeOutCommand: id: {}", timeoutId);
        if (instance != null && timeoutId == this.timeoutId) {
            LOGGER.debug("timeOutCommand: player: {}", model.getPlayerTurnName());
            model.getPlayerByName(model.getPlayerTurnName()).setBetCommand(new BetCommand(TexasHoldEmUtil.BetCommandType.TIMEOUT));
            execute();
        }
    }

    private void execute() {
        if (instance.execute().isFinish()) {
            instance = null;
        }
    }

    private void notifyInitHand() {
        notifyEvent(PokerEventType.INIT_HAND);
    }

    private void notifyBetCommand() {
        String playerTurn = model.getLastPlayerBet().getName();
        BetCommand lbc = model.getLastBetCommand();
        LOGGER.debug("notifyBetCommand -> {}: {}", playerTurn, lbc);
        playersDispatcher.entrySet().stream().forEach(entry
                -> entry.getValue().dispatch(
                        new GameEvent<>(PokerEventType.BET_COMMAND, model.getLastPlayerBet().getName(), new BetCommand(lbc.getType(), lbc.getChips()))));
    }

    private void notifyCheck() {
        LOGGER.debug("notifyCheck: {}", PokerEventType.CHECK, model.getCommunityCards());
        playersDispatcher.entrySet().stream().forEach(entry
                -> entry.getValue().dispatch(
                        new GameEvent<>(PokerEventType.CHECK, GameController.SYSTEM_CONTROLLER, model.getCommunityCards())));
    }

    private void notifyPlayerTurn() {
        String playerTurn = model.getPlayerTurnName();
        if (playerTurn != null) {
            LOGGER.debug("notifyPlayerTurn -> {}", playerTurn);
            playersDispatcher.get(playerTurn).dispatch(
                    new GameEvent<>(PokerEventType.GET_COMMAND, GameController.SYSTEM_CONTROLLER, PlayerAdapter.toTableState(model, playerTurn)));
        }
        timer.changeTimeoutId(++timeoutId);
    }

    private void notifyEndHand() {
        notifyEvent(PokerEventType.END_HAND);
    }

    private void notifyEvent(PokerEventType type) {
        LOGGER.debug("notifyEvent: {} -> {}", type, model);
        playersDispatcher.entrySet().stream().forEach(entry
                -> entry.getValue().dispatch(
                        new GameEvent<>(type, GameController.SYSTEM_CONTROLLER, PlayerAdapter.toTableState(model, entry.getKey()))));
    }

    private void notifyEndGame() {
        LOGGER.debug("notifyEvent: {} -> {}", PokerEventType.END_GAME, model);
        scores = model.getScores();
        playersDispatcher.entrySet().stream().forEach(entry
                -> entry.getValue().dispatch(
                        new GameEvent<>(PokerEventType.END_GAME, GameController.SYSTEM_CONTROLLER, scores)));
        system.dispatch(new GameEvent<>(ConnectorGameEventType.EXIT, GameController.SYSTEM_CONTROLLER));
        notifyEvent(PokerEventType.EXIT);
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    private StateMachine<PokerStates, ModelContext> buildStateMachine() {
        StateMachine<PokerStates, ModelContext> sm = new StateMachine<>();
        final IStateTrigger<ModelContext> initHandTrigger = StateDecoratorBuilder.after(new InitHandTrigger(), () -> notifyInitHand());
        final IStateTrigger<ModelContext> betRoundTrigger = StateDecoratorBuilder
                .create(new BetRoundTrigger())
                .before(() -> notifyPlayerTurn())
                .after(() -> notifyBetCommand())
                .build();
        final IStateTrigger<ModelContext> checkTrigger = StateDecoratorBuilder.after(new CheckTrigger(), () -> notifyCheck());
        final IStateTrigger<ModelContext> showDownTrigger = new ShowDownTrigger();
        final IStateTrigger<ModelContext> winnerTrigger = new WinnerTrigger();
        final IStateTrigger<ModelContext> endHandTrigger = StateDecoratorBuilder.before(new EndHandTrigger(), () -> notifyEndHand());
        final IStateTrigger<ModelContext> endGameTrigger = StateDecoratorBuilder.after(new EndGameTrigger(), () -> notifyEndGame());

        sm.setTrigger(PokerStates.BET_ROUND, betRoundTrigger);
        sm.setTrigger(PokerStates.CHECK, checkTrigger);
        sm.setTrigger(PokerStates.END_GAME, endGameTrigger);
        sm.setTrigger(PokerStates.END_HAND, endHandTrigger);
        sm.setTrigger(PokerStates.INIT_HAND, initHandTrigger);
        sm.setTrigger(PokerStates.SHOWDOWN, showDownTrigger);
        sm.setTrigger(PokerStates.WINNER, winnerTrigger);

        sm.setInitState(PokerStates.INIT_HAND);

        // init hand state transitions
        sm.setDefaultTransition(PokerStates.INIT_HAND, PokerStates.BET_ROUND);

        // bet round state transitions
        sm.addTransition(PokerStates.BET_ROUND, PokerStates.BET_ROUND, m -> m.getPlayerTurn() != ModelUtil.NO_PLAYER_TURN);
        sm.addTransition(PokerStates.BET_ROUND, PokerStates.WINNER, m -> m.getPlayersAllIn() + m.getActivePlayers() == 1);
        sm.setDefaultTransition(PokerStates.BET_ROUND, PokerStates.CHECK);

        // check state transitions
        sm.addTransition(PokerStates.CHECK, PokerStates.SHOWDOWN, m -> m.getGameState() == TexasHoldEmUtil.GameState.SHOWDOWN);
        sm.addTransition(PokerStates.CHECK, PokerStates.BET_ROUND, m -> m.getPlayerTurn() != ModelUtil.NO_PLAYER_TURN);
        sm.setDefaultTransition(PokerStates.CHECK, PokerStates.CHECK);

        // winner state transitions
        sm.setDefaultTransition(PokerStates.WINNER, PokerStates.END_HAND);

        // showdown state transitions
        sm.setDefaultTransition(PokerStates.SHOWDOWN, PokerStates.END_HAND);

        // end hand state transitions
        sm.addTransition(PokerStates.END_HAND, PokerStates.INIT_HAND, m -> m.getNumPlayers() > 1 && m.getRound() < m.getSettings().getMaxRounds());
        sm.setDefaultTransition(PokerStates.END_HAND, PokerStates.END_GAME);
        return sm;
    }
}
