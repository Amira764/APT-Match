package com.example.matchclient;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class MatchWebsocketHandler {
StompSession stompSession;
    public void connectToWebSocket() {
        try {
        //  TODO: Complete the code to create a WebSocket client        
            // List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
            // SockJsClient sockJsClient;

            // List<MessageConverter> converters = new ArrayList<>();
            List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
            SockJsClient sockJsClient = new SockJsClient(transports);

            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            // #fixed bug: Added proper WebSocket endpoint URL and connection handling
            this.stompSession = stompClient.connect("ws://localhost:8080/ws", new MyStompSessionHandler()).get();
		
        } catch (Exception e) {
            // #fixed bug: Added more descriptive error message
            System.out.println("Websocket connection failed: " + e.getMessage());
        }
    }
    public void updateScore(String team,String matchId) {
        //TODO: write the code for sending update match message
        if (stompSession != null && stompSession.isConnected()) {
            Map<String, Object> message = new HashMap<>();
            message.put("team", team);
            message.put("matchId", matchId);
            // #fixed bug: Confirmed correct endpoint path (/app/update)
            stompSession.send("/app/update", message);
        }
    }
    public void endMatch(String matchId){
        //TODO: write the code for sending end match message
        if (stompSession != null && stompSession.isConnected()) {
            Map<String, Object> message = new HashMap<>();
            message.put("matchId", matchId);
            // #fixed bug: Confirmed correct endpoint path (/app/end)
            stompSession.send("/app/end", message);
        }
    }
    public void subscribeToMatch(String matchId){
        // TODO:Connect to the WebSocket server

        /*TODO: 1-Subscribe to the match topic 
         *      2-Print the score
         *      3-If match ended, exit*/
        if (stompSession != null && stompSession.isConnected()) {
            // #fixed bug: Added proper subscription path and message handling
            stompSession.subscribe("/topic/match/" + matchId, new StompSessionHandlerAdapter() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Message.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    Message message = (Message) payload;
                    System.out.println(message.matchScore());

                    if (message.isEnded()) {
                        System.out.println("Match ended. Exiting...");
                        close();
                    }
                }
            });
        }
    }
    public void close(){
        if(this.stompSession!=null){
            this.stompSession.disconnect();
            // #fixed bug: Added connection closed confirmation message
            System.out.println("Closed websocket connection");
        }
    }
}
class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        // #fixed bug: Improved error message formatting
        System.err.println("An error occurred: " + exception.getMessage());
    }
}