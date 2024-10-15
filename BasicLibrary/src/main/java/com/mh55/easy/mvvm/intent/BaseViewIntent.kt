package com.mh55.easy.mvvm.intent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View

sealed class BaseViewIntent{
    /**
     * 跳转界面，不返回参数
     */
    data class startActivity(
        val clazz: Class<out Activity>,
        val map: MutableMap<String, *>? = null,
        val bundle: Bundle?=null):BaseViewIntent()

    /**
     * 跳转界面  返回参数
     */
    data class startActivityForResult(
        val clazz: Class<out Activity>,
        val map: MutableMap<String, *>? = null,
        val bundle: Bundle?=null
        ):BaseViewIntent()

    /**
     * 设置返回参数
     */
    data class setResult(
        val resultCode: Int,
        val map: MutableMap<String, *>? = null,
        val bundle: Bundle? = null,
        val data: Intent? = null):BaseViewIntent()

    data class finish(
        val resultCode: Int?=null,
        val map: MutableMap<String, *>? = null,
        val bundle: Bundle? = null
    ):BaseViewIntent()

    data class showLoading(
        val isShow:Boolean = false,
        val showMsg:String = ""
    ):BaseViewIntent()
}
