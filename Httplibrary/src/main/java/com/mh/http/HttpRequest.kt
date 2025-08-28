package com.mh.http

import com.mh.http.ssl.SSLContextUtil
import com.mh.http.moshi.MyKotlinJsonAdapterFactory
import com.mh.http.moshi.MyStandardJsonAdapters
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class HttpRequest {

    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
    private val retrofit: Retrofit

    init {
        // 添加配置增加拦截器
        val interceptors = NetBase.interceptors
        if (interceptors.isNotEmpty()) {
            interceptors.forEach { okHttpBuilder.addInterceptor(it) }
        }
        // 添加网络拦截器
        val networkInterceptors = NetBase.networkInterceptors
        if (networkInterceptors.isNotEmpty()) {
            networkInterceptors.forEach { okHttpBuilder.addInterceptor(it) }
        }

        okHttpBuilder.connectTimeout(NetBase.defaultTimeout.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(NetBase.defaultTimeout.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(NetBase.defaultTimeout.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.retryOnConnectionFailure(true)

        // 判断是否启用 https
        if (NetBase.enableHttps) {
            //给client的builder添加了增加可以忽略SSL
            val sslParams = SSLContextUtil.getSslSocketFactory()
            okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)
            okHttpBuilder.hostnameVerifier(SSLContextUtil.UnSafeHostnameVerifier)
        }

        val client = okHttpBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(NetBase.baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
            .build()
    }

    fun <S> create(serviceClass: Class<S>): S = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

    private fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(MyKotlinJsonAdapterFactory())
            .add(MyStandardJsonAdapters.FACTORY)
            .build()
    }
}
