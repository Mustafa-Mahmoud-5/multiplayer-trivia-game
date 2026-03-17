/*
example of use:

RequestQuestions request =
    new RequestQuestions(UUID.randomUUID(), QuestionCategorty.SCIENCE, 5);

Gson gson = new Gson();
String json = gson.toJson(request);

// Send 'json' to the Lookup Server
*/

package org.game.common.jsonProtocol;

import java.util.UUID;
import org.game.common.jsonProtocol.enums.QuestionCategorty;
import org.game.common.jsonProtocol.enums.QuestionDifficulty;


public class RequestQuestions {
    private UUID requestId;
    private QuestionCategorty category;
    private QuestionDifficulty difficulty;
    private int count;

    public RequestQuestions() {
    }

    public RequestQuestions(UUID ID, int numberOfQuestions){
        requestId = ID;
        count = numberOfQuestions;
    }

    public RequestQuestions(UUID requestID, QuestionCategorty categorty, int numberOfQuestions){
        requestId = requestID;
        this.category = categorty;
        count = numberOfQuestions;
    }

    public RequestQuestions(UUID requestID, QuestionDifficulty difficulty, int numberOfQuestions){
        requestId = requestID;
        this.difficulty = difficulty;
        count = numberOfQuestions;
    }

    public RequestQuestions(UUID requestID, QuestionCategorty categorty, QuestionDifficulty difficulty, int numberOfQuestions){
        requestId = requestID;
        this.category = categorty;
        this.difficulty = difficulty;
        count = numberOfQuestions;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public QuestionCategorty getCategory() {
        return category;
    }

    public void setCategory(QuestionCategorty category) {
        this.category = category;
    }

    public QuestionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuestionDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}