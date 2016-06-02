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
import org.junit.runner.notification.RunListener.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alberto
 */
@ThreadSafe
public class GameEventDispatcher<E extends Enum, T> implements IGameEventDispatcher<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameEventDispatcher.class);
    private final Map<E, IGameEventProcessor<E, T>> processors;
    private final T target;
    private final E exitEven;
    private List<GameEvent> events = new ArrayList<>();
    private volatile boolean exit = false;
    private ExecutorService executors;

    public GameEventDispatcher(T target, Map<E, IGameEventProcessor<E, T>> processors, ExecutorService executors, E exitEven) {
        this.target = target;
        this.processors = processors;
        this.executors = executors;
        this.exitEven = exitEven;
    }

    @Override
    public synchronized void dispatch(GameEvent<E> event) {
        events.add(event);
        this.notify();
    }

    private void process(GameEvent<E> event) {
        IGameEventProcessor<E, T> processor = processors.get(event.getType());
        if (processor != null) {
            executors.execute(() -> processor.process(target, event));
        } else {
            System.out.println("warn!!!! no hay procesador para el evento: " + event.getType());
        }
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
            if (exitEven == event.getType()) {
                exit = true;
            } else {
                process(event);
            }
        }
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                doTask();
            } catch (Exception ex) {
                LOGGER.error("GameEventDispatcher<" + target.getClass() + ">.run(): " + target, ex);
            }
        }
        executors.shutdown();
    }
}
