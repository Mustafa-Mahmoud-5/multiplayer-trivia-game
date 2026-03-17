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

    RequestQuestions(UUID ID, int numberOfQuestions){
        requestId = ID;
        count = numberOfQuestions;
    }

    RequestQuestions(UUID requestID, QuestionCategorty categorty, int numberOfQuestions){
        requestId = requestID;
        this.category = categorty;
        count = numberOfQuestions;
    }

    RequestQuestions(UUID requestID, QuestionDifficulty difficulty, int numberOfQuestions){
        requestId = requestID;
        this.difficulty = difficulty;
        count = numberOfQuestions;
    }

    RequestQuestions(UUID requestID, QuestionCategorty categorty, QuestionDifficulty difficulty, int numberOfQuestions){
        requestId = requestID;
        this.category = categorty;
        this.difficulty = difficulty;
        count = numberOfQuestions;
    }

}

// to use it from 