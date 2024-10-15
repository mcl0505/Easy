package com.mh0505.basic

import android.os.Bundle
import com.mh0505.basic.databinding.ActivityMainBinding
import com.mh0505.basic.net.MainViewModel
import com.mh55.easy.app.AppConfig
import com.mh55.easy.ext.singleClick
import com.mh55.easy.ext.toast
import com.mh55.easy.ui.activity.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {
    override fun main(savedInstanceState: Bundle?) {
        with(mBinding){
            testBtn.singleClick {
                mViewModel.login()
            }
            testUserInfo.singleClick {
                mViewModel.getUerInfo()
            }
            testConfig.singleClick {
                mViewModel.getAppConfig()
            }
            mDragView.isAdsorbent = false
            mDragView.onFloastClick = {
//                "点击了悬浮按钮".toast()
                AppConfig.mAppListener.onErrorTip(100,"错误形象")
            }

        }
    }
}