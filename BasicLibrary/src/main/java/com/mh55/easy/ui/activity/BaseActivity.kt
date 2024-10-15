package com.mh55.easy.ui.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.mh55.easy.ext.createBinding
import com.mh55.easy.mvvm.BaseViewModel

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel>() : AbsActivity<V, VM>() {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V? = null


    @SuppressLint("MissingSuperCall")
    final override fun initViewAndViewModel() {
        super.initViewAndViewModel()
        // 让 LiveData 和 xml 可以双向绑定
        mBinding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }
}