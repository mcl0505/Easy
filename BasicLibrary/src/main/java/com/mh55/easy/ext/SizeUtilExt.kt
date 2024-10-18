package com.mh55.easy.ext

import com.mh55.easy.app.AppConfig


/**
 * 作者: mh55
 * 添加时间: 2024/2/26 13:54
 * 描述: 尺寸单位换算相关扩展属性
 */

/**
 * dp 转 px
 */
fun dp2px(dpValue: Float): Int {
    val scale = AppConfig.mApplication!!.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 描述: px 转 dp
 */
fun px2dp(pxValue: Float): Int {
    val scale = AppConfig.mApplication!!.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * sp 转 px
 */
fun sp2px(spValue: Float): Int {
    val scale = AppConfig.mApplication!!.resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

/**
 * px 转 sp
 */
fun px2sp(pxValue: Float): Int {
    val scale = AppConfig.mApplication!!.resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}
