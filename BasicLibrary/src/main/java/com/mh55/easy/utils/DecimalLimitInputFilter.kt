package com.mh55.easy.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalLimitInputFilter(private val maxValue: Double, private val decimalPlaces: Int) :
    InputFilter {

    // 正则表达式匹配规则，允许数字和指定的小数位数
    private val pattern = Pattern.compile("[0-9]*+((\\.[0-9]{0,$decimalPlaces})?)||(\\.)?")

    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        val result = StringBuilder(dest.toString()).replace(dstart, dend, source.toString())

        // 确保第一位不是0，除非它是唯一的一个数字或后面跟着一个小数点
        if (result.isNotEmpty() && result[0] == '0' && result.length > 1 && result[1] != '.') {
            return ""
        }

        if (decimalPlaces == 0){
            if (result.isNotEmpty() && result[0] == '0'){
                return ""
            }
        }

        // 检查是否符合正则表达式
        if (!pattern.matcher(result).matches()) {
            return ""
        }

        // 尝试将结果转换为Double来检查是否超过了最大值
        return try {
            val inputDouble = if (result.isEmpty()) 0.0 else java.lang.Double.parseDouble(result.toString())
            if (inputDouble <= maxValue) null else "" // 如果输入合法返回null，否则返回空字符串拒绝输入
        } catch (e: NumberFormatException) {
            "" // 如果格式错误，拒绝输入
        }
    }
}