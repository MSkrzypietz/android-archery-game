package com.example.michael.archerygame;

public class Player {

    private String name = "";
    private int score = 0;

    public Player(String name, int score) {
        if (name != null) this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
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

}
