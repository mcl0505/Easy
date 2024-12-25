package com.mh55.easy.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.LayoutInflater
import android.widget.TextView
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.mh55.easy.R
import com.mh55.easy.ext.toDp

/**
 * 暂无数据
 */
object EmptyViewUtil {
    fun setEmptyView(
        context: Context,
        recyclerView: RecyclerView,
        mDrawable: Drawable = R.mipmap.empty_pic_data.toDrawable(),
        desc: String = "暂无数据",
        marginTop:Int = 150
    ): View {
        val inflate =
            LayoutInflater.from(context).inflate(R.layout.view_no_data, recyclerView, false)
        if (!TextUtils.isEmpty(desc)) {
            val tvDesc = inflate.findViewById<TextView>(R.id.no_data_tv)
            tvDesc.text = desc
        }
        val imgDesc = inflate.findViewById<ImageView>(R.id.img_no_data)
        if (mDrawable != null) {
            imgDesc.setImageDrawable(mDrawable)
        }

        val params = imgDesc.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = marginTop.toDp()
        imgDesc.layoutParams = params
        return inflate
    }


}