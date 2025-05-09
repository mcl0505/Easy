package com.mh55.easy.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.mh55.easy.mvvm.intent.BaseViewIntent
import com.mh55.easy.ui.activity.AbsActivity
import com.mh55.easy.utils.LogUtil
import java.lang.ref.WeakReference
import java.util.*
import kotlin.system.exitProcess

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/2/19
 * 功能描述：
 */
/**
 * UI管理类,任何地方可以安全退出程序
 */
@Suppress("unused")
object AppManager {
    /** [Application] 对象 */
    private var mApplication: Application? = null

    /** 保存 [Activity] 对象的堆 */
    private val activityStack: Stack<WeakReference<Activity>> = Stack()

    /**
     * 忽略列表
     * > 列表中的 [Activity] 对象不会被纳入管理
     */
    private val ignoreActivities = arrayListOf<Class<out Activity>>()

    /** 前台界面数量 */
    private var foregroundCount = 0

    /** App 前后台切换回调 */
    private var mAppForegroundStatusChangeCallback: ((Boolean) -> Unit)? = null

    /** Activity 生命周期回调接口*/
    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, p1: Bundle?) {
            onCreate(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            if (foregroundCount == 0) {
                // App 回到前台
                mAppForegroundStatusChangeCallback?.invoke(true)
            }
            foregroundCount++
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            foregroundCount--
            if (foregroundCount == 0) {
                // App 退到后台
                mAppForegroundStatusChangeCallback?.invoke(false)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            onDestroy(activity)
        }
    }

    /** 获取前台界面数量 */
    @JvmStatic
    fun getForegroundCount(): Int {
        return foregroundCount
    }

    /** 判断应用是否在前台 */
    @JvmStatic
    fun isForeground(): Boolean {
        val activityManager = getContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName = getContext().packageName
        // 获取Android设备中所有正在运行的App
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName == packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }

    /**
     * 将 [AppManager] 注册到 [application]
     * > 应用启动后自动调用
     */
    internal fun register(application: Application) {
        mApplication = application
        application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
    }

    /**
     * 设置 App 前后台状态切换监听[onChange]
     * > [onChange] 入参[Boolean] `true`回到前台 & `false`进入后台
     */
    @JvmStatic
    fun setOnAppForegroundStatusChangeListener(onChange: (Boolean) -> Unit) {
        mAppForegroundStatusChangeCallback = onChange
    }

    /**
     * 获取可用的 [Application] 对象
     * > [AppManager]已初始化，返回[mApplication]，否则通过反射[getApplicationByReflect]
     * > 获取当前[Application]对象，若获取失败，抛出异常[NullPointerException]
     */
    @JvmStatic
    @Throws(NullPointerException::class)
    fun getApplication(): Application {
        if (mApplication == null) {
            register(getApplicationByReflect())
            if (mApplication == null) {
                throw NullPointerException("Application must not be null! Please register AppManager in your Application start！")
            }
        }
        return mApplication!!
    }

    /**
     * 获取 [Context] 对象
     * > 优先获取栈顶的 [Activity] 对象，若没有，则返回 [Application] 对象
     */
    @JvmStatic
    fun getContext(): Context {
        return peekActivity() ?: getApplication()
    }

    /**
     * 通过反射获取当前 [Application] 对象
     * > 获取失败时抛出[NullPointerException]
     */
    @Throws(NullPointerException::class)
    private fun getApplicationByReflect(): Application {
        try {
            @SuppressLint("PrivateApi")
            val activityThread = Class.forName("android.app.ActivityThread")
            val thread = activityThread.getMethod("currentActivityThread").invoke(null)
            val app = activityThread.getMethod("getApplication").invoke(thread)
                ?: throw NullPointerException("u should init first")
            return app as Application
        } catch (e: Exception) {
            LogUtil.e("AppManager", "getApplicationByReflect=$e")
        }
        throw NullPointerException("u should init first")
    }

    /**
     * 将[Activity]类对象列表[classArray]添加到忽略列表[ignoreActivities]
     * > 忽略列表[ignoreActivities]中的[Activity]类不会被[AppManager]管理
     */
    @JvmStatic
    fun addToIgnore(vararg classArray: Class<out Activity>) {
        ignoreActivities.addAll(classArray)
    }

    /** [Activity.onCreate] 时回调，将不在忽略列表[ignoreActivities]中的[activity]添加到管理堆[activityStack] */
    @JvmStatic
    private fun onCreate(activity: Activity?) {
        if (activity != null && ignoreActivities.contains(activity.javaClass)) {
            // 不管理在忽略列表中的 Activity
            return
        }
        add(activity)
    }

    /** [Activity.onDestroy] 时回调，将[activity]从管理堆[activityStack]中移除 */
    @JvmStatic
    private fun onDestroy(activity: Activity?) {
        remove(activity)
    }

    /** 判断当前堆[activityStack]中是否存在[clazz]对应的[Activity] */
    @JvmStatic
    fun contains(clazz: Class<out Activity>): Boolean {
        return activityStack.count { it.get()?.javaClass == clazz } > 0
    }

    /** 将[activity]添加到[activityStack] */
    @JvmStatic
    fun add(activity: Activity?) {
        if (activity == null) {
            return
        }
        activityStack.add(WeakReference(activity))
    }

    /** 将[activity]从[activityStack]中移除 */
    @JvmStatic
    fun remove(activity: Activity?) {
        if (activity == null) {
            return
        }
        val index = activityStack.indexOfFirst {
            it.get() == activity
        }
        if (index in 0 until activityStack.size) {
            activityStack.removeElementAt(index)
        }
    }

    /** 关闭[activityStack]中，除了[activities]以外的[Activity] */
    @JvmStatic
    fun finishAllWithout(vararg activities: Activity?) {
        if (activities.isEmpty()) {
            return
        }
        activities.forEach {
            remove(it)
        }
        finishAllActivity()
        activities.forEach {
            add(it)
        }
    }

    /** 关闭[activityStack]中，除了类型在[classArray]以外的[Activity] */
    @JvmStatic
    fun finishAllWithout(vararg classArray: Class<out Activity>) {
        if (classArray.isEmpty()) {
            return
        }
        val ls = arrayListOf<Activity>()
        classArray.forEach {
            val elements = getActivities(it)
            ls.addAll(elements)
        }
        ls.forEach {
            remove(it)
        }
        finishAllActivity()
        for (activity in ls) {
            add(activity)
        }
    }

    /** 关闭类对象为[clazz]的[Activity] */
    @JvmStatic
    fun finishActivity(clazz: Class<out Activity>) {
        getActivities(clazz).forEach { activity ->
            activity.finish()
        }
    }

    /** 关闭类型为[A]的[Activity] */
    inline fun <reified A : Activity> finishActivity() {
        finishActivity(A::class.java)
    }

    /** 关闭类型在[classArray]的[Activity] */
    @JvmStatic
    fun finishActivities(vararg classArray: Class<out Activity>) {
        for (clazz in classArray) {
            finishActivity(clazz)
        }
    }

    /** 获取[activityStack]堆顶的[Activity] */
    @JvmStatic
    fun peekActivity(): Activity? {
        return if (activityStack.isEmpty()) {
            null
        } else {
            activityStack.peek().get()
        }
    }

    /** 获取类对象为[clazz]的[Activity]列表[List] */
    @JvmStatic
    fun <A : Activity> getActivities(clazz: Class<out A>): List<A> {
        return activityStack.filter {
            it.get()?.javaClass == clazz
        }.mapNotNull {
            @Suppress("UNCHECKED_CAST")
            it.get() as? A
        }
    }

    /** 获取类型为[A]的[Activity]列表[List] */
    inline fun <reified A : Activity> getActivities(): List<A> {
        return getActivities(A::class.java)
    }

    /** 获取堆[activityStack]中[Activity]的数量 */
    @JvmStatic
    fun getStackSize(): Int {
        return activityStack.size
    }

    /** 关闭[activityStack]中的所有[Activity] */
    @JvmStatic
    fun finishAllActivity() {
        activityStack.forEach {
            it.get()?.finish()
        }
        activityStack.clear()
    }

    /** 退出应用程序 */
    @JvmStatic
    fun appExit() {
        try {
            val activityMgr = getContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.killBackgroundProcesses(getContext().packageName)
            finishAllActivity()
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun start(intent: Intent){
        peekActivity()!!.startActivity(intent)
    }

    fun start(cla: Class<*>){
        peekActivity()!!.startActivity(Intent(peekActivity(),cla))
    }

}