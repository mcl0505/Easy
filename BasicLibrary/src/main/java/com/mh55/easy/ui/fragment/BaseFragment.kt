package com.mh55.easy.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.mh55.easy.ext.bindingInflate
import com.mh55.easy.ext.createBinding
import com.mh55.easy.mvvm.BaseViewModel

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel>(
    sharedViewModel: Boolean = false
) : AbsFragment<V, VM>(sharedViewModel) {

    final override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V? = null

    final override fun initViewAndViewModel() {
        super.initViewAndViewModel()
        // 让 LiveData 和 xml 可以双向绑定
        mBinding.lifecycleOwner = this
    }

    override fun onDestroyView() {
        mBinding.unbind()
        super.onDestroyView()
    }
}