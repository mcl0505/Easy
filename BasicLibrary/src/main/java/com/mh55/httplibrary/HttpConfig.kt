package com.mh55.httplibrary

import rxhttp.wrapper.annotation.DefaultDomain

object HttpConfig {

    @JvmField
    @DefaultDomain
    var apiUrl:String = ""

    var isDebug :Boolean = true

    var userId :String = ""
    var userToken :String = ""
    var versionName :String = ""
    var appModel :String = ""
    var deviceId :String = ""

    var md5Key = ""
    var rsaKey = ""
    var strKey = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
    var aesKey = ""
    var aesIv = ""

    var encrypt_key = ""


}