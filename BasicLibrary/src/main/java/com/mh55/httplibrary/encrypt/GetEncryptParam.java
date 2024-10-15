package com.mh55.httplibrary.encrypt;

import android.graphics.Point;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import rxhttp.wrapper.annotation.Param;
import rxhttp.wrapper.entity.KeyValuePair;
import rxhttp.wrapper.param.Method;
import rxhttp.wrapper.param.NoBodyParam;

@Param(methodName = "getEncrypt")
public class GetEncryptParam extends NoBodyParam {

    private final String url;

    public GetEncryptParam(String url) {
        super(url, Method.GET);
        this.url = url;
    }

    public <T extends Point, R extends CharSequence> GetEncryptParam test(List<R> a, Map<T, R> map, T[]... b) throws IOException, IllegalArgumentException {
        return this;
    }

    @Override
    public HttpUrl getHttpUrl() {
        StringBuilder paramsBuilder = new StringBuilder(); //存储加密后的参数
        for (KeyValuePair pair : getQueryParam()) {
            //这里遍历所有添加的参数，可对参数进行加密操作
            String key = pair.getKey();
            String value = pair.getValue().toString();
            //加密逻辑自己写
        }
        String simpleUrl = getSimpleUrl();  //拿到请求Url
        if (paramsBuilder.length() == 0) {
            return HttpUrl.get(simpleUrl);
        }
        return HttpUrl.get(simpleUrl + "?" + paramsBuilder);  //将加密后的参数和url组拼成HttpUrl对象并返回
    }
}
