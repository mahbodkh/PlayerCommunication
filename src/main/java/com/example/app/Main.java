package com.example.app;

import com.example.app.model.Player;
import com.example.app.service.MessageHandler;
import com.example.app.service.MessageHandlerImpl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    private static final String TERMINATE = "TERMINATE";

    public static void main(String[] args) {
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        final MessageHandler handler = new MessageHandlerImpl();
        final Player initiator = new Player("Player1");
        final Player responder = new Player("Player2");


        handler.register(initiator);
        handler.register(responder);

        // Start communication loop
        executor.submit(() -> initiateCommunication(handler, initiator, responder));
        executor.submit(() -> respondToMessages(handler, responder, initiator));


        executor.shutdown();
    }

    /**
     * Initiator sends messages to the responder and waits for a response after each send.
     *
     * @param handler   The messaging handler.
     * @param initiator The player initiating the conversation.
     * @param responder The target player.
     */
    private static void initiateCommunication(MessageHandler handler, Player initiator, Player responder) {
        try {
            for (int i = 1; i <= 10; i++) {
                String message = "Message " + i;
                System.out.println(initiator.name() + " sending: " + message);
                handler.send(responder, message);

                String response = handler.receive(initiator);
                System.out.println(initiator.name() + " received response: " + response);
            }
            // After sending 10 messages, send a termination signal.
            handler.send(responder, TERMINATE);
        } catch (IOException e) {
            System.err.println("Initiator encountered an error: " + e.getMessage());
        }
    }

    /**
     * Responder continuously processes incoming messages, appending a counter to each,
     * and sends the response back to the initiator. It stops upon receiving the termination signal.
     *
     * @param handler   The messaging handler.
     * @param responder The responder player.
     * @param initiator The player to send responses to.
     */
    private static void respondToMessages(MessageHandler handler, Player responder, Player initiator) {
        int counter = 0;
        try {
            while (true) {
                String received = handler.receive(responder);
                if (TERMINATE.equals(received)) {
                    System.out.println(responder.name() + " received termination signal. Shutting down.");
                    break;
                }
                System.out.println(responder.name() + " received: " + received);
                counter++;
                String response = received + " " + counter;
                System.out.println(responder.name() + " sending response: " + response);
                handler.send(initiator, response);
            }
        } catch (IOException e) {
            System.err.println("Responder encountered an error: " + e.getMessage());
        }
    }
}
