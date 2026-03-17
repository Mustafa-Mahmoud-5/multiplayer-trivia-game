package org.game.common.jsonProtocol;

import java.util.UUID;

import org.game.common.jsonProtocol.enums.QuestionDifficulty;
import org.game.common.jsonProtocol.enums.QuestionsResponseStatus;

import com.google.gson.Gson;

public class Test {
    public static void main(String[] args) {
        RequestQuestions requestQuestions = new RequestQuestions(UUID.randomUUID(),QuestionDifficulty.EASY, 4);
        Gson gson = new Gson();
        String json = gson.toJson(requestQuestions);
        System.out.println(json);


        String respone = "{\"requestId\":\"123e4567-e89b-12d3-a456-426614174000\",\"status\":\"OK\",\"questions\":[{\"id\":1,\"text\":\"What is the capital of France?\",\"choices\":[\"Paris\",\"London\",\"Berlin\",\"Madrid\"],\"correctAnswer\":0}]}";


        QuestionsResponse questionsResponse = gson.fromJson(respone, QuestionsResponse.class);
        System.out.println("Request ID: " + questionsResponse.getRequestId());
        System.out.println("Status: " + questionsResponse.getStatus());
        if (questionsResponse.getStatus() == QuestionsResponseStatus.OK) {
            System.out.println("Questions:");
            for (var question : questionsResponse.getQuestions()) {
                System.out.println(" - " + question.getText());
                System.out.println("   Options: " + question.getChoices());
                System.out.println("   Correct Answer Index: " + question.getCorrectAnswer());
            }
        }

    }
}
