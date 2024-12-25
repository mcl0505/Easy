package com.mh55.easy.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.imyyq.mvvm.base.IActivityResult
import com.imyyq.mvvm.base.IArgumentsFromIntent
import com.mh55.easy.manager.AppManager
import com.mh55.easy.app.ConfigBuilder
import com.mh55.easy.R
import com.mh55.easy.ext.*
import com.mh55.easy.mvvm.BaseViewModel
import com.mh55.easy.mvvm.intent.BaseViewIntent
import com.mh55.easy.ui.ILoading
import com.mh55.easy.ui.IView
import com.mh55.easy.ui.dialog.LoadingDialog
import com.mh55.easy.utils.StatusBarUtils
import com.mh55.easy.widget.title.TitleBar
import java.lang.reflect.ParameterizedType

abstract class AbsActivity<V : ViewDataBinding, VM : BaseViewModel> :
    AppCompatActivity(), IView<V, VM>, IActivityResult, IArgumentsFromIntent, ILoading {
    //Activity 标识
    open val TAG: String get() = this::class.java.simpleName
    protected lateinit var mContext: Context
    protected val mBinding: V by lazy {
        var type = javaClass.genericSuperclass
        var vbClass: Class<V> = (type as ParameterizedType).actualTypeArguments[0] as Class<V>
        val method = vbClass.getMethod("inflate", LayoutInflater::class.java)
        method.isAccessible = true
        method.invoke(this, layoutInflater) as V
    }
    protected lateinit var mViewModel: VM
    protected lateinit var mViewContent: FrameLayout
    lateinit var tagBtn:TextView

    //标题
    protected lateinit var mTitlebar: TitleBar

    //加载框
    private var mLoadingDialog: LoadingDialog? = null
    private lateinit var mStartActivityForResult: ActivityResultLauncher<Intent>

    //true=黑色  false=白色
    open val isDark get() = true

    //true=禁用重力感应  false=启用重力感应
    open val isNoSensor get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.add(this)
        mContext = this
        //app 全局置灰处理
        window.decorView.changeGray(ConfigBuilder.isGray)
        //全屏 透明状态栏
        StatusBarUtils.setStatusBarTransparent(this)
        //设置默认状态栏字体为黑色
        StatusBarUtils.setStateBarTextColor(this, isDark)
        if(isNoSensor) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        }
        setContentView(R.layout.activity_root)
        mViewContent = findViewById(R.id.frame_content)
        tagBtn = findViewById<TextView>(R.id.tagBtn)
        mViewContent.addView(mBinding.root)


        initStartActivityForResult()
        //初始化ViewModel
        initViewAndViewModel()
        //初始化参数
        initParam()
        //初始化标题
        initTitle()
        //基础业务逻辑
        main(savedInstanceState)
        //事件观察
        initViewObservable()
        //基础事件观察
        initBaseLiveData()
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

    private fun initTitle() {
        mTitlebar = findViewById(R.id.titleBar)
        mTitlebar.apply {
            titleLine.visibleOrGone(false)
            visibleOrGone(!setTitleText().isNullOrEmpty())
            tvTitleCenter.text = setTitleText()
            imgTitleLeft.singleClick { finish() }
        }
    }

    /**
     * 默认不显示标题
     */
    open fun setTitleText(): String = ""

    override fun initBaseLiveData() {
        mViewModel.mUiChangeLiveData.observe(this) {
            when (it) {
                is BaseViewIntent.finish -> {
                    if (it.resultCode != null) {
                        setResult(
                            it.resultCode,
                            getIntentByMapOrBundle(this, null, it.map, it.bundle)
                        )
                    }

                    finish()
                }

                is BaseViewIntent.startActivity -> {
                    startActivity(getIntentByMapOrBundle(this, it.clazz, it.map, it.bundle))
                }

                is BaseViewIntent.startActivityForResult -> {
                    mStartActivityForResult.launch(
                        getIntentByMapOrBundle(
                            this,
                            it.clazz,
                            it.map,
                            it.bundle
                        )
                    )
                }

                is BaseViewIntent.setResult -> {
                    if (it.data == null) {
                        val intent = getIntentByMapOrBundle(this, null, it.map, it.bundle)
                        setResult(it.resultCode, intent)
                    } else {
                        setResult(it.resultCode, it.data)
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

    override fun initViewAndViewModel() {
        mViewModel = initViewModel(this)
        mViewModel.mIntent = getArgumentsIntent()
        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(mViewModel)

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

    final override fun getBundle(): Bundle? {
        return intent.extras
    }

    final override fun getArgumentsIntent(): Intent? {
        return intent
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

    /**
     * APP字体大小，不随系统的字体大小变化而变化
     */
    override fun getResources(): Resources? {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    /**
     * 点击键盘外部隐藏键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            //当键盘弹出是，点击键盘之外的区域隐藏键盘
            val view = currentFocus
            hideKeyboard(ev, view)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun hideKeyboard(event: MotionEvent, view: View?) {
        try {
            if (view != null && view is EditText) {
                val location = intArrayOf(0, 0)
                view.getLocationInWindow(location)
                val left = location[0]
                val top = location[1]
                val right = (left
                        + view.getWidth())
                val bootom = top + view.getHeight()
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.rawX < left || event.rawX > right || event.y < top || event.rawY > bootom
                ) {
                    if (view == null) return
                    val inputMethodManager = view.getContext()
                        .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view.getWindowToken(), 0)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 退出app 操作
     */
    private var lastTime: Long = 0
    private val intervalTime = 1000 * 2.toLong()
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ("MainActivity" == TAG) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTime < intervalTime) {
                    AppManager.appExit()
                } else {
                    lastTime = currentTime
                    R.string.app_exit.toast()
                    return false
                }
            } else {
                return super.onKeyDown(keyCode, event)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 界面销毁时移除 vm 的生命周期感知
        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }

    }
}