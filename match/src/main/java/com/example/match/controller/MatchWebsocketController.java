package com.example.match.controller;

/*TODO: Implement the MatchWebsocketController class that handles the websocket connections
* and messages as follows:
    * 1. handle updating the score of a specific match and send the updated score to subscribers
    * 2. Handle end match messages and send notification with match end to match subscribers
    Subscribers expect you to send a Message object in both cases
Your endpoints/topics should be named appropriately
    */

import com.example.match.model.Message;
import com.example.match.service.MatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class MatchWebsocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MatchService matchService;

    @MessageMapping("/update")
    public void updateScore(@Payload Map<String, String> payload) {
        String matchId = payload.get("matchId");
        String team = payload.get("team");

        matchService.incrementScore(matchId, team);
        String matchScore = matchService.getMatchDetails(matchId);

        Message response = new Message(matchScore, false); // Not ended
        messagingTemplate.convertAndSend("/topic/match/" + matchId, response);
    }

    @MessageMapping("/end")
    public void endMatch(@Payload Map<String, String> payload) {
        String matchId = payload.get("matchId");

        matchService.endMatch(matchId);

        Message response = new Message("Match Ended", true); // Match ended
        messagingTemplate.convertAndSend("/topic/match/" + matchId, response);
    }
}
