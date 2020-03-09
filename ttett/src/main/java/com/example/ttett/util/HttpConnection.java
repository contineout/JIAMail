package com.example.ttett.util;

import com.example.ttett.Entity.User;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnection {
    public static void sendOkHttpRequest(String address, User user, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        //使用Ggon将user对象转为json
        String params = new Gson().toJson(user);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, params);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
