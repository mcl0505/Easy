package com.mh55.easy.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.math.BigDecimal

object CacheDataManager {

    /**
     * 获取缓存大小
     */
    fun getLocalCachesSize(context: Context): String {
        try {
            var cacheSize: Long = getFolderSize(context.cacheDir)
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                context.externalCacheDir?.let { cacheSize += getFolderSize(it) }
                context.getExternalFilesDir("")?.let { cacheSize += getFolderSize(it) }
            }

            return getFormatSize(cacheSize.toDouble())
        } catch (e: Exception) {
            throw e
        }

    }

    /**
     * 清除文件
     */
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            context.externalCacheDir?.let { deleteDir(it) }
            context.getExternalFilesDir("")?.let { deleteDir(it) }
        }
    }

    /**
     * 删除文件
     */
    fun deleteDir(dir: File): Boolean {
        if (dir != null && dir.isDirectory) {
            var children = dir.list()
            children.forEach {
                var success: Boolean = deleteDir(File(dir, it))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }

    /**
     * 获取文件的大小
     */
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            var listFiles = file.listFiles()
            listFiles.forEach {
                //如果下面还有文件
                if (it.isDirectory) {
                    size += getFolderSize(it)
                } else {
                    size += it.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 格式化单位
     */
    fun getFormatSize(size: Double): String {
        val kiloByte: Double = size / 1024
        if (kiloByte < 1) {
            return "${size}Byte"
        }

        val megaByte: Double = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }

        val result4 = BigDecimal(teraBytes.toString())
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }
}
