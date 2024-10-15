package com.mh55.httplibrary.common

object HttpConstants {
    const val SUCCESS_CODE = 200   //200 成功，
    const val ERROR_CODE_500 = 500  // // 500 错误

    const val ERROR_CODE_401 = 401 // 登录已过期，
    const val ERROR_CODE_402 = 402 // 当前账号在另一设备上登陆，请重新登录，
    const val ERROR_CODE_405 = 405 //  你还未注册，请用该手机号注册，
    const val ERROR_CODE_406 = 406 //  你还未绑定手机号，请绑定手机，

    const val ERROR_CODE_217 = 217  // 支付密码错误
    const val ERROR_CODE_218 = 218  // 你还未设置交易密码
    const val ERROR_CODE_219 = 219  // 账户已锁定，请修改交易密码，

    const val ERROR_CODE_306 = 306 //已经付款了实名认证费用,但是还未实名认证，
    const val ERROR_CODE_308 = 308  //魔珠余额不足，
    const val ERROR_CODE_309 = 309 //你还未实名认证，

    const val ERROR_CODE_505 = 505 //密码错误
    const val ERROR_CODE_506 = 506 //  人机验证失败，

    const val ERROR_CODE_605 = 605 //未定位的用户


    const val ERROR_CODE_706 = 706 //  版本更新 APP版本过低

    const val ERROR_CODE_4006 = 4006 //  登录-密码输入错误次数太多，请使用验证码登录

    const val ERROR_CODE_1001 = 1001 //  定位

}