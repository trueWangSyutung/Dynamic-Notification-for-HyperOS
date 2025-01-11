/*
 * Copyright (C) 2024 The LiteNote Project
 * @author OpenAcademic
 * @version 1.0
 * 
 */
package  kg.edu.yjut.enhancenoticehyperos.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.requestPermissions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat

@Composable
// 根据 添加的日期 来 判断急迫程度
fun getUrgency(date: Long) {
    val currentTime = System.currentTimeMillis()
    val diffTime = (currentTime - date) / 1000 / 60 / 60  // 计算时间差（小时）
    // 根据时间差判断急迫程度
    // 时间差在 3 个小时内，认为是 不紧急的

    // 3-6 小时，认为是 紧急的，返回 1个 红色感叹号
    if (diffTime < 3) {
        Text(
            "!",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End

        )
    } else if (diffTime < 6) {
        Text(text = "!",
            color = MaterialTheme.colorScheme.error,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
            )
    } else if (diffTime < 9) {
        Text(text = "!!",
            color = MaterialTheme.colorScheme.error,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
            )
    } else if (diffTime < 12) {
        Text(text = "!!!",
            color = MaterialTheme.colorScheme.error,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
            )
    } else if (diffTime < 24) {
        Text(text = "!!!!",
            color = MaterialTheme.colorScheme.error,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
            )
    } else {
        Text(text = "!!!!!",
            color = MaterialTheme.colorScheme.error,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
            )
    }


}




fun getApplicationStatus(
    context: Context
): Boolean {
    val sharePref = context.getSharedPreferences("init", Context.MODE_PRIVATE)
    return sharePref.getBoolean("isFirst", true)
}


fun getApplicationAgentStatus(
    context: Context
): Boolean{
    val sharePref = context.getSharedPreferences("init", Context.MODE_PRIVATE)
    return  sharePref.getBoolean("agent", false)
}

fun timestr2ShowStr(timest: Long): AnnotatedString {
    val sdf = SimpleDateFormat("yyyy-MM-dd-HH-mm");
    val dateStr = sdf.format(java.util.Date(timest).time);
    val strs = dateStr.split("-")
    return  buildAnnotatedString {

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 20.sp)) {
            append(strs[0])
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 16.sp)) {
            append("年")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 20.sp)) {
            append(strs[1])
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 16.sp)) {
            append("月")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 20.sp)) {
            append(strs[2])
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 16.sp)) {
            append("日")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 20.sp)) {
            append(strs[3])
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 16.sp)) {
            append(":")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 20.sp)) {
            append(strs[4])
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
            color = Color.Black, fontSize = 18.sp)) {
            append("开")
        }



    }
}

@SuppressLint("SimpleDateFormat")
fun timeStempToTime(timest: Long,mode:Int): String {
    //val ptimest=1000L*timest
    // 转换为 2024-12-12 12:12
    if (mode == 1){
        val sdf =  SimpleDateFormat("yyyy-MM-dd");
        val dateStr = sdf.format(java.util.Date(timest).time);
        return dateStr;
    }else if (mode == 2){
        val sdf =  SimpleDateFormat("yyyy-MM");
        val dateStr = sdf.format(java.util.Date(timest).time);
        return dateStr;
    }else if (mode == 3){
        val sdf =  SimpleDateFormat("yyyy");
        val dateStr = sdf.format(java.util.Date(timest).time);
        return dateStr;
    }

    else{
        val sdf =  SimpleDateFormat("yyyy-MM-dd HH:mm");
        val dateStr = sdf.format(java.util.Date(timest).time);
        return dateStr;
    }

}

enum class ModeType(val value: Int,var show:String) {
    AUTO(0,"自动"),
    LIGHT(1, "浅色模式"),
    NIGHT(2 , "深色模式"),
}
fun getModeType(value: Int): ModeType {
    return when (value) {
        0 -> ModeType.AUTO
        1 -> ModeType.LIGHT
        2 -> ModeType.NIGHT
        else -> ModeType.AUTO
    }
}


fun isDarkMode(
    context: Context
): Boolean {
    val mode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
    return mode == android.content.res.Configuration.UI_MODE_NIGHT_YES
}
fun isDarkMode2(
    context: Context
): Boolean {
    val mode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
    return mode == android.content.res.Configuration.UI_MODE_NIGHT_YES
}
fun getDarkModeTextColor2(
    context: Context
): Color {
    return if (isDarkMode2(context)) {
        Color.White
    } else {
        Color.Black
    }
}
fun getDarkModeTextColor(
    context: Context
): Color {
    return if (isDarkMode(context)) {
        Color.White
    } else {
        Color.Black
    }
}

fun getBtnDarkModeTextColor(
    context: Context
): Color {
    return if (!isDarkMode(context)) {
        Color.White
    } else {
        Color.Black
    }
}

fun randomColor(): Color {
    val r = (0..255).random()
    val g = (0..255).random()
    val b = (0..255).random()
    return Color(r, g, b)
}

@Composable
fun getDarkModeBackgroundColor(
    context: Context,
    level : Int
): Color {
    return if (isDarkMode(context)) {
        if (level == 0) {
            MaterialTheme.colorScheme.surface
        } else if (level == 1) {
            MaterialTheme.colorScheme.surfaceContainer
        } else if (level == 2) {
            Color.Gray
        } else {
            Color.Black
        }
    } else {
        if (level == 0) {
            MaterialTheme.colorScheme.surface
        } else if (level == 1) {
            MaterialTheme.colorScheme.surfaceContainer
        } else if (level == 2) {
            Color(0xFFFFF8E3)
        } else {
            Color.White
        }
    }
}




fun daysToYearDays(totalDays: Int): String {
    val years = totalDays / 365
    val days = totalDays % 365
    return when {
        years > 0 && days > 0 -> "${years}年${days}天"
        years > 0 -> "${years}年"
        else -> "${days}天"
    }
}