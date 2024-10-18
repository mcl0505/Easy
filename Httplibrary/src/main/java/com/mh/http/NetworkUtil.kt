package com.mh.http

import android.content.Context
import android.net.ConnectivityManager

/**
 * 网络工具类
 */
object NetworkUtil {
    fun isConnected(context: Context): Boolean {
        val manager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as? ConnectivityManager?
        if (manager != null) {
            val info = manager.activeNetworkInfo
            return info != null && info.isAvailable
        }
        return false
    }

    /**
     * 是否使用WIFI联网
     * @return
     */
    fun isWifiConnection(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return false
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return if (networkInfo != null) {
            networkInfo.isAvailable && networkInfo.isConnected
        } else false
    }
}