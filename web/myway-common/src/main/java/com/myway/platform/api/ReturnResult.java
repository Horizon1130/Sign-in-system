package com.myway.platform.api;

import com.alibaba.fastjson.JSONObject;

/**
 * 接口返回Model
 * <p>
 * code代码提示：
 * 10000：通过
 * 10001：错误
 * 10002：无权限
 * 10003：无登录
 */
public class ReturnResult extends JSONObject {

    public ReturnResult() {
        setData("");
        commit();
    }

    public void setMessage(String message) {
        put("msg", message);
    }

    public void setErrorMsg(String errorMsg) {
        put("errorMsg", errorMsg);
    }

    public void setCode(Integer code) {
        put("code", code);
    }

    public void commit() {
        setCode(10000);
    }

    public void set(String key, Object value) {
        put(key, value);
    }

    public void setData(Object value) {
        put("data", value);
    }

    public void noAuthority() {
        setCode(10002);
    }

    public void notLogin() {
        setCode(10003);
    }

    public void rollback() {
        setCode(10001);
    }

    public void setListView(Object list, long total) {
        set("list", list);
        set("total", total);
        set("code", 10000);
        set("errorMsg", "");
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
