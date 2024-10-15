package com.mh55.httplibrary.encrypt

import com.blankj.utilcode.util.GsonUtils
import com.mh55.easy.app.ConfigBuilder.isOpenEncrypt
import com.mh55.httplibrary.HttpConfig.aesIv
import com.mh55.httplibrary.HttpConfig.aesKey
import com.mh55.httplibrary.HttpConfig.encrypt_key
import com.mh55.httplibrary.HttpConfig.userId
import okhttp3.RequestBody
import rxhttp.wrapper.annotation.Param
import rxhttp.wrapper.param.FormParam
import rxhttp.wrapper.param.Method

@Param(methodName = "postEncryptForm")
class PostEncryptFormParam //Method.POST代表post请求
    (private val url: String) : FormParam(url, Method.POST) {
    override fun getRequestBody(): RequestBody {
        if (isOpenEncrypt){
            add("uid", userId)
            val list = bodyParam
            val hashMap: HashMap<String, Any> = HashMap<String, Any>()
            if (list != null && list.size > 0) {
                for (pair in list) {
                    hashMap[pair.key] = pair.value.toString() + ""
                }
                val dataStr = GsonUtils.toJson(hashMap)
                val encrypt = AESUtils.encrypt(encrypt_key, aesIv, dataStr)
                val sign = AESUtils.encrypt(aesKey, aesIv, dataStr)
                removeAllBody()
                add("encrypt_str", encrypt)
                add("sign_str", sign)
            }
        }

        return super.getRequestBody()
    }
}
