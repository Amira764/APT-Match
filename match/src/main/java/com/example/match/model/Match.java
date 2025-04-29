package com.example.match.model;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

@Getter
public class Match {
    private final String teamA;
    private final String teamB;
    private AtomicInteger scoreA;
    private AtomicInteger scoreB;
    private MatchStat status;
    public Match (String teamA, String teamB) {
        // #fixed bug: Added null checks for team names
        this.teamA = teamA != null ? teamA : "TeamA";
        this.teamB = teamB != null ? teamB : "TeamB";
        this.scoreA = new AtomicInteger(0);
        this.scoreB = new AtomicInteger(0);
        this.status=MatchStat.LIVE;
    }
    public void incrementScoreA(){
        this.scoreA.incrementAndGet();
    }
    public void incrementScoreB(){
        this.scoreB.incrementAndGet();
    }
    public String getMatchScore(){
        // #fixed bug: Improved score formatting
        return teamA+" "+scoreA+" - "+teamB+" "+scoreB;
    }
    public void endMatch(){
        this.status=MatchStat.ENDED;
    }
    public int getTeamScore(String team){
        if(team == null) return 0;
        if(teamA.equalsIgnoreCase(team)) return scoreA.intValue(); 
        else if(teamB.equalsIgnoreCase(team)) return scoreB.intValue();
        return 0;
    }
}