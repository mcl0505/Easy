package com.mh55.easy.app

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.mh55.easy.helper.CrashHelper
import com.mh55.easy.manager.AppManager
import com.mh55.easy.utils.AppListener

object AppConfig {
    lateinit var mApplication: Application
    lateinit var mAppListener: AppListener
    fun init(application: Application,listener: AppListener){
        this.mApplication = application
        this.mAppListener = listener
        AppManager.register(application)
        Utils.init(application)
        CrashHelper.initCrashHandler(application)
    }
}