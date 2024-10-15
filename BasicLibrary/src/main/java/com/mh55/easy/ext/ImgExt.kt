package com.mh55.easy.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.mh55.easy.app.ConfigBuilder
import com.mh55.easy.manager.AppManager

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/10/18
 *   功能描述:
 *
 *   图片加载   Coil + ShapeableImageView
 *   Coil:加载普通图片与圆形图片
 *   ShapeableImageView：添加不同的圆角 配合Coil 使用
 */

/**
 * 加载普通图片
 * @param url  必选  可为空
 * @param imageView  图片展示控件  如果不传，则加载的结果在block() 中展示
 * @param block  图加载结果回调  仅imageView 为null 时生效
 */
fun displayImage(url: Any?,imageView: ImageView?=null,block:(drawable:Drawable)->Unit = {}){
    if (imageView == null){
        val request = ImageRequest.Builder(AppManager.getContext())
            .data(url)
            .placeholder(ConfigBuilder.mImagePlaceholder)
            .error(ConfigBuilder.mImagePlaceholder)
            .target {
                block.invoke(it)
            }
            .build()
        AppManager.getContext().imageLoader.enqueue(request)
    }else {
        imageView.load(url){
            placeholder(ConfigBuilder.mImagePlaceholder)
            error(ConfigBuilder.mImagePlaceholder)
        }
    }
}

/**
 * 加载圆形图片
 * @param url  必选  可为空
 * @param imageView  图片展示控件  如果不传，则加载的结果在block() 中展示
 * @param block  图加载结果回调  仅imageView 为null 时生效
 */
fun displayImageRound(url: Any?,imageView: ImageView?=null,block:(drawable:Drawable)->Unit = {}){
    if (imageView == null){
        val request = ImageRequest.Builder(AppManager.getContext())
            .data(url)
            .placeholder(ConfigBuilder.mImagePlaceholder)
            .error(ConfigBuilder.mImagePlaceholder)
            .target {
                block.invoke(it)
            }
            .build()
        AppManager.getContext().imageLoader.enqueue(request)
    }else {
        imageView.load(url){
            placeholder(ConfigBuilder.mImagePlaceholder)
            error(ConfigBuilder.mImagePlaceholder)
            transformations(CircleCropTransformation())
        }
    }
}

/**
 * 加载圆形图片
 * @param url  必选  可为空
 * @param imageView  图片展示控件  如果不传，则加载的结果在block() 中展示
 * @param block  图加载结果回调  仅imageView 为null 时生效
 */
fun displayImageRadius(url: Any?,imageView: ImageView?=null,radius:Float?=0f){
    if (imageView == null){
        val request = ImageRequest.Builder(AppManager.getContext())
            .data(url)
            .placeholder(ConfigBuilder.mImagePlaceholder)
            .error(ConfigBuilder.mImagePlaceholder)
            .build()
        AppManager.getContext().imageLoader.enqueue(request)
    }else {
        imageView.load(url){
            placeholder(ConfigBuilder.mImagePlaceholder)
            error(ConfigBuilder.mImagePlaceholder)
            transformations(RoundedCornersTransformation(radius?:0f))
        }
    }
}
