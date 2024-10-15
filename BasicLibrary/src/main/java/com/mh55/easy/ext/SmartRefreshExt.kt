package com.mh55.easy.ext

import android.annotation.SuppressLint
import com.mh55.easy.R
import com.mh55.easy.app.ConfigBuilder
import com.scwang.smart.refresh.layout.SmartRefreshLayout


@SuppressLint("RestrictedApi")
fun SmartRefreshLayout.setHeardColor(
    backgroundColor: Int = R.color.color_transparent.getColor(),
    textColor: Int = R.color.color_white.getColor()
) {
    this.refreshHeader?.setPrimaryColors(backgroundColor, textColor)
}

@SuppressLint("RestrictedApi")
fun SmartRefreshLayout.setFooterColor(
    backgroundColor: Int = R.color.color_transparent.getColor(),
    textColor: Int = R.color.color_white.getColor()
) {
    this.refreshFooter?.setPrimaryColors(backgroundColor, textColor)
}


//根据返回的条数判断时候需要加载更多
fun <T> MutableList<T>.isLoadMore() = this.size == ConfigBuilder.RefreshPageConfig.mLimit

fun SmartRefreshLayout.noMoreData(isLoadMore: Boolean = false) {
    this.apply {
        finish(isLoadMore)
    }
}

fun SmartRefreshLayout.finish(isLoadMore: Boolean = true) {
    this.apply {
        closeHeaderOrFooter()
        setEnableLoadMore(isLoadMore)
        finishLoadMoreWithNoMoreData()

    }
}