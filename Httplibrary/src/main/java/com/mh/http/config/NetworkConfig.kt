package com.mh.http.config

/**
 * 网络请求配置
 */
data class NetworkConfig(
    val baseUrl: String,
    val successCode: String = "200", // 成功的code值
    val codeKey: String = "code", // code字段名
    val messageKey: String = "message", // message字段名
    val dataKey: String = "data", // data字段名
    val connectTimeout: Long = 15, // 连接超时(秒)
    val readTimeout: Long = 15, // 读取超时(秒)
    val writeTimeout: Long = 15, // 写入超时(秒)
    val debug: Boolean = false, // 调试模式
    val successCodePredicate: (String) -> Boolean = { it == successCode }
)