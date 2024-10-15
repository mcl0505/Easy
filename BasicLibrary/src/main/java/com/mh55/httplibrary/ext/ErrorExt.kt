package com.mh55.httplibrary.ext

import com.blankj.utilcode.util.NetworkUtils.isConnected
import com.blankj.utilcode.util.StringUtils
import com.mh55.easy.R
import com.mh55.easy.utils.LogUtil
import com.mh55.httplibrary.parser.MyParseException
import rxhttp.wrapper.exception.HttpStatusCodeException
import rxhttp.wrapper.exception.ParseException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

val Throwable.errorCode: Int
    get() {
        val errorCode = when (this) {
            is HttpStatusCodeException -> {//请求失败异常 状态码
                this.statusCode
            }
            is ParseException -> {  // ParseException 异常表明请求成功，但是数据不正确
                this.errorCode
            }
            is MyParseException -> {  // ParseException 异常表明请求成功，但是数据不正确
                this.errorCode
            }
            else -> {
                -1
            }
        }
        return "$errorCode".toInt()
    }

val Throwable.errorMsg: String
    get() {
        var errorMsg = handleNetworkException(this)  //网络异常
        return errorMsg ?: message ?: this.toString()
    }

//处理网络异常
private fun <T> handleNetworkException(throwable: T): String? {
    val stringId =
        if (!isConnected()) {//网络异常
            R.string.notify_no_network
        } else if (throwable is UnknownHostException) {
            R.string.network_no_host
        } else if (throwable is SocketTimeoutException || throwable is TimeoutException) {
            R.string.time_out_please_try_again_later  //前者是通过OkHttpClient设置的超时引发的异常，后者是对单个请求调用timeout方法引发的超时异常
        } else if (throwable is ConnectException) {
            R.string.esky_service_exception  //连接异常
        } else {
            -1
        }
    return if (stringId == -1) null else StringUtils.getString(stringId)
}


fun errorCodeProcess(it: Throwable,block:(code:Int)->Unit) {
    if (it is MyParseException) {
        LogUtil.d("操！请求出错了 RxHttp 11111-接口::${it.errorCode}--> ${it.message}:::${it.httpUrl}")

    } else {
        LogUtil.e("操！请求出错了 RxHttp 00000-接口::${it.errorCode}--> ${it.message}")
        //将发生的网络请求异常上报
    }
    block.invoke(it.errorCode)
}