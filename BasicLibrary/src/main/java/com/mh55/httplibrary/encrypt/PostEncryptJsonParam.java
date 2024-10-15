package com.mh55.httplibrary.encrypt;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rxhttp.wrapper.annotation.Param;
import rxhttp.wrapper.param.JsonParam;
import rxhttp.wrapper.param.Method;


@Param(methodName = "postEncryptJson")
public class PostEncryptJsonParam extends JsonParam {
    private MediaType MEDIA_TYPE_JSON=MediaType.parse("application/json; charset=utf-8");

    public PostEncryptJsonParam(String url) {
        super(url, Method.POST);
    }

    @Override
    public RequestBody getRequestBody() {
        //这里拿到你添加的所有参数
        Map<String, Object> params = getBodyParam();
        //根据上面拿到的参数，自行实现解密逻辑
        String encryptStr = "加密后的字符串";
        //发送加密后的字符串
        return RequestBody.create(MEDIA_TYPE_JSON, encryptStr);
    }
}