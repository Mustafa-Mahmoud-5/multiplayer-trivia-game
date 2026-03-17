package org.game.common.jsonProtocol;

import java.util.UUID;

import org.game.common.jsonProtocol.enums.QuestionDifficulty;

import com.google.gson.Gson;

public class Test {
    public static void main(String[] args) {
        RequestQuestions requestQuestions = new RequestQuestions(UUID.randomUUID(),QuestionDifficulty.EASY, 4);
        Gson gson = new Gson();
        String json = gson.toJson(requestQuestions);
        System.out.println(json);
    }
}
