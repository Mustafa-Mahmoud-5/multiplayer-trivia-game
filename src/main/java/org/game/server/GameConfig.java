package org.game.server;

public class GameConfig {
    String category;
    String difficulty;
    int questionsCount;
    String opponentName;
    public GameConfig(String category, String difficulty, int questionsCount, String opponentName) {
        this.category = category;
        this.difficulty = difficulty;
        this.questionsCount = questionsCount;
        this.opponentName = opponentName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(int questionsCount) {
        this.questionsCount = questionsCount;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }
}
