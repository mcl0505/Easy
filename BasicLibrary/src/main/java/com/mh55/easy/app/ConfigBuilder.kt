package com.mh55.easy.app

import com.mh55.easy.R
import okhttp3.Interceptor

object ConfigBuilder {
    //是否开启日志打印
    var isOpenLog = true
    //是否开启日志采集
    var isOpenCarsh = true
    //是否开启全局置灰  用作特殊节日默哀处理
    var isGray = false
    var mImagePlaceholder = R.mipmap.icon_avatar_default
    var mInterceptor = mutableListOf<Interceptor>()
    //是否开启请求加密
    var isOpenEncrypt = false

    object RefreshPageConfig{
        var mLimit = 10
        var mEmptyImage = R.mipmap.img_empty
        var mEmptyTipText = "暂无数据"
    }
}