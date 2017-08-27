package com.example.michael.archerygame;

public class Player {

    private long playerId;
    private String name = "";
    private int score = 0;
    private boolean isPlaying = true;

    public Player(long playerId, String name, int score, boolean isPlaying) {
        this.playerId = playerId;
        if (name != null) this.name = name;
        this.score = score;
        this.isPlaying = isPlaying;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getName() {
        if (name != null) return name;
        return "name not set.";
    }

    public void setName(String name) {
        if (name != null) this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score >= 0) this.score = score;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
