package com.mh55.easy.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

/**
 * 创建 Intent 实例，并把参数 [map] [bundle] 放进去
 */
fun getIntentByMapOrBundle(
    context: Context? = null,
    clz: Class<out Activity>? = null,
    map: MutableMap<String, *>? = null,
    bundle: Bundle? = null
): Intent {
    val intent =
        if (context != null && clz != null)
            Intent(context, clz)
        else
            Intent()

    map?.forEach { entry ->
        @Suppress("UNCHECKED_CAST")
        when (val value = entry.value) {
            is Boolean -> {
                intent.putExtra(entry.key, value)
            }
            is BooleanArray -> {
                intent.putExtra(entry.key, value)
            }
            is Byte -> {
                intent.putExtra(entry.key, value)
            }
            is ByteArray -> {
                intent.putExtra(entry.key, value)
            }
            is Char -> {
                intent.putExtra(entry.key, value)
            }
            is CharArray -> {
                intent.putExtra(entry.key, value)
            }
            is Short -> {
                intent.putExtra(entry.key, value)
            }
            is ShortArray -> {
                intent.putExtra(entry.key, value)
            }
            is Int -> {
                intent.putExtra(entry.key, value)
            }
            is IntArray -> {
                intent.putExtra(entry.key, value)
            }
            is Long -> {
                intent.putExtra(entry.key, value)
            }
            is LongArray -> {
                intent.putExtra(entry.key, value)
            }
            is Float -> {
                intent.putExtra(entry.key, value)
            }
            is FloatArray -> {
                intent.putExtra(entry.key, value)
            }
            is Double -> {
                intent.putExtra(entry.key, value)
            }
            is DoubleArray -> {
                intent.putExtra(entry.key, value)
            }
            is String -> {
                intent.putExtra(entry.key, value)
            }
            is CharSequence -> {
                intent.putExtra(entry.key, value)
            }
            is Parcelable -> {
                intent.putExtra(entry.key, value)
            }
            is Serializable -> {
                intent.putExtra(entry.key, value)
            }
            is Bundle -> {
                intent.putExtra(entry.key, value)
            }
            is Intent -> {
                intent.putExtra(entry.key, value)
            }
            is ArrayList<*> -> {
                val any = if (value.isNotEmpty()) {
                    value[0]
                } else null
                when (any) {
                    is String -> {
                        intent.putExtra(entry.key, value as ArrayList<String>)
                    }
                    is Parcelable -> {
                        intent.putExtra(entry.key, value as ArrayList<Parcelable>)
                    }
                    is Int -> {
                        intent.putExtra(entry.key, value as ArrayList<Int>)
                    }
                    is CharSequence -> {
                        intent.putExtra(entry.key, value as ArrayList<CharSequence>)
                    }
                    else -> {
                        throw RuntimeException("不支持此类型 $value")
                    }
                }
            }
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() -> {
                        intent.putExtra(entry.key, value as Array<String>)
                    }
                    value.isArrayOf<Parcelable>() -> {
                        intent.putExtra(entry.key, value as Array<Parcelable>)
                    }
                    value.isArrayOf<CharSequence>() -> {
                        intent.putExtra(entry.key, value as Array<CharSequence>)
                    }
                    else -> {
                        throw RuntimeException("不支持此类型 $value")
                    }
                }
            }
            else -> {
                throw RuntimeException("不支持此类型 $value")
            }
        }
    }
    bundle?.let { intent.putExtras(bundle) }
    return intent
}

fun MutableMap<String, Any?>?.mapToBundle() :Bundle  {
    val bundle = Bundle()

    this?.forEach { entry ->
        if (entry.value!=null){
            @Suppress("UNCHECKED_CAST")
            when(val value = entry.value){
                is Int->bundle.putInt(entry.key,value)
                is Boolean->bundle.putBoolean(entry.key,value)
                is Float->bundle.putFloat(entry.key,value)
                is Double->bundle.putDouble(entry.key,value)
                is String->bundle.putString(entry.key,value)
                is Parcelable->bundle.putParcelable(entry.key,value)
                is Serializable->bundle.putSerializable(entry.key,value)
                else -> {
                    throw RuntimeException("不支持此类型 $value")
                }
            }
        }

    }

    return bundle
}

