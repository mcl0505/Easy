package com.mh55.easy.app

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.mh55.easy.helper.CrashHelper
import com.mh55.easy.manager.AppManager
import com.mh55.easy.utils.ActivityRouter
import com.mh55.easy.utils.AppListener

object AppConfig {
    var mApplication: Application?=null
    fun init(application: Application){
        if (application == null){
            throw Exception("application is null")
        }

        this.mApplication = application
        AppManager.register(application)
        ActivityRouter.init(mApplication)
        Utils.init(application)
//        CrashHelper.initCrashHandler(application)
    }
}