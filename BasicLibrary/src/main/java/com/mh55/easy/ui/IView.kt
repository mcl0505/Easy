package com.mh55.easy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.mh55.easy.manager.AppManager
import com.mh55.easy.mvvm.BaseViewModel
import com.mh55.easy.utils.bus.LiveDataBus
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * V 层，这里的视图都是 Activity 或 Fragment
 */
interface IView<V : ViewBinding, VM : BaseViewModel> : IArgumentsFromBundle {
    /**
     * 1.
     * 初始化外部传进来的参数
     */
    fun initParam() {}

    /**
     * 2.
     * 初始化界面观察者
     */
    fun initViewObservable() {}

    /**
     * 3.
     * 初始化数据
     */
    fun main(savedInstanceState: Bundle?)


    /**
     * 初始化 DataBinding，基类应该在初始化后设为 final
     */
    fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V?

    /**
     * 初始化视图和 VM
     */
    fun initViewAndViewModel()

    /**
     * 初始化通用的 UI 改变事件，基类应该在初始化后设为 final
     */
    fun initBaseLiveData()

    /**
     * 移除事件总线监听，避免内存泄露
     */
    fun removeLiveDataBus(owner: LifecycleOwner) {
        LiveDataBus.removeObserve(owner)
        LiveDataBus.removeStickyObserver(this)
    }

    /**
     * 每个视图肯定会持有一个 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    fun initViewModel(viewModelStoreOwner: ViewModelStoreOwner): VM {
        var modelClass: Class<VM>?
        val type: Type? = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as? Class<VM>
        } else null
        if (modelClass == null) {
            modelClass = BaseViewModel::class.java as Class<VM>
        }
        //如果没有指定泛型参数，则默认使用BaseViewModel
        BaseViewModel::class.java
        val vm = ViewModelProvider(
            viewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory(AppManager.getApplication())
        ).get(modelClass)
        // 让 vm 也可以直接获取到 bundle
        vm.mBundle = getBundle()
        return vm
    }
}