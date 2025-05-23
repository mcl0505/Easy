package com.mh0505.basic

import android.os.Bundle
import com.mh0505.basic.databinding.ActivityMainBinding
import com.mh0505.basic.net.MainViewModel
import com.mh55.easy.ext.singleClick
import com.mh55.easy.ui.activity.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {
    override fun main(savedInstanceState: Bundle?) {
        with(mBinding){
            testBtn.singleClick {
//                ImageSelectDialog.Builder()
//                    .setSingle(true)
//                    .setCompress(true)
//                    .setEnableCrop(true)
//                    .setCircleCrop(true)
//                    .setDisplayCamera(false)
//                    .setWightHeight(1,1)
//                    .setOnSelectCallBackListener {
//
//                    }
//                    .build()
//                    .show(supportFragmentManager)
            }
            testUserInfo.singleClick {
                mViewModel.getUerInfo()
            }
            testConfig.singleClick {
                mViewModel.getAppConfig()
            }
            mDragView.isAdsorbent = false
            mDragView.onFloastClick = {

            }

        }
    }
}