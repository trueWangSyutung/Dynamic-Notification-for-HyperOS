package kg.edu.yjut.litenote.miui.devicesSDK
/*
  * This file is part of HyperCeiler.

  * HyperCeiler is free software: you can redistribute it and/or modify
  * it under the terms of the GNU Affero General Public License as
  * published by the Free Software Foundation, either version 3 of the
  * License.

  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU Affero General Public License for more details.

  * You should have received a copy of the GNU Affero General Public License
  * along with this program.  If not, see <https://www.gnu.org/licenses/>.

  * Copyright (C) 2023-2024 HyperCeiler Contributions
*/

import android.content.Context
import android.os.Build
import kg.edu.yjut.litenote.miui.devicesSDK.PropUtils.getProp

/**
获取设备 Android 版本 、MIUI 版本 、HyperOS 版本 等
并判断设备指定类型
 */

// ----- HyperOS ----------------------------------------------------------------------------------

fun getHyperOSVersion(): Float = when (getProp("ro.mi.os.version.name")) {
    "OS1.0" -> 1f
    else -> 0f
}


//判断是否是横屏
fun isLandscape(
    context : Context
): Boolean {
    var w = context.resources.displayMetrics.widthPixels
    var h = context.resources.displayMetrics.heightPixels
    return w > h
}

fun isMoreHyperOSVersion(version: Float): Boolean {
    return getHyperOSVersion() >= version
}

fun isUnHyperOSNotices(context: Context): Boolean {
    var sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    return sp.getBoolean("useHyperOSNotices", false)
}

fun closeUseHyperOSNotices(context: Context) {
    var sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    var editor = sp.edit()
    editor.putBoolean("useHyperOSNotices", true)
    editor.apply()
}

fun openUseHyperOSNotices(context: Context) {
    var sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    var editor = sp.edit()
    editor.putBoolean("useHyperOSNotices", false)
    editor.apply()
}
