package com.mh55.httplibrary.ext

import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils.isConnected
import com.mh55.easy.app.AppConfig
import com.mh55.easy.ext.toast
import com.mh55.easy.mvvm.BaseViewModel
import com.mh55.easy.mvvm.intent.BaseViewIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HttpRequestDSL {
    open var isShowDialog = false
    var loadingMessage = "请求网络中..."
    lateinit var onRequest:suspend CoroutineScope.() -> Unit
    lateinit var onError:(throwable:Throwable)-> Unit
}


fun BaseViewModel.rxRequestHttp(requestDslClass: HttpRequestDSL.()->Unit){
    if (!isConnected()){
        "当前网络不可用".toast()
        return
    }

    val requestDSL = HttpRequestDSL()
    requestDslClass.invoke(requestDSL)

    if (requestDSL.isShowDialog){
        this.mUiChangeLiveData.postValue(BaseViewIntent.showLoading(isShow = requestDSL.isShowDialog, showMsg = requestDSL.loadingMessage))
    }

    viewModelScope.launch {
        try {
            requestDSL.onRequest.invoke(this)
        }catch (e:Exception){
            try {
                errorCodeProcess(e){
                    requestDSL.onError.invoke(e)
                }

            }catch (ex:Exception){
                errorCodeProcess(e){
                    AppConfig.mAppListener.onErrorTip(e.errorCode,e.errorMsg)
                }

            }


        }finally {
            if (requestDSL.isShowDialog){
                this@rxRequestHttp.mUiChangeLiveData.postValue(BaseViewIntent.showLoading(isShow = false, showMsg = requestDSL.loadingMessage))
            }
        }

    }




}