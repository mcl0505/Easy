package com.mh0505.basic

import android.app.Application
import com.mh55.easy.app.AppConfig
import com.mh55.easy.ext.toast
import com.mh55.easy.utils.AppListener
import com.tencent.rfix.anno.ApplicationLike
import com.tencent.rfix.entry.DefaultRFixApplicationLike
import com.tencent.rfix.loader.entity.RFixLoadResult

@ApplicationLike(application = ".App")
class App(p0: Application?, p1: RFixLoadResult?) : DefaultRFixApplicationLike(p0, p1) {
    override fun onCreate() {
        super.onCreate()
        AppConfig.init(application)
    }
}