package com.mh55.easy.ext

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import com.mh55.easy.R
import com.mh55.easy.app.ConfigBuilder
import com.mh55.easy.ui.recycler.BindAdapter
import com.mh55.easy.utils.EmptyViewUtil


enum class AdapterLoadType{
    Page,
    Offset
}

class RecyclerViewAdapterDSL<T> {
    //分页类型
    var loadType:AdapterLoadType = AdapterLoadType.Page
    //数据源
    var data:MutableList<T> = mutableListOf()
    //缺省布局
    var emptyLayout:Int = 0
    //缺省图
    var emptyImage:Drawable = ConfigBuilder.RefreshPageConfig.mEmptyImage.toDrawable()
    //缺省提示文字
    var emptyText:String = ConfigBuilder.RefreshPageConfig.mEmptyTipText
    //缺省提示文字颜色
    var emptyTextColor:Int = R.color.color_333333.getColor()
    var marginTop:Int = 150
    var marginBottom:Int = 10
}



fun <T> BindAdapter<T, *>.setAdapterData(dsl: RecyclerViewAdapterDSL<T>.()->Unit){
    val adapterDSL =RecyclerViewAdapterDSL<T>()
    dsl.invoke(adapterDSL)
    when(adapterDSL.loadType){
        AdapterLoadType.Page->{
            setAdapterEmptyOrListPage(
                adapterDSL.data,
                adapterDSL.emptyImage,
                adapterDSL.emptyText,
                adapterDSL.emptyLayout,
                adapterDSL.marginTop,
                adapterDSL.marginBottom,
                adapterDSL.emptyTextColor)
        }
        AdapterLoadType.Offset->{
            setAdapterEmptyOrListOffset(
                adapterDSL.data,
                adapterDSL.emptyImage,
                adapterDSL.emptyText,
                adapterDSL.emptyLayout,
                adapterDSL.marginTop,
                adapterDSL.marginBottom,
                adapterDSL.emptyTextColor)
        }
    }


}