package com.mh55.easy.utils

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.mh55.easy.app.ConfigBuilder
import java.util.*

object LogUtil {

    private const val V = 0x1
    private const val D = 0x2
    private const val I = 0x3
    private const val W = 0x4
    private const val E = 0x5
    private const val A = 0x6

    private const val TAG = "打印日志"
    private const val TOP_BORDER =
        "╔══════打印开始═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private const val LEFT_BORDER = "║ "
    private const val BOTTOM_BORDER =
        "╚══════打印结束═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════"

    @JvmStatic
    fun v(msg: Any?) {
        printLog(V, TAG, msg)
        LogUtils.d()
    }

    @JvmStatic
    fun v(tag: String?, msg: String?) {
        printLog(V, tag, msg)
    }

    @JvmStatic
    fun d(msg: Any?) {
        printLog(D, TAG, msg)
    }

    @JvmStatic
    fun d(tag: String?, msg: Any?) {
        printLog(D, tag, msg)
    }

    @JvmStatic
    fun i(msg: Any?) {
        printLog(I, TAG, msg)
    }

    @JvmStatic
    fun i(tag: String?, msg: Any?) {
        printLog(I, tag, msg)
    }

    @JvmStatic
    fun w(msg: Any?) {
        printLog(W, TAG, msg)
    }

    @JvmStatic
    fun w(tag: String?, msg: Any?) {
        printLog(W, tag, msg)
    }

    @JvmStatic
    fun e(msg: Any?) {
        printLog(E, TAG, msg)
    }

    @JvmStatic
    fun e(tag: String?, msg: Any?) {
        printLog(E, tag, msg)
    }

    @JvmStatic
    fun a(msg: Any?) {
        printLog(A, TAG, msg)
    }

    @JvmStatic
    fun a(tag: String?, msg: Any?) {
        printLog(A, tag, msg)
    }

    private fun printLog(type: Int, tagStr: String?, objectMsg: Any?) {

        if (!ConfigBuilder.isOpenLog){
            return
        }

        val stackTrace =
            Thread.currentThread().stackTrace
        val index = 4
        val className = stackTrace[index].fileName
        var methodName = stackTrace[index].methodName
        val lineNumber = stackTrace[index].lineNumber
        val tag = tagStr ?: className
        methodName = methodName.substring(0, 1).uppercase(Locale.getDefault()) + methodName.substring(1)
        val stringBuilder = StringBuilder()
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#")
            .append(methodName).append(" ] \n$TOP_BORDER\n")
        val msg = objectMsg?.toString() ?: "Log with null Object"
        stringBuilder.append("$LEFT_BORDER $msg")
        stringBuilder.append("\n$BOTTOM_BORDER\n")
        val logStr = stringBuilder.toString()
        when (type) {
            V -> Log.v(tag, logStr)
            D -> Log.d(tag, logStr)
            I -> Log.i(tag, logStr)
            W -> Log.w(tag, logStr)
            E -> Log.e(tag, logStr)
            A -> Log.wtf(tag, logStr)
        }
    }

}