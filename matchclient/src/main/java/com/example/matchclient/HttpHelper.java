package com.example.matchclient;

import java.util.Scanner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpHelper {
    private static final RestTemplate restTemplate= new RestTemplate();
    // #fixed bug: Corrected endpoint path case sensitivity
    private static final String baseUrl= "http://localhost:8080";

    public static String createMatch(Scanner scanner) {
        System.out.print("Enter Team A Name: ");
        String teamA = scanner.next();
        System.out.print("Enter Team B Name: ");
        // #fixed bug: Added buffer clearing
        scanner.nextLine();
        String teamB = scanner.nextLine();
        String matchId = null;
        String url = baseUrl + "/create";
        String[] requestData={teamA,teamB};
        try {
            matchId = restTemplate.postForObject(url, requestData, String.class);
            System.out.println("Match created successfully with Id: " + matchId);
            
        } catch (Exception e) {
            // #fixed bug: Improved error handling
            System.out.println("Failed to create match with error: " + e.getMessage());
            System.out.println("Exiting..");
            System.exit(0);
        }
        return matchId;
    }

    public static int countTeamGoals(String team) {
        // #fixed bug: Added proper endpoint path
        String url = baseUrl + "/total/" + team;
        ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
        return response.getBody();
    }

    public static int countActiveMatch() {
        // #fixed bug: Corrected endpoint path from '/active_count' to '/activeCount'
        String url = baseUrl + "/activeCount";
        ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
        return response.getBody();
    } 
    public static String getMatchScore(String matchId) {
        String url = baseUrl + "/score/"+matchId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    } 
}