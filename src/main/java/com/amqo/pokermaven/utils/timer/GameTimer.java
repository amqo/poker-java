/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.utils.timer;

import com.amqo.pokermaven.game.engine.model.PlayerEntity;
import com.amqo.pokermaven.utils.dispatcher.GameEvent;
import com.amqo.pokermaven.utils.dispatcher.IGameEventDispatcher;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
public class GameTimer implements IGameTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameTimer.class);
    public static final String TIMEOUT_EVENT_TYPE = "timeOutCommand";
    private final String source;
    private long time;
    private IGameEventDispatcher dispatcher;
    private boolean reset = false;
    private volatile boolean exit = false;
    private final ExecutorService executors;
    private Long timeoutId;
    private PlayerEntity player;

    public GameTimer(String source, ExecutorService executors) {
        this.source = source;
        this.executors = executors;
    }

    @Override
    public synchronized IGameEventDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public synchronized void setDispatcher(IGameEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public synchronized void resetTimer(Long timeoutId) {
        this.timeoutId = timeoutId;
        this.reset = true;
        notify();
    }

    @Override
    public synchronized void exit() {
        this.exit = true;
        this.reset = false;
        this.player = null;
        notify();
    }

    @Override
    public void run() {
        LOGGER.debug("run");
        while (!exit) {
            try {
                doTask();
            } catch (InterruptedException ex) {
                LOGGER.error("Timer interrupted", ex);
            }
        }
        LOGGER.debug("finish");
    }

    private synchronized void doTask() throws InterruptedException {
        if (timeoutId == null) {
            wait();
        }
        if (timeoutId != null) {
            reset = false;
            wait(time);
            if (!reset && timeoutId != null) {
                GameEvent event = new GameEvent(TIMEOUT_EVENT_TYPE, source, timeoutId);
                executors.execute(() -> dispatcher.dispatch(event));
                //player = null;
            }
        }
    }
}
