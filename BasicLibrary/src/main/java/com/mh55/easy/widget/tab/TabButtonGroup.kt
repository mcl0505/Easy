package com.mh55.easy.widget.tab

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import java.util.*

/**
 * 公司：
 * 作者：Android 孟从伦
 * 创建时间：2020/12/19
 * 功能描述：
 */

class TabButtonGroup @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private val mList: MutableList<TabButton>
    private lateinit var mViewPager: ViewPager2
    private var mCurPosition: Int
    private val mAnimator: ValueAnimator?
    private var mAnimView: View? = null
    private val mRunnable: Runnable
    lateinit var onTabSelectClick: (positon: Int) -> Unit
    override fun onFinishInflate() {
        super.onFinishInflate()
        var i = 0
        val count = childCount
        while (i < count) {
            val v = getChildAt(i)
            v.id = i
            v.setOnClickListener(this)
            mList.add(v as TabButton)
            i++
        }
    }

    override fun onClick(v: View) {
        val tag = v.id
        if (tag != null) { //            cancelAnim();

            val position = tag as Int
            if (position == mCurPosition) {
                return
            }
            mList[mCurPosition].setChecked(false)
            val tbn = mList[position]
            tbn.setChecked(true)
            mCurPosition = position
            mAnimView = tbn;
            mAnimator?.start();
            selectPosition(mCurPosition)
            postDelayed(mRunnable, 150)
            if (::onTabSelectClick.isInitialized) onTabSelectClick.invoke(position)
        }
    }

    fun setViewPager(viewPager: ViewPager2) {
        mViewPager = viewPager
    }

    fun cancelAnim() {
        mAnimator?.cancel()
    }

    init {
        mList = ArrayList()
        mCurPosition = 0
        mAnimator = ValueAnimator.ofFloat(0.5f, 1.0f, 1f)
        mAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            val v = animation.animatedValue as Float
            if (mAnimView != null) {
                mAnimView!!.setScaleX(v)
                mAnimView!!.setScaleY(v)
            }
        })
        mAnimator.duration = 300
        mAnimator.interpolator = AccelerateDecelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mAnimView = null
            }
        })
        mRunnable = Runnable {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(mCurPosition, false)
                cancelAnim()
            }
        }
    }

    fun setSelectClick(selectPosition: Int) {
        mList[selectPosition].setChecked(true)
        mList[selectPosition].performClick()
    }

    fun selectPosition(selectPosition: Int) {

    }
}


fun TabButtonGroup.withViewPager(
    context: AppCompatActivity,
    vp: ViewPager2,
    fragmentList: MutableList<Fragment>
) {
    vp.apply {
        //添加适配器
        adapter = object : FragmentStateAdapter(context) {
            override fun getItemCount(): Int = fragmentList.size

            override fun createFragment(position: Int): Fragment = fragmentList[position]
        }
        //禁止滑动
        isUserInputEnabled = false
        offscreenPageLimit = fragmentList.size
    }
    this.setViewPager(vp)
}