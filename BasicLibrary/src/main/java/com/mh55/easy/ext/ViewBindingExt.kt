package com.mh55.easy.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/1/20
 * 功能描述：ViewBinding试图绑定扩展函数
 */

fun <T : ViewDataBinding> AppCompatActivity.bindingInflate(inflater: LayoutInflater = layoutInflater, @LayoutRes resId: Int, viewGroup: ViewGroup? = null): T =
    DataBindingUtil.inflate<T>(inflater, resId,viewGroup,false).apply {
        lifecycleOwner = this@bindingInflate
    }



inline fun <T : ViewDataBinding> Fragment.bindingInflate(inflater: LayoutInflater = layoutInflater,@LayoutRes resId: Int,viewGroup: ViewGroup?): T =
    DataBindingUtil.inflate<T>(inflater, resId,viewGroup,false).apply {
        lifecycleOwner = this@bindingInflate
    }


/**
 * 通过反射的方式获ViewDataBinding
 */
//fun <VDB:ViewDataBinding> AppCompatActivity.createBinding(vdbIndex:Int = 0) : VDB?{
//    var mBinding:VDB?=null
//    val vbClass: Class<VDB> = getVDBClass(vdbIndex)
//    try {
//        if (vbClass == null) {
//            return throw NullPointerException()
//        }
//        val method: Method = vbClass!!.getMethod("inflate", LayoutInflater::class.java)
//        mBinding = method.invoke(null, layoutInflater) as VDB
//        //让 LiveData 和 xml 可以双向绑定
//        mBinding.lifecycleOwner = this
//
//    } catch (e: NoSuchMethodException) {
//        e.printStackTrace()
//    } catch (e: InvocationTargetException) {
//        e.printStackTrace()
//    } catch (e: IllegalAccessException) {
//        e.printStackTrace()
//    }
//
//    return mBinding
//}
private fun <VDB:ViewDataBinding> AppCompatActivity.getVDBClass(index:Int): Class<VDB> {
    val type = javaClass.genericSuperclass as ParameterizedType
    return type.actualTypeArguments[index] as Class<VDB>
}

/**
 * 通过反射的方式获ViewDataBinding
 */
fun <VDB:ViewDataBinding> Fragment.createBinding(vdbIndex:Int = 0,inflater: LayoutInflater, container: ViewGroup?) : VDB{
    val vbClass = getVDBClass<VDB>(vdbIndex)
    val method = vbClass.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    return method.invoke(null, inflater, container, false) as VDB
}

/**
 * 通过反射的方式获ViewDataBinding
 */
fun <VDB:ViewDataBinding> AppCompatActivity.createBinding(vdbIndex:Int = 0,inflater: LayoutInflater, container: ViewGroup?) : VDB{
    val vbClass = getVDBClass<VDB>(vdbIndex)
    val method = vbClass.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    return method.invoke(null, inflater, container, false) as VDB
}
private fun <VDB:ViewDataBinding> Fragment.getVDBClass(index:Int): Class<VDB> {
    val type = javaClass.genericSuperclass as ParameterizedType
    return type.actualTypeArguments[index] as Class<VDB>
}