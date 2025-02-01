package com.example.app.service;

import com.example.app.model.Player;

import java.io.IOException;

public interface MessageHandler {
    /**
     * Registers a player in the messaging system by creating an inbox for them.
     *
     * @param player The player to register.
     */
    void register(Player player);

    /**
     * Sends a message over the channel.
     *
     * @param message The message to send.
     * @param player  The message should be sent to the specified player.
     * @throws IOException If an I/O error occurs.
     */
    void send(Player player, String message) throws IOException;

    /**
     * Receives a message from the channel.
     * This method blocks until a message is available.
     *
     * @param player The message should be sent to the specified player.
     * @return The received message.
     * @throws IOException If an I/O error occurs.
     */
    String receive(Player player) throws IOException;

    /**
     * Closes the channel and releases any resources.
     *
     * @throws IOException If an I/O error occurs.
     */
    void close() throws IOException;
}
