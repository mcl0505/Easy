package com.mh55.easy.mvvm.live_data

import androidx.lifecycle.MutableLiveData

/**
 * 公司名称：~漫漫人生路~总得错几步~
 * 创建作者：Android 孟从伦
 * 创建时间：2022/10/3
 * 功能描述：自定义的Boolean类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */

class BooleanLiveData : MutableLiveData<Boolean>() {

    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}