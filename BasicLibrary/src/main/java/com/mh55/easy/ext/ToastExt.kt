package com.mh55.easy.ext

import android.graphics.drawable.Drawable
import com.mh55.easy.utils.ToastUtil

fun Int.toast(img:Drawable?=null){
    ToastUtil.toast(this.getString(),img)
}

fun String.toast(img:Drawable?=null){
    ToastUtil.toast(this,img)
}
fun CharSequence.toast(img:Drawable?=null){
    ToastUtil.toast(this.toString(),img)
}