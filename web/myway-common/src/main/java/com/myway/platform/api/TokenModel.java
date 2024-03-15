package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;

public class TokenModel {

    private String userKey;
    private String token;
    private JSONObject custom;

    public TokenModel() {
        super();
    }

    public TokenModel(String key, String token) {
        this.userKey = key;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return userKey;
    }

    public void setKey(String userKey) {
        this.userKey = userKey;
    }

    public JSONObject getCustom() {
        if (custom == null) {
            custom = new JSONObject();
        }
        return custom;
    }

    public void setCustom(JSONObject custom) {
        this.custom = custom;
    }
}
