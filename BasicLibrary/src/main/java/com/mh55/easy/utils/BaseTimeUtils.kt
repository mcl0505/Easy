package com.mh55.easy.utils

import java.text.SimpleDateFormat
import java.util.*

object BaseTimeUtils {
    const val mDefaultPatternYMD = "yyyy-MM-dd"
    const val mDefaultPatternYMDHMS = "yyyy-MM-dd HH:mm:ss"

    private fun getSimpleDateFormat(mPattern:String) = SimpleDateFormat(mPattern,Locale.getDefault())

    /**
     * 获取当前时间戳
     *  返回毫秒时间戳
     */
    fun getCurrentMillis() = System.currentTimeMillis()

    /**
     * 时间戳转时间
     * @param millis 时间戳  毫秒
     * @param mPattern 字符串格式
     */
    fun millisToString(millis : Long, mPattern:String = mDefaultPatternYMDHMS) = getSimpleDateFormat(mPattern).format(Date(millis))?:""

    /**
     * 时间格式字符串转时间戳
     * @param time 时间  格式与时间戳格式一致
     * @param mPattern 字符串格式
     */
    fun stringToMillis(time:String,mPattern:String = mDefaultPatternYMDHMS): Long = getSimpleDateFormat(mPattern).parse(time)?.time?:0

    /**
     * 时间格式字符串转Date
     * @param time 时间  格式与时间戳格式一致
     * @param mPattern 字符串格式
     */
    fun stringToDate(time:String,mPattern:String = mDefaultPatternYMDHMS) = getSimpleDateFormat(mPattern).parse(time)?: Date()

    /**
     * 比较日期大小
     * @param startMillis 第一个时间戳
     * @param endMillis 第二个时间戳
     */
    fun compareDateMillis(startMillis: Long, endMillis: Long) = if (startMillis == endMillis)0 else if (startMillis>endMillis) -1 else 1
    fun compareDateString(startTime: String, endString: String,mPattern:String = mDefaultPatternYMDHMS) =
        if (stringToMillis(startTime,mPattern) == stringToMillis(endString,mPattern))0
        else if (stringToMillis(startTime,mPattern) > stringToMillis(endString,mPattern)) -1
        else 1

    /**
     * 根据日期算周几
     */
    fun getWeekDay(date: Date): String {
        val calendar = Calendar.getInstance()// 获得一个日历
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)
        calendar.set(year, month, day)// 设置当前时间,月份是从0月开始计算
        val number = calendar.get(Calendar.DAY_OF_WEEK)// 周表示1-7，是从周日开始，
        val str = arrayOf("", "周日", "周一", "周二", "周三", "周四", "周五", "周六")
        return str[number]
    }

    /**
     * 获取[date2]比[date1]多的天数
     */
    fun dateDifference(date1: Date, date2: Date): Int {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        val day1 = cal1[Calendar.DAY_OF_YEAR]
        val day2 = cal2[Calendar.DAY_OF_YEAR]
        val year1 = cal1[Calendar.YEAR]
        val year2 = cal2[Calendar.YEAR]
        return if (year1 != year2) {
            var timeDistance = 0
            for (i in year1 until year2) {
                timeDistance += if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    //闰年
                    366
                } else {//不是闰年
                    365
                }
            }
            timeDistance + (day2 - day1)
        } else {
            day2 - day1
        }
    }

}