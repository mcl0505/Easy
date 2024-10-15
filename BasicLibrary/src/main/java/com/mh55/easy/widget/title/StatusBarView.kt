package com.mh55.easy.widget.title

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.mh55.easy.utils.StatusBarUtils.getStatusBarHeight

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：StatusBarView
 * 创建时间：2020/6/29
 * 功能描述： 顶部状态栏的代替控件
 */
class StatusBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            mStatusBarHeight
        )
    }

    private var mStatusBarHeight = 0



    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = getStatusBarHeight(context)
        }
    }
}