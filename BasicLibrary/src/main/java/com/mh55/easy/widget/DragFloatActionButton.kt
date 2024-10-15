package com.mh55.easy.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs
import kotlin.math.max

class DragFloatActionButton : AppCompatImageView {
    private var parentHeight = 0
    private var parentWidth = 0
    private var lastX = 0
    private var lastY = 0
    private var parent: ViewGroup? = null
    private var onClickListener: OnClickListener? = null
    /**是否自动吸附两侧**/
    var isAdsorbent = false
    /**按钮点击事件拦截**/
    lateinit var onFloastClick:()->Unit

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    private var downX = 0
    private var downY = 0
    private var upX = 0
    private var upY = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rawX = event.rawX.toInt()
        val rawY = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val viewParent = getParent()
                run {
                    lastX = rawX
                    downX = lastX
                }
                run {
                    lastY = rawY
                    downY = lastY
                }
                if (viewParent != null) {
                    //请求父控件不中断事件
                    viewParent.requestDisallowInterceptTouchEvent(true)
                    parent = viewParent as ViewGroup
                    //获取父控件的高度
                    parentHeight = parent!!.height
                    //获取父控件的宽度
                    parentWidth = parent!!.width
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = rawX - lastX
                val dy = rawY - lastY
                var x = x + dx
                var y = y + dy
                //检测是否到达边缘 左上右下
                x = if (x < 0) 0f else if (x > (parentWidth - width)) (parentWidth - width).toFloat() else x
                //控件距离底部的margin
                val bottomMargin = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    60f,
                    resources.displayMetrics
                ).toDouble()
                y = if (y < 0) 0f else (if (y > parentHeight - height - bottomMargin) parentHeight - height - bottomMargin else y).toFloat()
                setX(x)
                setY(y)
                lastX = rawX
                lastY = rawY
            }

            MotionEvent.ACTION_UP -> {
                upX = event.rawX.toInt()
                upY = event.rawY.toInt()
                val distanceX = abs((abs(upX.toDouble()) - abs(downX.toDouble())).toDouble())
                    .toInt()
                val distanceY = abs((abs(upY.toDouble()) - abs(downY.toDouble())).toDouble())
                    .toInt()
                //当手指按下的事件跟手指抬起事件之间的距离小于10时执行点击事件
                if (max(distanceX.toDouble(), distanceY.toDouble()) <= 10) {
                    if (::onFloastClick.isInitialized)onFloastClick.invoke()
                }

                if (isAdsorbent){
                    moveHide(rawX)
                }

            }
        }
        //如果是拖拽则消s耗事件，否则正常传递即可。
        return true
    }

    private fun moveHide(rawX: Int) {
        if (rawX >= parentWidth / 2) {

            //靠右吸附
            animate().setInterpolator(DecelerateInterpolator())
                .setDuration(500)
                .xBy(parentWidth - width - x)
                .start()
        } else {
            //靠左吸附
            val oa = ObjectAnimator.ofFloat(this, "x", x, 0f)
            oa.interpolator = DecelerateInterpolator()
            oa.setDuration(500)
            oa.start()
        }
    }
}
