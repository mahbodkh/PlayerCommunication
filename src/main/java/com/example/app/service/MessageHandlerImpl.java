package com.example.app.service;

import com.example.app.model.Player;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * An in-memory implementation of MessageHandler using blocking queues.
 * Each registered player is associated with its own inbox.
 */
public class MessageHandlerImpl implements MessageHandler {

    private final ConcurrentHashMap<Player, BlockingQueue<String>> inboxes = new ConcurrentHashMap<>();

    @Override
    public void register(Player player) {
        inboxes.putIfAbsent(player, new LinkedBlockingQueue<>());
    }

    @Override
    public void send(Player player, String message) throws IOException {
        BlockingQueue<String> queue = inboxes.get(player);
        if (queue == null) {
            throw new IOException("Player not registered: " + player.name());
        }
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while sending message", e);
        }
    }


    @Override
    public String receive(Player player) throws IOException {
        BlockingQueue<String> queue = inboxes.get(player);
        if (queue == null) {
            throw new IOException("Player not registered: " + player.name());
        }
        try {
            return queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while receiving message", e);
        }
    }

    @Override
    public void close() throws IOException {
        inboxes.clear();
    }
}
