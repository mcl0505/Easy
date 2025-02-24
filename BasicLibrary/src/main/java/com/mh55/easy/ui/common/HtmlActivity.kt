package com.mh55.easy.ui.common

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.mh55.easy.databinding.ActivityHtmlBinding
import com.mh55.easy.manager.AppManager
import com.mh55.easy.mvvm.BaseViewModel
import com.mh55.easy.ui.activity.BaseActivity
import com.mh55.easy.utils.SoftHideKeyBoardUtil

/**
 * @createTime =>  2024/4/10 14:39
 * @class =>  HtmlActivity.kt
 * @desciption =>  网页加载
 **/
class HtmlActivity : BaseActivity<ActivityHtmlBinding, BaseViewModel>() {

    companion object {
        fun toStart(url: String, str: String = "") {
            val bundle = Intent(AppManager.peekActivity() as AppCompatActivity, HtmlActivity::class.java)
            bundle.putExtra("url", url)
            bundle.putExtra("title", str)
            AppManager.start(bundle)
        }
    }

    var loadPath = ""

    override fun setTitleText(): String {
        return intent.getStringExtra("title") ?: ""
    }

    override fun main(savedInstanceState: Bundle?) {
        SoftHideKeyBoardUtil.assistActivity(this)
        loadPath = intent.getStringExtra("url") ?: ""
        with(mBinding.main) {
            settings.apply {
                //如果访问的页面中要与Javascript交互，则webView必须设置支持Javascript
                // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
                // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
                javaScriptEnabled = true
                // 设置允许JS弹窗
                javaScriptCanOpenWindowsAutomatically = true
                //页面自适应 两者合用
                useWideViewPort = true
                loadWithOverviewMode = true
                //缩放操作
                setSupportZoom(true)//支持缩放，默认为true。是下面那个的前提。
                builtInZoomControls = true  //设置内置的缩放控件。若为false，则该WebView不可缩放
                displayZoomControls = true //隐藏原生的缩放控件
                //其他细节操作
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webView中缓存
                allowFileAccess = true //设置可以访问文件
                javaScriptCanOpenWindowsAutomatically = true  //支持通过JS打开新窗口
                loadsImagesAutomatically = true //支持自动加载图片
                defaultTextEncodingName = "utf-8"//设置编码格式
                // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
            }
            //清除网页访问留下的缓存
            //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
            clearCache(true)
            loadUrl(loadPath)
        }
    }

    override fun onDestroy() {
        if (mBinding.main != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，
            //需要先onDetachedFromWindow()，再destory()
            val parent = mBinding.main.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(mBinding.main)
            }
            mBinding.main.stopLoading()
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mBinding.main.settings.javaScriptEnabled = false
            mBinding.main.clearHistory()
            mBinding.main.removeAllViews()
            mBinding.main.destroy()

        }
        super.onDestroy()
    }

}