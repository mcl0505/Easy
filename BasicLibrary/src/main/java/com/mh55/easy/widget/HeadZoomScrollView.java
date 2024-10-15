package com.mh55.easy.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;

public class HeadZoomScrollView extends NestedScrollView {

    public HeadZoomScrollView(Context context) {
        super(context);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //用于记录下拉位置
    private float y = 0f;
    //zoomView原本的宽高
    private int zoomViewWidth = 0;
    private int zoomViewHeight = 0;

    //是否正在放大
    private boolean mScaling = false;

    //放大的view，默认为第一个子view
    private View zoomView;

    public void setZoomView(View zoomView) {
        this.zoomView = zoomView;
    }

    //滑动放大系数，系数越大，滑动时放大程度越大
    private float mScaleRatio = 0.2f;

    public void setmScaleRatio(float mScaleRatio) {
        this.mScaleRatio = mScaleRatio;
    }

    //最大的放大倍数
    private float mScaleTimes = 1.2f;

    public void setmScaleTimes(int mScaleTimes) {
        this.mScaleTimes = mScaleTimes;
    }

    //回弹时间系数，系数越小，回弹越快
    private float mReplyRatio = 0.5f;

    public void setmReplyRatio(float mReplyRatio) {
        this.mReplyRatio = mReplyRatio;
    }

    //滚动触发子view横向放大的方法
    private float moveDistance = 300.0f;

    public void setMoveDistance(float moveDistance) {
        this.moveDistance = moveDistance;
    }

    //是否横向拉伸，默认为true
    private boolean isTransverse = false;

    public void setTransverse(boolean isTransverse) {
        this.isTransverse = isTransverse;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //不可过度滚动，否则上移后下拉会出现部分空白的情况
        setOverScrollMode(OVER_SCROLL_NEVER);
        //获得默认第一个view
        if (getChildAt(0) != null && getChildAt(0) instanceof ViewGroup && zoomView == null) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            if (vg.getChildCount() > 0) {
                zoomView = vg.getChildAt(0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            zoomViewWidth = zoomView.getMeasuredWidth();
            zoomViewHeight = zoomView.getMeasuredHeight();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //手指离开后恢复图片
                mScaling = false;
                replyImage();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScaling) {
                    if (getScrollY() == 0) {
                        y = ev.getY();// 滚动到顶部时记录位置，否则正常返回
                    } else {
                        break;
                    }
                }
                int distance = (int) ((ev.getY() - y) * mScaleRatio); // 滚动距离乘以一个系数
                if (distance < 0) { // 当前位置比记录位置要小，正常返回
                    break;
                }
                // 处理放大
                mScaling = true;
                setZoom(distance);
                return true; // 返回true表示已经完成触摸事件，不再处理
        }
        return super.onTouchEvent(ev);
    }

    //拉伸效果
    public void setZoom(float zoom) {
        if (zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            return;
        }
        ViewGroup.LayoutParams lp = zoomView.getLayoutParams();
        if (isTransverse) {
            if (zoom <= moveDistance) {
                lp.width = (int) (zoomViewWidth);
            } else {
                lp.width = (int) (zoomViewWidth + zoom);
                if (onZoomListener != null) {
                    onZoomListener.onZoom(zoom);
                }
            }
        } else {
            lp.width = (int) (zoomViewWidth);
        }
        lp.height = (int) (zoomViewHeight * ((zoomViewWidth + zoom) / zoomViewWidth));
        ((MarginLayoutParams) lp).setMargins(-(lp.width - zoomViewWidth) / 2, 0, 0, 0);
        zoomView.setLayoutParams(lp);
    }

    private OnZoomListener onZoomListener;

    public interface OnZoomListener {
        void onZoom(float zoom);
    }

    public void setOnZoomListener(OnZoomListener onZoomListener){
        this.onZoomListener = onZoomListener;
    }

    //回弹动画
    private void replyImage() {
        float distance = 0f;
        if(isTransverse){
            distance = zoomView.getMeasuredWidth() - zoomViewWidth;
        }else{
            distance = zoomView.getMeasuredHeight() - zoomViewHeight;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(distance, 0f).setDuration((long) (distance * mReplyRatio));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setZoom((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

}

