package com.mh55.easy.ext

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.mh55.easy.manager.AppManager

//获取颜色
fun Int.getColor() : Int = ContextCompat.getColor(AppManager.getContext(),this)
fun String.getColor() : Int = Color.parseColor(this)

fun Int.getDrawable() : Drawable? = ContextCompat.getDrawable(AppManager.getContext(),this)

fun Int.getString() : String = AppManager.getContext().resources.getString(this)

fun Int.getStringArray() : List<String> = AppManager.getContext().resources.getStringArray(this).toList() as MutableList<String>

/**动态修改颜色透明度**/
fun Int.adjustAlpha(alpha:Float) = Color.argb(Color.alpha(this*alpha.toInt()),Color.red(this),Color.green(this),Color.blue(this))