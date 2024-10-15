package com.mh55.easy.ext

import java.util.regex.Pattern

enum class FormMatType{
    IdCard,Mobile,Name
}


/** 字符串脱敏处理 **/
fun String.formMat(type: FormMatType):String{
    val hide: String = when(type){
        FormMatType.IdCard ->this.replace("(\\d{4})\\d{10}(\\w{4})".toRegex(), "$1*****$2")
        FormMatType.Mobile ->this.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        FormMatType.Name ->getHideString(this)
    }


    return hide
}

fun getHideString(name:String):String{
    if (name.isNullOrEmpty())return ""

    val first = name.toCharArray()[0].toString()

    if (name.length == 1){
        return first
    }else if (name.length == 2){
        return "${first}*"
    }else{
        val end = name.toCharArray()[name.length-1].toString()
        return "${first}*${end}"
    }

}


fun MutableList<String>.listToString(char:String = ","):String{
    var idStr = ""
    if (this.size == 1){
        idStr = this[0]
    }else{
        this.forEach {
            idStr = idStr+char+it
        }

        idStr = idStr.subSequence(1,idStr.length).toString()
    }

    return idStr
}

/**
 * 判断字符串是否为纯数字
 */
fun CharSequence?.isNumber(): Boolean {
    if (this == null) {
        return false
    }
    var pattern = Pattern.compile("[0-9]*")
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.isChinesePhoneNumber(): Boolean {
    return Regex("^1[3456789]\\d{9}$").matches(this)
}

fun String.isHttpUrl() = if (this.contains("http://") || this.contains("https://")) true else false