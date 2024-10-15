package com.mh55.httplibrary.parser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class MyParseException extends IOException {

    private final String errorCode;

    private final String requestMethod; //请求方法，Get/Post等
    private final HttpUrl httpUrl; //请求Url及查询参数
    private final Headers responseHeaders; //响应头
    private final String data;

    public MyParseException(@NonNull String code, String message, Response response, String data) {
        super(message);
        errorCode = code;

        Request request = response.request();
        requestMethod = request.method();
        httpUrl = request.url();
        responseHeaders = response.headers();
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return httpUrl.toString();
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    @Nullable
    @Override
    public String getLocalizedMessage() {
        return String.valueOf(errorCode);
    }

    @Override
    public String toString() {
        return getClass().getName() + ":" +
                "\n" + requestMethod + " " + httpUrl +
                "\n\nCode=" + errorCode + " message=" + getMessage() +
                "\n" + responseHeaders;
    }
}

