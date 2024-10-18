package com.mh.http


//请求成功
const val CODE_HTTP_SUCCESS = 200
//请求失败  可以不用定义，这里定义是为了在模拟数据的时候使用
const val CODE_HTTP_ERROR = 500
//token 失效
const val CODE_TOKEN_INVALID = 401

const val notHttpException = -100
const val mSocketTimeoutException = -101
const val mNetworkErrorException = 404
const val entityNullable = -1
const val entityCodeNullable = -2


const val msgEntityNullable = "实体为 null"
const val msgEntityCodeNullable = "实体状态码为 null"
const val msgNotHttpException = "网络请求发生错误，但错误不是 HttpException！"
const val msgSocketTimeoutException = "请求超时,请稍后再试"
const val msgNetworkErrorException = "请检查网络后重试"