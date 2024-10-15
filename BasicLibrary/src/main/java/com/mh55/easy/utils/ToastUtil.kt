package com.mh55.easy.utils

import android.graphics.drawable.Drawable
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import com.mh55.easy.R
import com.mh55.easy.ext.getColor

/**
 * 吐司工具类
 *  在两秒内如果是相同的消息则只提示一次
 */
object ToastUtil {
    private var time: Long = 0
    private var oldMsg: String? = null
    fun toast(msg:String = "",img:Drawable?=null){
        if (msg != oldMsg) {
            create(msg,img)
            time = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - time > 2000) {
                create(msg,img)
                time = System.currentTimeMillis()
            }
        }
        oldMsg = msg
    }

    private fun create(massage: String,img:Drawable?=null) {
        if (img!=null){
            ToastUtils.make()
                .setBgResource(R.drawable.bg_toast)
                .setTextColor(R.color.color_white.getColor())
                .setDurationIsLong(false)
                .setTopIcon(img)
                .setGravity(
                    Gravity.CENTER,
                    0,
                    0
                ).show(massage)
        }else{
            ToastUtils.make()
                .setBgResource(R.drawable.bg_toast)
                .setTextColor(R.color.color_white.getColor())
                .setDurationIsLong(false)
                .setGravity(
                    Gravity.CENTER,
                    0,
                    0
                ).show(massage)
        }
    }

}
