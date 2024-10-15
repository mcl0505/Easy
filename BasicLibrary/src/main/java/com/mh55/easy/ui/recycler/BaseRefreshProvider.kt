package com.mh55.easy.ui.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout

interface BaseRefreshProvider<cls,VIDB: ViewDataBinding> {
    /**
     * 获取列表控件
     * @return
     */
    fun getRecyclerView(): RecyclerView

    /**
     * 获取刷新控件
     * @return
     */
    fun getSmartRefreshLayout(): SmartRefreshLayout

    /**
     * 刷新逻辑
     */
    fun onRefreshData()

    /**
     * 适配器渲染
     * @param baseViewHolder
     * @param item
     */
    fun convertData(baseViewHolder: BaseViewHolder, item: cls, binding: VIDB,position: Int)

    /**
     * 适配器渲染
     * @param baseViewHolder
     * @param item
     */
    fun convertDataPayloads(
        baseViewHolder: BaseViewHolder,
        item: cls,
        binding: VIDB,
        position: Int,
        payloads: List<Any>
    )
}