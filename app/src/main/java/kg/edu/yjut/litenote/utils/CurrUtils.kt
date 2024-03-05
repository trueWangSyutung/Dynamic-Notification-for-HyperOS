package cn.tw.sar.projecter.utils

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

fun isDarkMode(
    context: Context
): Boolean {
    val mode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
    return mode == android.content.res.Configuration.UI_MODE_NIGHT_YES

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

fun getDarkModeBackgroundColor(
    context: Context,
    level : Int
): Color {
    return if (isDarkMode(context)) {
        if (level == 0) {
            Color.Black
        } else if (level == 1) {
            Color.DarkGray
        } else if (level == 2) {
            Color.Gray
        } else {
            Color.Black
        }
    } else {
        if (level == 0) {
            Color.White
        } else if (level == 1) {
            Color(0xFFFFFAF8)
        } else if (level == 2) {
            Color(0xFFFFF8E3)
        } else {
            Color.White
        }
    }
}


fun getYesOrNo(
    value: Boolean
): ImageVector {
    return if (value) {
        Icons.Filled.Check
    } else {
        Icons.Filled.Close
    }
}

fun getYesOrNoColor(
    value: Boolean
): Color {
    return if (value) {
        Color.Green
    } else {
        Color.Red
    }
}