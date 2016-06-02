/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.utils.timer;

import java.util.concurrent.ExecutorService;
import org.junit.runner.notification.RunListener.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
@ThreadSafe
public class GameTimer implements IGameTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameTimer.class);

    private long time;
    private TimeoutNotifier notifier;
    private boolean reset = false;
    private volatile boolean exit = false;
    private final ExecutorService executors;
    private Long timeoutId;

    public GameTimer(ExecutorService executors) {
        this.executors = executors;
    }

    @Override
    public void setNotifier(TimeoutNotifier notifier) {
        this.notifier = notifier;
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
    public synchronized void changeTimeoutId(Long timeoutId) {
        this.timeoutId = timeoutId;
        this.reset = true;
        notify();
    }

    @Override
    public synchronized void exit() {
        this.exit = true;
        this.reset = false;
        this.timeoutId = null;
        notify();
    }

    @Override
    public void run() {
        LOGGER.debug("run");
        while (!exit) {
            try {
                doTask();
            } catch (Exception ex) {
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
                final Long timeoutToNotify = timeoutId;
                executors.execute(() -> notifier.notify(timeoutToNotify));
                timeoutId = null;
            }
        }
    }
}
