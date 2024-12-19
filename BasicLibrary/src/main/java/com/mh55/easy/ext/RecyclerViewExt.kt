package com.mh55.easy.ext

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mh55.easy.R
import com.mh55.easy.app.ConfigBuilder
import com.mh55.easy.ui.recycler.BindAdapter
import com.mh55.easy.ui.recycler.DefaultDecoration
import com.mh55.easy.utils.EmptyViewUtil


enum class DividerOrientation {
    VERTICAL, HORIZONTAL, GRID
}

fun <T,DB: ViewDataBinding> RecyclerView.addAdapter(list: MutableList<T>,layout:Int,block:(holder: BaseViewHolder, item: T, binding: DB)->Unit): BindAdapter<T,DB>{
    val mAdapter  = object : BindAdapter<T, DB>(layout) {
        override fun convertBind(holder: BaseViewHolder, item: T, binding: DB) {
            block.invoke(holder, item, binding)
        }

    }
    this.adapter = mAdapter
    mAdapter.setNewInstance(list)
    return mAdapter
}

/**
 * 创建[HoverLinearLayoutManager]  线性列表
 * @param orientation 列表方向
 * @param reverseLayout 是否反转列表
 * @param scrollEnabled 是否允许滚动
 */
fun RecyclerView.linear(
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false,
    stackFromEnd: Boolean = false
): RecyclerView {
    layoutManager = LinearLayoutManager(context, orientation, reverseLayout).apply {
        this.stackFromEnd = stackFromEnd
    }
    return this
}

/**
 * 创建[HoverGridLayoutManager] 网格列表
 * @param spanCount 网格跨度数量
 * @param orientation 列表方向
 * @param reverseLayout 是否反转
 */
fun RecyclerView.grid(
    spanCount: Int = 1,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
): RecyclerView {
    layoutManager = GridLayoutManager(context, spanCount, orientation, reverseLayout)
    return this
}

/**
 * 创建[HoverStaggeredGridLayoutManager] 交错列表
 * @param spanCount 网格跨度数量
 * @param orientation 列表方向
 * @param reverseLayout 是否反转
 */
fun RecyclerView.staggered(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
): RecyclerView {
    layoutManager = StaggeredGridLayoutManager(spanCount, orientation).apply {
        this.reverseLayout = reverseLayout
    }
    return this
}
//</editor-fold>

//<editor-fold desc="分割线">

/**
 * 函数配置分割线
 * 具体配置参数查看[DefaultDecoration]
 */
fun RecyclerView.divider(
    block: DefaultDecoration.() -> Unit
): RecyclerView {
    val itemDecoration = DefaultDecoration(context).apply(block)
    addItemDecoration(itemDecoration)
    return this
}

/**
 * 指定Drawable资源为分割线, 分割线的间距和宽度应在资源文件中配置
 * @param drawable 描述分割线的drawable
 * @param orientation 分割线方向, 仅[androidx.recyclerview.widget.GridLayoutManager]需要使用此参数, 其他LayoutManager都是根据其方向自动推断
 */
fun RecyclerView.divider(
    @DrawableRes drawable: Int,
    orientation: DividerOrientation = DividerOrientation.VERTICAL
): RecyclerView {
    return divider {
        setDrawable(drawable)
        this.orientation = orientation
    }
}

/**
 * 设置空白间距分割
 * @param space item的空白间距
 * @param orientation 分割线方向, 仅[androidx.recyclerview.widget.GridLayoutManager]需要使用此参数, 其他LayoutManager都是根据其方向自动推断
 */
fun RecyclerView.dividerSpace(
    space: Int,
    orientation: DividerOrientation = DividerOrientation.VERTICAL,
): RecyclerView {
    return divider {
        setDivider(space)
        this.orientation = orientation
    }
}
//</editor-fold>

/**
 * 适配器加载分页数据  利用适配器条数判断
 * @param isFirst 是否首页
 * @param mList 数据源
 * @param mDrawable 显示的图标  ConfigBuilder中配置默认图片
 * @param isShowText 显示的文字 ConfigBuilder中配置默认提示信息
 * @param layout 显示的布局
 * @param marginTop 图片距离顶部距离
 */
fun <T> BindAdapter<T, *>.setAdapterEmptyOrListOffset(
    mList: MutableList<T>?=null,
    mDrawable: Drawable = ConfigBuilder.RefreshPageConfig.mEmptyImage.toDrawable(),
    isShowText: String = ConfigBuilder.RefreshPageConfig.mEmptyTipText,
    @LayoutRes emptyLayout: Int = 0,
    marginTop:Int = 150
) {
    //判断当前是否是刷新或者首次请求
    var isFirst = this.mOffset == 0

    if (this.mOffset == 0){
        this.setNewInstance(mutableListOf())
    }

    if (isFirst) {

        if (mList == null || mList.size == 0) {
            if (emptyLayout == 0){
                this.setEmptyView(EmptyViewUtil.setEmptyView(this.context,this.recyclerView,mDrawable,isShowText,marginTop = marginTop))
            }else {
                this.setEmptyView(emptyLayout)
            }
        }else {
            this.setNewInstance(mList)
        }

    } else {
        this.addData(mList!!)
    }

    this.mOffset = data.size

}

/**
 * 适配器加载分页数据
 * @param isFirst 是否首页
 * @param mList 数据源
 * @param mDrawable 显示的图标  ConfigBuilder中配置默认图片
 * @param isShowText 显示的文字 ConfigBuilder中配置默认提示信息
 * @param layout 显示的布局
 * @param marginTop 图片距离顶部距离
 */
fun <T> BindAdapter<T, *>.setAdapterEmptyOrListPage(
    mList: MutableList<T>?=null,
    mDrawable: Drawable = ConfigBuilder.RefreshPageConfig.mEmptyImage.toDrawable(),
    isShowText: String = ConfigBuilder.RefreshPageConfig.mEmptyTipText,
    @LayoutRes emptyLayout: Int = 0,
    marginTop:Int = 150
) {
    //判断当前是否是刷新或者首次请求
    var isFirst = this.mPage == 1

    if (isFirst) {
        this.setNewInstance(mutableListOf())

        if (mList == null || mList.size == 0) {
            if (emptyLayout == 0){
                this.setEmptyView(EmptyViewUtil.setEmptyView(this.context,this.recyclerView,mDrawable,isShowText,marginTop = marginTop))
            }else {
                this.setEmptyView(emptyLayout)
            }
        }else {
            this.setNewInstance(mList)
        }

    } else {
        this.addData(mList!!)
    }
}
