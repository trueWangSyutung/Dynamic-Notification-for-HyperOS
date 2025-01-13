package kg.edu.yjut.enhancenoticehyperos.utils

import android.content.Context

enum class SoftwareMode(val value: String) {
    CompatibleMode("compatible"),
    AllApplication("allApplication")
}

fun getSoftwareMode(value : String) : SoftwareMode {
    return when (value) {
        "compatible" -> SoftwareMode.CompatibleMode
        "allApplication" -> SoftwareMode.AllApplication
        else -> SoftwareMode.AllApplication
    }
}

object Configs {
    fun checkAgreeUser(context: Context) : Boolean {
        val sp = context.getSharedPreferences("agree", Context.MODE_PRIVATE)
        return sp.getBoolean("agree", false)
    }
    fun setAgreeUser(context: Context, agree: Boolean) {
        val sp = context.getSharedPreferences("agree", Context.MODE_PRIVATE)
        sp.edit().putBoolean("agree", agree).apply()
    }
    fun checkFirstLaunch(context: Context) : Boolean {
        val sp = context.getSharedPreferences("firstLaunch", Context.MODE_PRIVATE)
        return sp.getBoolean("firstLaunch", true)
    }
    fun setFirstLaunch(context: Context, firstLaunch: Boolean) {
        val sp = context.getSharedPreferences("firstLaunch", Context.MODE_PRIVATE)
        sp.edit().putBoolean("firstLaunch", firstLaunch).apply()
    }
    fun getSoftwareMode(context: Context) : SoftwareMode {
        val sp = context.getSharedPreferences("softwareMode", Context.MODE_PRIVATE)
        return getSoftwareMode(sp.getString("mode", "allApplication")!!)
    }
    fun setSoftwareMode(context: Context, mode: SoftwareMode) {
        val sp = context.getSharedPreferences("softwareMode", Context.MODE_PRIVATE)
        sp.edit().putString("mode", mode.value).apply()
    }

    //    var sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    //    return sp.getBoolean("useHyperOSNotices", false)
    fun checkUseHyperOSNotices(context: Context) : Boolean {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        return sp.getBoolean("useHyperOSNotices", true)
    }
    fun setUseHyperOSNotices(context: Context, useHyperOSNotices: Boolean) {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        sp.edit().putBoolean("useHyperOSNotices", useHyperOSNotices).apply()
    }


    // 检查是否接管系统通知
    fun checkInterceptSystemNotices(context: Context) : Boolean {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        return sp.getBoolean("interceptSystemNotices", false)
    }
    fun setInterceptSystemNotices(context: Context, interceptSystemNotices: Boolean) {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        sp.edit().putBoolean("interceptSystemNotices", interceptSystemNotices).apply()
    }

    fun setSettingItems(context: Context, label : String, value: Boolean) {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        sp.edit().putBoolean(label, value).apply()

    }
    fun getSettingItems(context: Context, label : String, defaultValue:Boolean) : Boolean {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        return sp.getBoolean(label, defaultValue)
    }


}