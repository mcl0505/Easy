package com.mh55.easy.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.imyyq.mvvm.base.IActivityResult
import com.mh55.easy.R
import com.mh55.easy.ext.getIntentByMapOrBundle
import com.mh55.easy.ext.getString
import com.mh55.easy.mvvm.BaseViewModel
import com.mh55.easy.mvvm.intent.BaseViewIntent
import com.mh55.easy.ui.ILoading
import com.mh55.easy.ui.IView
import com.mh55.easy.ui.dialog.LoadingDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * 公司名称：~漫漫人生路~总得错几步~
 * 创建作者：Android 孟从伦
 * 创建时间：2022/10/5
 * 功能描述：Fragment 基类封装使用
 */

abstract class AbsFragment<V : ViewBinding, VM : BaseViewModel>(
    private val sharedViewModel: Boolean = false,
) : Fragment(), IView<V, VM>, IActivityResult, ILoading {

    //当前界面 标识
    open val TAG: String get() = this::class.java.simpleName
    protected lateinit var mContext: Context
    protected lateinit var mActivity: AppCompatActivity
    protected lateinit var mViewModel: VM
    private lateinit var mStartActivityForResult: ActivityResultLauncher<Intent>
    //加载框
    private var mLoadingDialog: LoadingDialog? = null

    protected val mBinding: V by lazy {
        var type = javaClass.genericSuperclass
        var vbClass: Class<V> = (type as ParameterizedType).actualTypeArguments[0] as Class<V>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(this, layoutInflater) as V
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
//        initBinding(layoutInflater, null)?.let {
//            mBinding = it
//        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndViewModel()

        initParam()
        initBaseLiveData()
        main(savedInstanceState)
        initViewObservable()
    }

    /**
     * 显示加载框
     */
    override fun showLoading() {
        showLoading(R.string.loading_msg.getString())
    }

    /**
     * 显示加载框
     * @param msg 加载提示文字
     */
    override fun showLoading(msg: String) {
        mLoadingDialog = LoadingDialog(mContext, msg)
        mLoadingDialog?.showDialog()
    }

    /**
     * 取消加载框
     */
    override fun dismissLoading() {
        mLoadingDialog?.dismissDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = (activity as AppCompatActivity?)!!
    }

    override fun initViewAndViewModel() {
        mViewModel = if (sharedViewModel) {
            initViewModel(requireActivity())
        } else {
            initViewModel(this)
        }

        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(mViewModel)
        initStartActivityForResult()
    }

    //必须先在OnCreate 中注册
    private fun initStartActivityForResult() {
        if (!this::mStartActivityForResult.isInitialized) {
            mStartActivityForResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    val data = it.data ?: Intent()
                    when (it.resultCode) {
                        Activity.RESULT_OK -> {
                            onActivityResultOk(data)
                            if (this::mViewModel.isInitialized) {
                                mViewModel.onActivityResultOk(data)
                            }
                        }
                        Activity.RESULT_CANCELED -> {
                            onActivityResultCanceled(data)
                            if (this::mViewModel.isInitialized) {
                                mViewModel.onActivityResultCanceled(data)
                            }
                        }
                        else -> {
                            onActivityResult(it.resultCode, data)
                            if (this::mViewModel.isInitialized) {
                                mViewModel.onActivityResult(it.resultCode, data)
                            }
                        }
                    }
                }
        }
    }

    override fun startActivity(
        clazz: Class<out Activity>,
        map: MutableMap<String, *>?,
        bundle: Bundle?
    ) {
        mViewModel.mUiChangeLiveData.postValue(BaseViewIntent.startActivity(clazz, map, bundle))
    }

    override fun startActivityForResult(
        clazz: Class<out Activity>,
        map: MutableMap<String, *>?,
        bundle: Bundle?
    ) {
        mViewModel.mUiChangeLiveData.postValue(
            BaseViewIntent.startActivityForResult(
                clazz,
                map,
                bundle
            )
        )
    }

    override fun finish(resultCode: Int?, map: MutableMap<String, *>?, bundle: Bundle?) {
        mViewModel.mUiChangeLiveData.postValue(BaseViewIntent.finish(resultCode, map, bundle))
    }

    override fun setResult(
        resultCode: Int,
        map: MutableMap<String, *>?,
        bundle: Bundle?,
        data: Intent?
    ) {
        mViewModel.mUiChangeLiveData.postValue(
            BaseViewIntent.setResult(
                resultCode,
                map,
                bundle,
                data
            )
        )
    }

    override fun initBaseLiveData() {
        mViewModel.mUiChangeLiveData.observe(this) {
            it?.let {
                when (it) {
                    is BaseViewIntent.finish -> {
                        if (it.resultCode != null) {
                            mActivity.setResult(
                                it.resultCode,
                                getIntentByMapOrBundle(mContext, null, it.map, it.bundle)
                            )
                        }

                        mActivity.finish()
                    }
                    is BaseViewIntent.startActivity -> {
                        startActivity(getIntentByMapOrBundle(mContext, it.clazz, it.map, it.bundle))
                    }
                    is BaseViewIntent.startActivityForResult -> {

                        mStartActivityForResult.launch(
                            getIntentByMapOrBundle(
                                mContext,
                                it.clazz,
                                it.map,
                                it.bundle
                            )
                        )
                    }
                    is BaseViewIntent.setResult -> {
                        if (it.data == null) {
                            val intent = getIntentByMapOrBundle(mContext, null, it.map, it.bundle)
                            mActivity.setResult(it.resultCode, intent)
                        } else {
                            mActivity.setResult(it.resultCode, it.data)
                        }

                    }

                    is BaseViewIntent.showLoading -> {
                        if (it.isShow) {
                            showLoading(it.showMsg)
                        } else dismissLoading()
                    }
                }
            }

        }
    }

    override fun getBundle(): Bundle? = arguments

    override fun onDestroy() {
        super.onDestroy()

        // 界面销毁时移除 vm 的生命周期感知
        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }
        removeLiveDataBus(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 通过反射，解决内存泄露问题
        GlobalScope.launch {
            var clz: Class<*>? = this@AbsFragment.javaClass
            while (clz != null) {
                // 找到 mBinding 所在的类
                if (clz == AbsFragment::class.java) {
                    try {
                        val field = clz.getDeclaredField("mBinding")
                        field.isAccessible = true
                        field.set(this@AbsFragment, null)
                    } catch (ignore: Exception) {
                    }
                }
                clz = clz.superclass
            }
        }
    }

}