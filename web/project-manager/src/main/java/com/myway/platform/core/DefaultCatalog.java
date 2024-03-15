package com.myway.platform.core;

public enum DefaultCatalog {

    userFace("images/face/"), images("images/"), videos("videos/");

    private String code;

    DefaultCatalog(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
