package com.mh.http.config

/**
 * 请求拦截器配置
 */
data class InterceptorConfig(
    val headers: Map<String, String> = emptyMap(), // 公共请求头
    val dynamicHeaders: () -> Map<String, String> = { emptyMap() },//添加动态header支持
)