package com.myway.platform.core;

public enum DefaultQueue {

    IMAGES("images");

    private String code;

    DefaultQueue(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
