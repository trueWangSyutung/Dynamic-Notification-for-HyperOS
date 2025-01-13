/*
 * Copyright (C) 2024 The LiteNote Project
 * @author OpenAcademic
 * @version 1.0
 * 
 */
package  kg.edu.yjut.enhancenoticehyperos.utils

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlin.concurrent.thread

object PickupCodeUtils {
    fun isYanzhengma(code: String): Boolean {
        // 如果存在 验证码、验证码、验证码字样，说明是验证码
        if (code.contains("验证码")) {
            return true
        }
        return false
    }
    fun getYanzhengma(code:String):String{
        // 验证码通常为4-6位数字、也有甚者是 六位大写字母
        // 例如：验证码：123456、验证码：ABCEF、验证码：1234
        var reg1 = Regex("[0-9]{4,6}")
        var reg2 = Regex("[A-Z]{6}")
        var result = reg1.find(code)
        if (result!=null) {
            // 获取结果
            return result.value

        }
        result = reg2.find(code)
        if (result!=null) {
            // 获取结果
            return result.value
        }
        return ""


    }


}