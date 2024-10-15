package com.mh55.easy.ext

import android.text.Editable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import com.mh55.easy.utils.CashierInputFilter

/**输入框赋值**/
fun EditText.setEditContent(text: String?) {
    this.text = Editable.Factory.getInstance().newEditable(if (text.isNullOrEmpty()) "" else text)
}

/**是否显示内容 **/
fun EditText.setPwdInputType(isShow: Boolean) {
    if (isShow) showPassword() else hidePassword()
}

/**
 * 隐藏密码
 */
fun EditText.hidePassword() {
    transformationMethod = PasswordTransformationMethod.getInstance()
    setSelection(text.toString().length)
}

/**
 * 显示密码
 */
fun EditText.showPassword() {
    transformationMethod = HideReturnsTransformationMethod.getInstance()
    setSelection(text.toString().length)

}

/**添加输入最大值限制**/
fun EditText.addMinMax(maxValue: Int = Int.MAX_VALUE, pointerLength: Int = 2) {
    this.filters = arrayOf(CashierInputFilter(maxValue, pointerLength))
}