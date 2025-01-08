package com.mh55.easy.utils

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller

/**
 * @createTime =>  2025/1/7 15:18
 * @class =>  EasyCore.kt
 * @desciption =>  模块中的业务简化使用
 **/
object EasyCore {
    /**
     *  启动Activity
     * @param activityName Activity 名称 不可为null
     * @return param 参数 可为null
     * @createtime 2025/1/7 15:13
     **/
    fun startActivity(activityName: String, param: Bundle? = null) {
        ActivityRouter.startActivity(null, activityName, param, -1)
    }

    /**
     *  启动Activity
     * @param starter  当前Activity  或者 Fragment   Context
     * @param activityName Activity 名称 不可为null
     * @param param 参数 可为null
     * @createtime 2025/1/7 15:13
     **/
    fun startActivity(starter: Any, activityName: String, param: Bundle? = null) {
        ActivityRouter.startActivity(starter, activityName, param, -1)
    }

    /**
     *  启动Activity并回调参数
     * @param caller  当前Activity  或者 Fragment  
     * @param activityName Activity 名称 不可为null
     * @param param 参数 可为null
     * @param resultCallback 回调 不可为null
     * @createtime 2025/1/7 15:13
     **/
    fun startActivityForResult(caller: ActivityResultCaller?, activityName: String, param: Bundle?, resultCallback: ActivityResultCallback<ActivityResult?>?) {
        ActivityRouter.startActivityForResult(caller, activityName, param, resultCallback)
    }

    /**
     *  启动Activity并回调参数
     * @param caller  当前Activity  或者 Fragment
     * @param activityName Activity.class 名称 不可为null
     * @param param 参数 可为null
     * @param resultCallback 回调 不可为null
     * @createtime 2025/1/7 15:13
     **/
    fun startActivityForResult(caller: ActivityResultCaller?, activityClazz: Class<out Activity?>?, param: Bundle?, resultCallback: ActivityResultCallback<ActivityResult?>?) {
        ActivityRouter.startActivityForResult(caller, activityClazz, param, resultCallback)
    }
}