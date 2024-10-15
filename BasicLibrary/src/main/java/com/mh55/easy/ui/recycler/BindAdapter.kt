package com.mh55.easy.ui.recycler

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/10/13
 *   功能描述: BaseQuickAdapter 的使用再度封装   针对单布局
 */
abstract class BindAdapter<T,DB: ViewDataBinding>(layout:Int) : BaseQuickAdapter<T, BaseViewHolder>(layout), LoadMoreModule {
    var mPage = 1
    var mOffset = 0
    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        // 绑定 view
        DataBindingUtil.bind<DB>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: T) {
        val mAdapterBinding = holder.getBinding<DB>()
        convertBind(holder,item,mAdapterBinding!!)
    }

    override fun convert(holder: BaseViewHolder, item: T, payloads: List<Any>) {
        val mAdapterBinding = holder.getBinding<DB>()
        convertBind(holder,item,mAdapterBinding!!,payloads)
    }

    abstract fun convertBind(holder: BaseViewHolder, item: T, binding: DB)

    open fun convertBind(holder: BaseViewHolder, item: T, binding: DB,payloads: List<Any>){}
}