package com.myway.platform.handler.exception;


public class NotLoginException extends Exception {

    @Override
    public String getMessage() {
        return "用户无登录，视为违规操作。";
    }
}
