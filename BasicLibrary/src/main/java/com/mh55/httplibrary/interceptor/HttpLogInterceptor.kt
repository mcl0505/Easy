package com.mh55.httplibrary.interceptor

import android.util.Log
import com.mh55.httplibrary.HttpConfig
import com.mh55.httplibrary.encrypt.AESUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLDecoder

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/10/28
 *   功能描述: 请求日志拦截
 */
class HttpLogInterceptor  : Interceptor {
    private val TAG = "打印日志"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val mediaType = response.body!!.contentType()
        val content = response.body!!.string()
        val method = request.method
        val url = request.url

        if (url.encodedPath.contains(".apk"))return response


        if (HttpConfig.isDebug){
            val sb = StringBuilder()
            sb.append("||  ===============Start================================================================================ ")
            sb.append("\n|| 请求地址 ==> $url")
            sb.append("\n|| 请求头 ==>\n${request.headers}")
            val formSb = StringBuilder()
            if ("POST" == method||"PUT" == method){
                val json = getRequestInfo(request)
                if (json?.contains("name=\"file\"") == true){
                    formSb.append("")
                }else{
                    formSb.append(json)
                }

            }else if ("GET" == method){
//                formSb.append(url.query)
            }

            if (formSb.contains("encrypt_str") && formSb.contains("sign_str")){
                sb.append("\n|| 请求参数已加密 ==> ${formatJson(formSb.toString())}")
                val sign_str = formSb.split("&")[1].replace("sign_str=","")
                val encrypt_str = formSb.split("&")[0].replace("encrypt_str=","")
                sb.append("\n|| 请求参数解密 ==> \nencrypt_str=${
                    AESUtils.decrypt(
                    HttpConfig.encrypt_key,
                    HttpConfig.aesIv, encrypt_str)}")
            }else{
                sb.append("\n|| 请求参数 ==> ${formatJson(formSb.toString())}")
            }

            sb.append("\n|| 请求响应 ==> ${formatJson(content)}")
            sb.append("\n|| ========= End: $duration 毫秒========================================================================== ")
            Log.d(TAG, sb.toString())
        }

        return response.newBuilder()
            .body(ResponseBody.create(mediaType, content))
            .build()
    }

    private fun formatJson(json: String): String? {
        try {
            var i = 0
            val len = json.length
            while (i < len) {
                val c = json[i]
                if (c == '{') {
                    return JSONObject(json).toString(4)
                } else if (c == '[') {
                    return JSONArray(json).toString(4)
                } else if (!Character.isWhitespace(c)) {
                    return json
                }
                i++
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private fun getRequestInfo(request: Request?): String? {
        var str = ""
        if (request == null) {
            return str
        }
        val requestBody = request.body ?: return str
        try {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            //将返回的数据URLDecoder解码
            str = buffer.readUtf8()
            str = str.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
            str = URLDecoder.decode(str, "utf-8")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return str
    }
}