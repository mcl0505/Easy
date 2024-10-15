package com.mh55.easy.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.mh55.easy.ext.singleClick
import com.mh55.easy.ext.visibleOrGone
import com.mh55.easy.ext.visibleOrInvisible

/**
 * 点击事件
 *
 */
@BindingAdapter(value = ["singleClick"], requireAll = false)
fun viewClick(view: View,block: View.OnClickListener){
    view.singleClick {
        block.onClick(it)
    }
}

/**
 * 是否显示布局  false = 隐藏  true=显示
 */
@BindingAdapter(value = ["isGone"], requireAll = false)
fun visibleOrGone(view: View,isGone: Boolean){
    view.visibleOrGone(isGone)
}

/**
 * 是否隐藏布局  false = 隐藏  true=显示
 */
@BindingAdapter(value = ["isInvisible"], requireAll = false)
fun visibleOrInvisible(view: View,isInvisible: Boolean){
    view.visibleOrInvisible(isInvisible)
}

