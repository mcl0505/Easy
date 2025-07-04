package com.mh55.easy.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.animation.Animation
import android.view.animation.Animation.INFINITE
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.mh55.easy.R

/**
 * 加载等待框
 */
class LoadingDialog(context: Context,val msg:String =  "加载中") : Dialog(
    context,
    R.style.LoadingDialog
){
    init {
        setContentView(R.layout.dialog_loading_view)
    }

    fun showDialog(activity: Activity) {

        if (!activity.isFinishing && !activity.isDestroyed) {
            try {
                if (!isShowing) {
                    show()
                    val imageView: ImageView = findViewById(R.id.iv_image)
                    val textView: TextView = findViewById(R.id.message)
                    val animation: Animation = RotateAnimation(
                        0f,
                        360f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                    )
                    animation.duration = 2000
                    animation.repeatCount = INFINITE
                    animation.fillAfter = true
                    imageView.startAnimation(animation)
                    textView.text = msg
                }
            } catch (e: Exception) {
                Log.e("LoadingDialog", "显示对话框失败: ${e.message}")
            }
        }

    }

    fun dismissDialog() {
        try {
            if (isShowing && window?.decorView?.windowToken != null) {
                dismiss()
            }
        } catch (e: Exception) {
            Log.e("LoadingDialog", "关闭对话框失败: ${e.message}")
        }
    }
}