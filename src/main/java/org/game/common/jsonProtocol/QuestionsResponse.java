/*
example of use:

Gson gson = new Gson();
QuestionsResponse response =
        gson.fromJson(jsonString, QuestionsResponse.class);
*/

package org.game.common.jsonProtocol;

import java.util.UUID;
import java.util.List;

import org.game.common.jsonProtocol.enums.QuestionsResponseStatus;
import org.game.common.models.Question;

public class QuestionsResponse {

    private UUID requestId;
    private QuestionsResponseStatus status;
    private List<Question> questions;
    private String error;

    public UUID getRequestId() {
        return requestId;
    }

    public QuestionsResponseStatus getStatus() {
        return status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getError() {
        return error;
    }
}