package org.game.server.models;

import java.time.LocalDateTime;


public class ScoreRecord {
    private String category;
    private String difficulty;
    private int score;
    private int correct;
    private int incorrect;
    private String gameType; // "SINGLE" or "TEAM"
    private String timestamp;

    public ScoreRecord(String category, String difficulty, int score, int correct, int incorrect, String gameType) {
        this.category = category;
        this.difficulty = difficulty;
        this.score = score;
        this.correct = correct;
        this.incorrect = incorrect;
        this.gameType = gameType;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getScore() {
        return score;
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public String getGameType() {
        return gameType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + gameType + "] " + category + "/" + difficulty
                + " | Score: " + score
                + " | Correct: " + correct + " | Incorrect: " + incorrect
                + " | " + timestamp;
    }
}
