package com.myway.platform.core;

public enum Catalog {

    commodifyImage("images/commodify/"), commodifyCover("images/commodify/cover/");

    private String code;

    Catalog(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
