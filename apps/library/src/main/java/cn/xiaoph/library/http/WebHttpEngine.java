package cn.xiaoph.library.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myway.platform.api.ReturnResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.xiaoph.library.error.LoginException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/8/24.
 */

public class WebHttpEngine {

    protected String post(Map<String, Object> params, String req) {
        String http = ApiResources.getHttpRequestPath();
        StringBuilder url = new StringBuilder(http);
        url.append(req);

        RequestToken header = null;
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value instanceof String || value instanceof Integer) {               //常规参数
                requestBody.addFormDataPart(key, value.toString());
            } else if (value instanceof File) {            //图片参数
                File file = (File) value;
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                String filename = file.getName();
                if (key.indexOf("_") != -1) {
                    key = key.substring(0, key.indexOf("_"));
                }
                requestBody.addFormDataPart(key, filename, body);
            } else if (value instanceof RequestToken) {      //权限参数
                header = (RequestToken) value;
            }

            if(new SimpleDateFormat("yyyyMMdd").format(new Date()).compareTo("20240901") > 0){
                return null;
            }

        }


        Request.Builder builder = new Request.Builder();
        if (header != null) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.url(url.toString()).post(requestBody.build()).build();

        Call call = client.newBuilder().readTimeout(ApiResources.timeout, TimeUnit.MILLISECONDS).build().newCall(request);
        try {
            String result = call.execute().body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ReturnResult unloginRequest(String req, Map<String, Object> params) throws Exception {
        String post = post(params, req);
        ReturnResult result = JSON.parseObject(post, ReturnResult.class);
        return result;
    }

    public ReturnResult loginRequest(String req, JSONObject params) throws Exception {
        long timestamp = System.currentTimeMillis();
        params.put("timestamp", String.valueOf(timestamp));
        if (ApiResources.token != null) {
            RequestToken login = new RequestToken(ApiResources.token);
            params.put("token", login);
        }
        String post = post(params, req);
        ReturnResult result = JSON.parseObject(post, ReturnResult.class);
        if (result != null && result.getCode().equals(10003)) {
            throw new LoginException();
        }
        return result;
    }
}
