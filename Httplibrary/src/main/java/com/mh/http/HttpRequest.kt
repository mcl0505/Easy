package com.mh.http

import android.util.ArrayMap
import android.util.Log
import com.mh.http.interceptor.HeaderInterceptor
import com.mh.http.interceptor.HttpLogInterceptor
import com.mh.http.moshi.MoshiUtils
import com.mh.http.moshi.MyKotlinJsonAdapterFactory
import com.mh.http.moshi.MyStandardJsonAdapters
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object HttpRequest {
    private const val mSpName = "http_request_flag"
    private const val mKeyIsSave = "is_save"

    // 缓存 service
    private val mServiceMap = ArrayMap<String, Any>()

    // 默认的请求头
    private lateinit var mDefaultHeader: ArrayMap<String, String>

    /**
     * 存储 baseUrl，以便可以动态更改
     */
    private lateinit var mBaseUrlMap: ArrayMap<String, String>

    /**
     * 请求超时时间，秒为单位
     */
    var mDefaultTimeout = 10

    /**
     * 添加默认的请求头
     */
    @JvmStatic
    fun addDefaultHeader(name: String, value: String) {
        if (!this::mDefaultHeader.isInitialized) {
            mDefaultHeader = ArrayMap()
        }
        mDefaultHeader[name] = value
    }

    /**
     * 设置了 [mDefaultBaseUrl] 后，可通过此方法获取 Service
     */
    @JvmStatic
    fun <T> getService(cls: Class<T>): T {
        if (HttpBuilder.apiUrl.isNullOrEmpty()) {
            throw RuntimeException("必须初始化 HttpConfig.apiUrl")
        }

        if (this::mDefaultHeader.isInitialized) {
            val headers = HeaderInterceptor(mDefaultHeader)
            return getService(cls, HttpBuilder.apiUrl, headers)
        }

        return getService(cls, HttpBuilder.apiUrl, null)
    }

    /**
     * 如果有不同的 baseURL，那么可以相同 baseURL 的接口都放在一个 Service 钟，通过此方法来获取
     */
    @JvmStatic
    fun <T> getService(cls: Class<T>, host: String, vararg interceptors: Interceptor?): T {
        val name = cls.name

        var obj: Any? = mServiceMap[name]
        if (obj == null) {
            val httpClientBuilder = OkHttpClient.Builder()
            // 超时时间
            httpClientBuilder.connectTimeout(mDefaultTimeout.toLong(), TimeUnit.SECONDS)
            httpClientBuilder.addInterceptor(HttpLogInterceptor())

            // 拦截器
            interceptors.forEach { interceptor ->
                interceptor?.let {
                    httpClientBuilder.addInterceptor(it)
                }
            }

            Log.d("打印日志", MoshiUtils.toJson(httpClientBuilder.interceptors()))

            val client = httpClientBuilder.build()
            val builder = Retrofit.Builder().client(client)
                // 基础url
                .baseUrl(host)
                // JSON解析
                .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
            obj = builder.build().create(cls)
            mServiceMap[name] = obj
        }
        @Suppress("UNCHECKED_CAST")
        return obj as T
    }

    private fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(MyKotlinJsonAdapterFactory())
            .add(MyStandardJsonAdapters.FACTORY)
            .build()
    }
}
