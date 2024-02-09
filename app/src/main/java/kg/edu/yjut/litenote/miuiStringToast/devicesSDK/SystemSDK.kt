package kg.edu.yjut.litenote.miuiStringToast.devicesSDK
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

import android.os.Build
import kg.edu.yjut.litenote.miuiStringToast.devicesSDK.PropUtils.getProp

/**
获取设备 Android 版本 、MIUI 版本 、HyperOS 版本 等
并判断设备指定类型
 */

fun getSystemVersionIncremental(): String = getProp("ro.system.build.version.incremental")
fun getBuildDate(): String = getProp("ro.system.build.date")
fun getHost(): String = Build.HOST
fun getBuilder(): String = getProp("ro.build.user")
fun getBaseOs(): String = getProp("ro.build.version.base_os")
fun getRomAuthor(): String = getProp("ro.rom.author") + getProp("ro.romid")

// ----- Android ----------------------------------------------------------------------------------

fun getAndroidVersion(): Int = Build.VERSION.SDK_INT

fun isAndroidVersion(versioncode: Int): Boolean{
    val result: Boolean = when (versioncode) {
        30 -> (getAndroidVersion() == Build.VERSION_CODES.R)
        31 -> (getAndroidVersion() == Build.VERSION_CODES.S)
        32 -> (getAndroidVersion() == Build.VERSION_CODES.S_V2)
        33 -> (getAndroidVersion() == Build.VERSION_CODES.TIRAMISU)
        34 -> (getAndroidVersion() == Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        else -> false
    };
    return result
}

fun isMoreAndroidVersion(version: Int): Boolean {
    return getAndroidVersion() >= version
}

// ----- MIUI -------------------------------------------------------------------------------------

fun getMiuiVersion(): Float = when (getProp("ro.miui.ui.version.name")) {
    "V150" -> 15f
    "V140" -> 14f
    "V130" -> 13f
    "V125" -> 12.5f
    "V12" -> 12f
    "V11" -> 11f
    "V10" -> 10f
    else -> 0f
}

fun isMiuiVersion(versioncode: Float): Boolean{
    val result: Boolean = when (versioncode) {
        10f -> (getProp("ro.miui.ui.version.name") == "V10")
        11f -> (getProp("ro.miui.ui.version.name") == "V11")
        12f -> (getProp("ro.miui.ui.version.name") == "V12")
        12.5f -> (getProp("ro.miui.ui.version.name") == "V125")
        13f -> (getProp("ro.miui.ui.version.name") == "V130")
        14f -> (getProp("ro.miui.ui.version.name") == "V140")
        15f -> (getProp("ro.miui.ui.version.name") == "V150")
        else -> false
    };
    return result
}

fun isMoreMiuiVersion(version: Float): Boolean {
    return getMiuiVersion() >= version
}

// ----- HyperOS ----------------------------------------------------------------------------------

fun getHyperOSVersion(): Float = when (getProp("ro.mi.os.version.name")) {
    "OS1.0" -> 1f
    else -> 0f
}

fun isHyperOSVersion(versioncode: Float): Boolean{
    val result: Boolean = when (versioncode) {
        1f -> (getProp("ro.mi.os.version.name") == "OS1.0")
        else -> false
    };
    return result
}

fun isMoreHyperOSVersion(version: Float): Boolean {
    return getHyperOSVersion() >= version
}