package com.mh.http.config

class NetworkDslConfig {
    //TODO  默认配置
    var baseUrl: String = "" //请求地址
    var debug: Boolean = false //是否打印日志
    var connectTimeout: Long = 15 //连接时长
    var readTimeout: Long = 15 // 读取时长
    var writeTimeout: Long = 15 //写入时长
    //TODO  响应码配置
    var defaultCodeKey: String = "code"
    var defaultMessageKey: String = "msg"
    var defaultDataKey: String = "data"
    var defaultSuccessCode: String = "200"
    var successCodePredicate: (String) -> Boolean = { it == defaultSuccessCode }

    //TODO 多URL配置
    private val urlConfigs = mutableMapOf<String, UrlConfig>()

    //TODO 拦截器配置
    var headers: Map<String, String> = emptyMap()
    var dynamicHeaders: () -> Map<String, String> = { emptyMap() }

    // 添加多URL配置
    fun url(name: String, config: UrlConfig.() -> Unit) {
        urlConfigs[name] = UrlConfig().apply(config)
    }

    // 获取URL配置
    fun getUrlConfig(name: String): UrlConfig? = urlConfigs[name]

    fun getUrlConfigs() = urlConfigs

    // URL配置类
    class UrlConfig {
        var baseUrl: String = ""
        var codeKey: String = "code"
        var messageKey: String = "msg"
        var dataKey: String = "data"
        var successCode: Int = 200
        var successPredicate: (Int) -> Boolean = { it == successCode }
    }
}