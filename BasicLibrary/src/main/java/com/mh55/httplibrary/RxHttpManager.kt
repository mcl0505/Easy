package com.mh55.httplibrary

import android.app.Application
import com.mh55.httplibrary.encrypt.MD5Utils
import com.mh55.httplibrary.encrypt.RSAUtils
import com.mh55.httplibrary.interceptor.HttpLogInterceptor
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.callback.Consumer
import rxhttp.wrapper.param.Param
import rxhttp.wrapper.ssl.HttpsUtils
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

object RxHttpManager {

    fun init(context: Application) {
        val sslParams: HttpsUtils.SSLParams = HttpsUtils.getSslSocketFactory()
        val client = OkHttpClient.Builder() //禁用代理防止抓包
            .proxy(Proxy.NO_PROXY)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS) //添加信任证书
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //忽略host验证
            .hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true }) //            .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
            //            .addInterceptor(new RedirectInterceptor())
            //            .addInterceptor(new TokenInterceptor())
            .addInterceptor(HttpLogInterceptor()) //添加自己定义的日志拦截器  方便查看网络请求信息
            .build()

        //设置缓存策略，非必须
        //RxHttp初始化，自定义OkHttpClient对象，非必须
        RxHttpPlugins.init(client)
            .setDebug(false) //暂时启用原框架中的日志打印  BuildConfig.IS_DEBUG
//                            .setCache(cacheFile, 1000 * 100, CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE)
            // .setResultDecoder(s -> s) //设置数据解密/解码器，非必须
//            .setConverter(MoshiConverter.create()) //设置全局的转换器，非必须
            // .setExcludeCacheKeys("time") //设置一些key，不参与cacheKey的组拼
            .setOnParamAssembly(Consumer<Param<*>> { p: Param<*> ->

                //添加公共请求头
                val uid: String = HttpConfig.userId
                val token: String = HttpConfig.userToken
                val timestamp = System.currentTimeMillis().toString() + ""
                val appversion: String = HttpConfig.versionName
                val devicename: String = HttpConfig.appModel
                val deviceid: String = HttpConfig.deviceId
                val sb = StringBuffer()
                sb.append(HttpConfig.md5Key)
                sb.append(token)
                sb.append("android")
                sb.append(appversion)
                sb.append(timestamp)
                sb.append(deviceid)
                sb.append(HttpConfig.md5Key)
                val md5: String = MD5Utils.MD5(sb.toString()).toUpperCase()
                val sEncryptionKey = dynamicEncryptionKey

                val encryptSecret = RSAUtils().encrypt(sEncryptionKey, HttpConfig.rsaKey).replace("\n", "")

                p.addHeader("uid", uid)
                    .addHeader("token", token)
                    .addHeader("appversion", appversion)
                    .addHeader("deviceid", deviceid)
                    .addHeader("devicename", devicename)
                    .addHeader("timestamp", timestamp)
                    .addHeader("encryptsecret", encryptSecret)
                    .addHeader("signstr", md5)
                    .addHeader("client", "1")
                    .addHeader("APPLICATION-PLATFORM", "")
                    .addHeader("appview", "android")
            })
    }

    val dynamicEncryptionKey: String
        /**
         * 动态生成的加密
         *
         * @return
         */
        get() {
            val sb = StringBuilder()
            val model: String = HttpConfig.strKey
            val m = model.toCharArray()
            for (j in 0..15) {
                val random = Math.random()
                val c = m[(random * model.length).toInt()]
                sb.append(c)
            }
            HttpConfig.encrypt_key = sb.toString()
            return sb.toString()
        }

}
