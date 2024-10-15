package com.mh55.easy.ext

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.mh55.easy.manager.AppManager
import com.mh55.easy.mvvm.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 判断是否处于UI线程
 */
fun isInUIThread() = Looper.getMainLooper().thread == Thread.currentThread()

private val mHandler = Handler(Looper.getMainLooper())

private val mSingleService: ExecutorService = Executors.newSingleThreadExecutor()

/**
 * 切换到主线程
 */
fun <T> T.ktxRunOnUiThread(block: T.() -> Unit) {
    mHandler.post {
        block()
    }
}

/**
 * 延迟delayMills切换到主线程,默认是600ms
 */
fun <T> T.ktxRunOnUiThreadDelay(delayMills: Long=600, block: T.() -> Unit) {
    mHandler.postDelayed({
        block()
    }, delayMills)
}

/**
 * 子线程执行
 */
fun <T> T.ktxRunOnThreadSingle(block: T.() -> Unit) {
    mSingleService.execute {
        block()
    }
    

}

fun countDown(
    timeMillis: Long = 1000,//默认时间间隔 1 秒
    time: Int = 3,//默认时间为 3 秒
    start: (scop: CoroutineScope) -> Unit,
    end: () -> Unit,
    next: (time: Int) -> Unit,
    error: (msg: String?) -> Unit
){
    (AppManager.peekActivity() as AppCompatActivity).lifecycleScope.launch {
        flow {
            (time downTo 1).forEach {
                delay(timeMillis)
                emit(it)
            }
        }.onStart {
            start(this@launch)
        }.onCompletion {
            end()
        }.catch {
            error(it.message ?: "countDown 出现未知错误")
        }.collect {
            next(it)
        }
    }
}



