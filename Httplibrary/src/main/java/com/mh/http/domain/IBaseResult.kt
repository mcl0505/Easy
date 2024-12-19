package com.mh.http.domain

/**
 * 实体类必须实现这个接口并复写其中的方法，这样才可以使用 BaseViewModel 中的协程方法
 */
interface IBaseResult<T> {
    fun code(): Int
    fun msg(): String?
    fun data(): T?
    fun isSuccess(): Boolean
    fun isInvalid(): Boolean
}