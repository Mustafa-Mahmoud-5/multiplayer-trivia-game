package org.game.common.models;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String id;
    private String category;
    private String difficulty;
    private String text;
    private List<String> choices;
    private String correctAnswer;

    public Question(String id, String category, String difficulty, String text, List<String> choices, String correctAnswer) {
        this.id = id;
        this.category = category;
        this.difficulty = difficulty;
        this.text = text;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public static class Team {
        private String name;
        ArrayList<User> players;
        int score;
    }
}