// 主 Activity 中的方法
/**
 *
 * @param activityClassName 需要传入Activity 完整路劲
 * @return null
 * @createtime 2024/12/6 14:15
 **/
fun navigateToActivity(context: AppCompatActivity, activityClassName: String,map: MutableMap<String, Any>? = null) {
    try {
        // 使用反射获取 Class 对象
        val targetActivityClass = Class.forName(activityClassName)

        // 检查该 Class 是否是 Activity 的子类
        if (AppCompatActivity::class.java.isAssignableFrom(targetActivityClass)) {
            // 创建并启动 Intent
            val intent = Intent(context, targetActivityClass as Class<out AppCompatActivity>)
            map?.forEach { entry ->
                @Suppress("UNCHECKED_CAST")
                when (val value = entry.value) {
                    is Boolean -> {
                        intent.putExtra(entry.key, value)
                    }
                    is BooleanArray -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Byte -> {
                        intent.putExtra(entry.key, value)
                    }
                    is ByteArray -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Char -> {
                        intent.putExtra(entry.key, value)
                    }
                    is CharArray -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Short -> {
                        intent.putExtra(entry.key, value)
                    }
                    is ShortArray -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Int -> {
                        intent.putExtra(entry.key, value)
                    }
                    is IntArray -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Long -> {
                        intent.putExtra(entry.key, value)
                    }
                    is LongArray -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Float -> {
                        intent.putExtra(entry.key, value)
                    }
                    is FloatArray -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Double -> {
                        intent.putExtra(entry.key, value)
                    }
                    is DoubleArray -> {
                        intent.putExtra(entry.key, value)
                    }
                    is String -> {
                        intent.putExtra(entry.key, value)
                    }
                    is CharSequence -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Parcelable -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Serializable -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Bundle -> {
                        intent.putExtra(entry.key, value)
                    }
                    is Intent -> {
                        intent.putExtra(entry.key, value)
                    }
                    is ArrayList<*> -> {
                        val any = if (value.isNotEmpty()) {
                            value[0]
                        } else null
                        when (any) {
                            is String -> {
                                intent.putExtra(entry.key, value as ArrayList<String>)
                            }
                            is Parcelable -> {
                                intent.putExtra(entry.key, value as ArrayList<Parcelable>)
                            }
                            is Int -> {
                                intent.putExtra(entry.key, value as ArrayList<Int>)
                            }
                            is CharSequence -> {
                                intent.putExtra(entry.key, value as ArrayList<CharSequence>)
                            }
                            else -> {
                                throw RuntimeException("不支持此类型 $value")
                            }
                        }
                    }
                    is Array<*> -> {
                        when {
                            value.isArrayOf<String>() -> {
                                intent.putExtra(entry.key, value as Array<String>)
                            }
                            value.isArrayOf<Parcelable>() -> {
                                intent.putExtra(entry.key, value as Array<Parcelable>)
                            }
                            value.isArrayOf<CharSequence>() -> {
                                intent.putExtra(entry.key, value as Array<CharSequence>)
                            }
                            else -> {
                                throw RuntimeException("不支持此类型 $value")
                            }
                        }
                    }
                    else -> {
                        throw RuntimeException("不支持此类型 $value")
                    }
                }
            }
            context.startActivity(intent)
        } else {
            throw IllegalArgumentException("The specified class is not a subclass of AppCompatActivity")
        }
    } catch (e: ClassNotFoundException) {
        // 处理类找不到的情况
        e.printStackTrace()
        println("Could not find the specified activity class: $activityClassName")
    } catch (e: IllegalAccessException) {
        // 处理非法访问异常
        e.printStackTrace()
        println("IllegalAccessException while trying to start the activity: ${e.message}")
    } catch (e: InstantiationException) {
        // 处理实例化异常
        e.printStackTrace()
        println("InstantiationException while trying to start the activity: ${e.message}")
    } catch (e: Exception) {
        // 处理其他可能的异常
        e.printStackTrace()
        println("An error occurred while trying to start the activity: ${e.message}")
    }
}