package com.mh.http

import com.mh.http.moshi.MoshiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * 公司坤创科技有限公
 * 作者：Android 孟从伦
 * 创建时间：2021/7/23
 * 功能描述：
 */
open class BaseRepository {
    /**post 传递json   map**/
    open fun postJson(map: Map<String, Any?>): RequestBody = RequestBody.create(
        "application/json;charset=UTF-8".toMediaTypeOrNull(),
        MoshiUtils.toJson(map)
    )

    /**post 传递json   Any**/
    open fun postJson(bean: Any): RequestBody = RequestBody.create(
        "application/json;charset=UTF-8".toMediaTypeOrNull(),
        MoshiUtils.toJson(bean)
    )

    /**文件上传**/
    open fun fileToPart(filePath:String,uploadKey:String):MultipartBody.Part{
        val file = File(filePath)
        val fileRQ: RequestBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
        val part: MultipartBody.Part =
            MultipartBody.Part.createFormData(uploadKey, file.getName(), fileRQ)
        return part
    }
}