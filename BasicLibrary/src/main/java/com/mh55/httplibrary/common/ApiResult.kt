package com.mh55.httplibrary.common;

/**
 * 服务器返回的数据基类
 */

data class ApiResult<T>(
    var data: T? = null,
    var model: String? = null,
    var code: Int = -1,
    var msg: String = "",
    var message: String = "",
    var success: Boolean = false
) {
    override fun toString(): String {
        val stringBuilder = StringBuilder()
        if (data != null) stringBuilder.append(data)
        return stringBuilder.append(" --- ").append(msg).toString()
    }
}