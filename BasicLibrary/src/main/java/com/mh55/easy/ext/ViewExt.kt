package com.mh55.easy.ext

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.SystemClock
import android.os.VibratorManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mh55.easy.R

//防止快速点击造成打开多个界面   只允许在 1秒内只能点击一次  single(2000){}   可自定义时间
/**
 * 防止快速点击造成打开多个界面
 */
fun <T : View> T.singleClick(time: Int = 500, block: (T) -> Unit) {
    this.setOnClickListener {
        val curClickTime = SystemClock.uptimeMillis()
        val lastClickTime = (it.getTag(R.id.singleClickId) as? Long) ?: 0
        // 超过点击间隔后再将lastClickTime重置为当前点击时间
        it.setTag(R.id.singleClickId, lastClickTime)
        if (curClickTime - lastClickTime >= time) {
            block(it as T)
        }
    }
}


/**
 * view 显示隐藏
 * @param show true:显示  false:不显示
 */
fun View.visibleOrGone(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}




/**
 * view 显示与占位
 * @param show true:显示  false:不显示但是占位
 */
fun View.visibleOrInvisible(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

/**
 * 布局置灰处理
 * @param isGray true:正常  false:灰色
 */
fun View.changeGray(isGray:Boolean){
    //全局置灰处理
    val paint = Paint()
    val cm = ColorMatrix()
    cm.setSaturation(if (isGray) 0f else 1f)
    paint.colorFilter = ColorMatrixColorFilter(cm)
    this.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
}


fun TabLayout.withViewPager2(activity: AppCompatActivity,viewPager2: ViewPager2,fragmentList:MutableList<Fragment>,tabList: MutableList<String>){
    viewPager2.adapter = object : FragmentStateAdapter(activity){
        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(p0: Int): Fragment  = fragmentList[p0]

    }

    viewPager2.offscreenPageLimit = 1

    TabLayoutMediator(this,viewPager2){ tab, position ->
        tab.text = tabList[position]
    }.attach()
}