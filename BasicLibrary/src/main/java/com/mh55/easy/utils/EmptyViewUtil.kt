package com.mh55.easy.utils

import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.widget.TextView
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mh55.easy.R
import com.mh55.easy.ext.getDrawable
import com.mh55.easy.ext.toDp

/**
 * 暂无数据
 */
object EmptyViewUtil {
    fun setEmptyView(
        context: Context,
        recyclerView: RecyclerView,
        mDrawable: Int = R.mipmap.empty_pic_data,
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
            imgDesc.setImageDrawable(mDrawable.getDrawable())
        }

        val params = imgDesc.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = marginTop.toDp()
        return inflate
    }


}