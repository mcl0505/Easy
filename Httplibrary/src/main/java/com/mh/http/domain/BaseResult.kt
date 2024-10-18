package com.mh.http.domain

import com.mh.http.CODE_HTTP_ERROR
import com.mh.http.CODE_HTTP_SUCCESS
import com.mh.http.CODE_TOKEN_INVALID
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResult<T>(
    val msg: String? = "接口出错,请联系开发人员",
    val code: Int = CODE_HTTP_ERROR,
    var payload: T? = null
) : IBaseResponse<T?> {

    override fun code() = code

    override fun msg() = msg

    override fun data() = payload

    override fun isSuccess() = code == CODE_HTTP_SUCCESS
    override fun isInvalid() = code == CODE_TOKEN_INVALID
}