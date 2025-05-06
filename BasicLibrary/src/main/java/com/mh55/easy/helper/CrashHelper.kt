package com.mh55.easy.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.os.Process
import android.os.SystemClock
import android.util.Log
import android.util.TimeUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.mh55.easy.app.AppPath
import com.mh55.easy.utils.BaseTimeUtils
import com.mh55.easy.utils.LogUtil
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.text.SimpleDateFormat
import java.util.Date

/**
 * 创建作者: 梦虍
 * 创建时间: 2024/2/26 14:59
 * 功能描述: 本地异常捕获，保存到内存中
 */
@SuppressLint("StaticFieldLeak")
object CrashHelper : Thread.UncaughtExceptionHandler {
    private var mContext: Context? = null
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    private var mExceptionMessage: String? = null
    fun initCrashHandler(context: Context?) {
        mContext = context
        //获取系统默认的 UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该 CrashHandler 为程序的默认 UncaughtExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        if (!handleException(throwable) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的 UncaughtExceptionHandler 来处理
            mDefaultHandler!!.uncaughtException(thread, throwable)
        } else {
//            SystemClock.sleep(1000)
//            //退出程序
//            Process.killProcess(Process.myPid())
        }
    }

   /**
    * 描述: 将异常信息保存至 SD 卡
    */
    private fun handleException(throwable: Throwable?): Boolean {
        if (throwable == null) {
            return false
        }

        //使用Toast来显示异常信息
        object : Thread() {
            override fun run() {
                Looper.prepare()
                Looper.loop()
            }
        }.start()
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        throwable.printStackTrace(printWriter)
        var cause = throwable.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        try {
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val sb = StringBuffer()
            val pm = mContext!!.packageManager
            val pi = pm.getPackageInfo(mContext!!.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                sb.append("=========================================================================\n")
                sb.append("应用版本：" + pi.versionName + "\n")
                sb.append("应用版本号：" + pi.versionCode + "\n")
                sb.append("品牌：" + Build.MANUFACTURER + "\n")
                sb.append("机型：" + Build.MODEL + "\n")
                sb.append("Android 版本：" + Build.VERSION.RELEASE + "\n")
                sb.append("系统版本：" + Build.DISPLAY + "\n")
                sb.append("当前手机CPU：" + Build.CPU_ABI + "\n")
                val timestamp = BaseTimeUtils.getCurrentMillis()
                val time =  BaseTimeUtils.millisToString(timestamp)
                sb.append("报错时间：${time}\n")
                sb.append("=========================================================================\n")
                sb.append("异常信息：\n$result")
                val fileName = "crash_$timestamp.txt"
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    val path = AppPath.crashPath
                    val dir = File(path)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val file = File(path, fileName)
                    FileIOUtils.writeFileFromString(file,sb.toString())
                }
                mExceptionMessage = sb.toString()
            }
        } catch (e: Exception) {
            LogUtil.i( "收集包信息时出错\n${e}")
        }
        return true
    }

}