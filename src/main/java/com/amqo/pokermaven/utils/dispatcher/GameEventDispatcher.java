/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.utils.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
public class GameEventDispatcher<T> implements IGameEventDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger( GameEventDispatcher.class);
public static final String EXIT_EVENT_TYPE = "exit";
    private final Map<String, IGameEventProcessor<T>> processors;
    private final T target;
    private List<GameEvent> events = new ArrayList<>();
    private volatile boolean exit = false;
    private ExecutorService executors;

    public GameEventDispatcher(T target, Map<String, IGameEventProcessor<T>> p,
            ExecutorService executors) {
        this.target = target;
        this.processors = p;
        this.executors = executors;
    }

    @Override
    public synchronized void dispatch(GameEvent event) {
        events.add(event);
        this.notify();
    }

    @Override
    public synchronized void exit() {
        exit = true;
        this.notify();
    }

    private void doTask() throws InterruptedException {
        List<GameEvent> lastEvents;
        synchronized (this) {
            if (events.isEmpty()) {
                this.wait();
            }
            lastEvents = events;
            events = new ArrayList<>();
        }
        for (int i = 0; i < lastEvents.size() && !exit; i++) {
            GameEvent event = lastEvents.get(i);
            if (EXIT_EVENT_TYPE.equals(event.getType())) {
                exit = true;
            } else {
                //process(event);
            }
        }
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                doTask();
            } catch (InterruptedException ex) {
                //LOGGER.error("GameEventDispatcher<" + … ,target, ex);
            }
        }
        executors.shutdown();
    }
}
