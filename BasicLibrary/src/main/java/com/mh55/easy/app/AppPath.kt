package com.mh55.easy.app

import com.mh55.easy.manager.AppManager
import com.mh55.easy.utils.BaseTimeUtils

object AppPath {
    val timestamp = BaseTimeUtils.getCurrentMillis()
    private val fileSpace = System.getProperty("file.separator")
    //文件路径
    private val appRootFilePath = AppManager.getContext().getExternalFilesDir(null)?.path
    //缓存路径
    private val appRootCachePath = AppManager.getContext().externalCacheDir?.path
    private const val crash = "CrashHelper"
    private const val image = "image"
    private const val video = "video"
    private const val cookie = "cookie"
    private const val cache = "cache"

    //TODO 异常捕获存储路径 Android/data/包名/files/crash/
    val crashPath = appRootFilePath + fileSpace + crash + fileSpace
    val imagePath = appRootCachePath + fileSpace + image + fileSpace
    val videoPath = appRootCachePath + fileSpace + video + fileSpace
    val cookiePath = appRootCachePath + fileSpace + cookie + fileSpace
    val cachePath = appRootCachePath + fileSpace + cache + fileSpace


    val crashFileName = "crash_$timestamp.txt"
    val cacheFileName = "cache_$timestamp.txt"
    val cookieFileName = "cookie_$timestamp.txt"
}