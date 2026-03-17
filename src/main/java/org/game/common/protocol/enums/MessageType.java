package org.game.common.protocol.enums;
public enum MessageType {
    // Client -> Server (Auth)
    LOGIN,
    REGISTER,

    // Client -> Server (Game Flow)
    ANSWER,
    CREATE_TEAM,
    JOIN_TEAM,
    START_GAME,
    QUIT,

    // Server -> Client (Auth)
    LOGIN_OK,
    REGISTER_OK,

    // Server -> Client (Errors & info)
    ERROR,
    INFO,

    // Server -> Client (Menu & Navigation)
    MENU,
    MULTIPLAYER_MENU,
    ADMIN_MENU,

    // Server -> Client (Game Config prompts)
    ENTER_TEAM_NAME,
    SELECT_CATEGORY,
    SELECT_DIFFICULTY,
    ENTER_QUESTION_COUNT,
    ENTER_OPPONENT_TEAM,
    ENTER_TEAM_TO_JOIN,

    // Server -> Client (Game)
    QUESTION,
    TIME_UPDATE,
    ANSWER_RESULT,
    SCORE_UPDATE,
    GAME_OVER,
    SCORES_HISTORY,

    // Server -> Client (Team status)
    TEAM_CREATED,
    TEAM_JOINED,
    WAITING,
    TEAM_READY,

    // Server -> Client (Admin)
    ADMIN_STATS,

    // GameServer -> LookupServer
    GET_QUESTIONS,
    GET_USERS,
    GET_SCORE,
    QUESTIONS_OK,

    // Disconnection
    BYE
}