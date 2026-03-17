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

    public QuestionsResponse() {
    }

    public QuestionsResponse(UUID requestId, QuestionsResponseStatus status, List<Question> questions, String error) {
        this.requestId = requestId;
        this.status = status;
        this.questions = questions;
        this.error = error;
    }

    public static QuestionsResponse ok(UUID requestId, List<Question> questions) {
        return new QuestionsResponse(requestId, QuestionsResponseStatus.OK, questions, null);
    }

    public static QuestionsResponse error(UUID requestId, String error) {
        return new QuestionsResponse(requestId, QuestionsResponseStatus.ERROR, null, error);
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public QuestionsResponseStatus getStatus() {
        return status;
    }

    public void setStatus(QuestionsResponseStatus status) {
        this.status = status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}