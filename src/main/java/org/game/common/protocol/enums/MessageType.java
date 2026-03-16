package org.game.common.protocol.enums;
public enum MessageType {
    // Client -> Server
    LOGIN,
    REGISTER,
    ANSWER,
    CREATE_TEAM,
    JOIN_TEAM,
    START_GAME,
    QUIT,

    // Server -> Client
    LOGIN_OK,
    ERROR,
    MENU,
    QUESTION,
    TIME_UPDATE,
    ANSWER_RESULT,
    GAME_OVER,

    // GameServer -> LookupServer
    GET_QUESTIONS,
    GET_USERS,
    GET_SCORE,
    QUESTIONS_OK
}