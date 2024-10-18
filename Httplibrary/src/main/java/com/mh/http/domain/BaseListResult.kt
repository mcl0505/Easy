package com.mh.http.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseListResult<T>(
    //数据列表
    val content:MutableList<T> = ArrayList()
)