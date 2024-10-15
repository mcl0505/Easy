package com.mh55.easy.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.mh55.easy.ext.bindingInflate
import com.mh55.easy.R
import com.mh55.easy.ext.getVmClazz
import com.mh55.easy.manager.AppManager
import com.mh55.easy.mvvm.BaseViewModel
import java.lang.reflect.ParameterizedType

open class BaseDialog<DB : ViewDataBinding,VM : BaseViewModel,> : DialogFragment() {
    @JvmField
    protected var TAG = this.javaClass.simpleName

    lateinit var onViewClick:(mView:DB)->Unit

    @JvmField
    protected var mContext: Context? = null
    val mDialogBinding: DB by lazy {
        var type = javaClass.genericSuperclass
        var vbClass: Class<DB> = (type as ParameterizedType).actualTypeArguments[0] as Class<DB>
        val method = vbClass.getMethod("inflate", LayoutInflater::class.java)
        method.isAccessible = true
        method.invoke(this, layoutInflater) as DB
    }
    val mViewModel: VM by lazy { createViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,getDialogStyle())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = activity
        dialog?.let {
            it.setCancelable(canCancel())
            it.setCanceledOnTouchOutside(canCancel())
            //禁止返回关闭弹框可在此处处理
            if (!canCancel()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    it.setOnKeyListener(object : DialogInterface.OnKeyListener  {
                        override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                            if (keyCode == KeyEvent.KEYCODE_BACK) return true
                            return false
                        }
                    });
                }
            }
        }
        mDialogBinding.lifecycleOwner = this
        main(savedInstanceState)
        return mDialogBinding.root
    }

    //点击手机返回
    fun onKeyBack(){}

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window!!.setGravity(setGravity())
        window.setWindowAnimations(getDialogAnim())
        window.setLayout(setWidth(), setHeight())
    }

    open fun main(savedInstanceState: Bundle?){}

    open fun getDialogStyle(): Int = R.style.dialog_style

    protected open fun setWidth(): Int {
        return LinearLayout.LayoutParams.MATCH_PARENT
    }

    protected open fun setHeight(): Int {
        return LinearLayout.LayoutParams.WRAP_CONTENT
    }

    protected open fun setGravity(): Int {
        return Gravity.CENTER
    }

    /**
     * 弹框进入与消失动画
     */
    private fun getDialogAnim(): Int = when(setGravity()){
        Gravity.CENTER->R.style.dialogAnimCenter
        Gravity.BOTTOM->R.style.dialogAnimBottom
        Gravity.LEFT->R.style.dialogAnimLeft
        Gravity.RIGHT->R.style.dialogAnimRight
        else-> R.style.dialogAnimBottom
    }

    protected open fun canCancel(): Boolean {
        return true
    }

    open fun showDialog(){
        showDialog(AppManager.peekActivity() as AppCompatActivity)
    }

    open fun showDialog(activity:AppCompatActivity){
        kotlin.runCatching {
            activity.supportFragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
//            super.show(manager!!, TAG)
            activity.supportFragmentManager?.beginTransaction()?.add(this,TAG)?.commitAllowingStateLoss()
        }
    }

    open fun show(manager: FragmentManager?) {
        kotlin.runCatching {
            manager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
//            super.show(manager!!, TAG)
            manager?.beginTransaction()?.add(this,TAG)?.commitAllowingStateLoss()
        }

    }


    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(1,this)]
    }

    open fun isDialogShow(activity:AppCompatActivity) = activity.supportFragmentManager.findFragmentByTag(TAG)!=null
    open fun isDialogShow(manager: FragmentManager?) = manager?.findFragmentByTag(TAG)!=null
}