package com.mh.http

import okhttp3.Interceptor

/**
 *             NetConfig.baseUrl = BaseConfig.BaseUrl
                NetConfig.isLog = BaseConfig.isLog
NetConfig.mToken = if (BaseConfig.token.isNullOrEmpty()) "token" else BaseConfig.token
NetConfig.mTokenKey = "XX-Token"
NetAppContext.init(instance)
 */
object NetBase {
    var baseUrl: String = "https://qdjtest.qiannengwuxian.com/api/"
    var defaultTimeout: Int = 60
    var mToken: String = ""
    var mTokenKey: String = "token"
    var interceptors = ArrayList<Interceptor>()
    var networkInterceptors = ArrayList<Interceptor>()
    var heads = HashMap<String, Any>()
    var enableHttps = false
    var isLog = false

    const val contentType = "Content-Type"
    const val contentTypeValue = "application/json"
    const val Encoding = "UTF-8"
}