package com.crafttalk.chat

enum class Events{
    MESSAGE_SEND,
    MESSAGE_SEND_ERROR,
    NO_INTERNET,
    HAS_INTERNET,
    MESSAGE_GET,
    MESSAGE_GET_ERROR,
    ACTION_SELECT,
    ACTION_SELECT_ERROR,

    USER_NOT_FAUND,
    USER_FAUND_WITHOUT_AUTH,

    MESSAGE_GET_SERVER,
    MESSAGE_GET_OPERATOR,
    USER_AUTHORIZAT,
    START_EVENT_SEND
}