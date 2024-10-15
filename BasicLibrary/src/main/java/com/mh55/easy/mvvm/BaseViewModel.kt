package com.mh55.easy.mvvm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.imyyq.mvvm.base.IActivityResult
import com.imyyq.mvvm.base.IArgumentsFromIntent
import com.mh55.easy.R
import com.mh55.easy.ext.getString
import com.mh55.easy.mvvm.intent.BaseViewIntent
import com.mh55.easy.mvvm.live_data.SingleLiveEvent
import com.mh55.easy.ui.IArgumentsFromBundle
import com.mh55.easy.ui.ILoading

open class BaseViewModel : ViewModel(), DefaultLifecycleObserver, IActivityResult, IArgumentsFromBundle,
    IArgumentsFromIntent, ILoading {

    // TODO: 2022/5/31 界面跳转 =======================================================================================================
    internal var mBundle: Bundle? = null
    internal var mIntent: Intent? = null
    override fun getBundle(): Bundle? = mBundle
    override fun getArgumentsIntent(): Intent? = mIntent

    val mUiChangeLiveData = SingleLiveEvent<BaseViewIntent>()
    override fun startActivity(clazz: Class<out Activity>, map: MutableMap<String, *>?, bundle: Bundle?) {
        mUiChangeLiveData.postValue(BaseViewIntent.startActivity(clazz, map, bundle))
    }
    override fun startActivityForResult(clazz: Class<out Activity>, map: MutableMap<String, *>?, bundle: Bundle?) {
        mUiChangeLiveData.postValue(BaseViewIntent.startActivityForResult(clazz, map, bundle))
    }
    override fun setResult(resultCode: Int, map: MutableMap<String, *>?, bundle: Bundle?, data: Intent?) {
        mUiChangeLiveData.postValue(BaseViewIntent.setResult(resultCode, map, bundle, data))
    }
    override fun finish(resultCode: Int?, map: MutableMap<String, *>?, bundle: Bundle?) {
        mUiChangeLiveData.postValue(BaseViewIntent.finish(resultCode, map, bundle))
    }

    override fun onCleared() {
        super.onCleared()
        mUiChangeLiveData.call()
    }

    override fun showLoading() {
        showLoading(R.string.loading_msg.getString())
    }

    override fun showLoading(msg: String) {
        mUiChangeLiveData.postValue(BaseViewIntent.showLoading(true,msg))
    }

    override fun dismissLoading() {
        mUiChangeLiveData.postValue(BaseViewIntent.showLoading(false,R.string.loading_msg.getString()))
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        onCleared()
    }

}