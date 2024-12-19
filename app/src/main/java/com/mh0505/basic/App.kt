package com.mh0505.basic

import android.app.Application
import com.mh55.easy.app.AppConfig
import com.mh55.easy.ext.toast
import com.mh55.easy.utils.AppListener

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfig.init(this)
    }
}