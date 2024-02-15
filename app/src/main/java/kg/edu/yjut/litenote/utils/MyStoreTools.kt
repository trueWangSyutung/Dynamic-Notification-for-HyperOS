package kg.edu.yjut.litenote.utils

import android.content.Context
import com.google.gson.Gson
import kg.edu.yjut.litenote.bean.ChannelInfo

object MyStoreTools {




    fun getChannalList(context: Context, packageName: String) : List<ChannelInfo>
    {
        var sp = context.getSharedPreferences("channels", Context.MODE_PRIVATE)
        var channelStr = sp.getString(packageName, "[]")
        // 转为 数组 对象
        return Gson().fromJson(channelStr, Array<ChannelInfo>::class.java).toList()
    }

    fun saveChannalList(context: Context, packageName: String, list: List<ChannelInfo>)
    {
        var sp = context.getSharedPreferences("channels", Context.MODE_PRIVATE)
        var channelStr = Gson().toJson(list)
        sp.edit().putString(packageName, channelStr).apply()
    }

    fun checkChannel(context: Context, packageName: String, channelName: String) : Boolean
    {
        var sp = context.getSharedPreferences("channels", Context.MODE_PRIVATE)
        var channelStr = sp.getString(packageName, "[]")
        // 转为 数组 对象
        var channelList = Gson().fromJson(channelStr, Array<ChannelInfo>::class.java).toList()
        for (channel in channelList)
        {
            if (channel.channelName == channelName)
            {
                return true
            }
        }
        return false
    }

    fun addChannel(context: Context, packageName: String, channel: ChannelInfo)
    {
        var sp = context.getSharedPreferences("channels", Context.MODE_PRIVATE)
        var channelStr = sp.getString(packageName, "[]")
        // 转为 数组 对象
        var channelList = Gson().fromJson(channelStr, Array<ChannelInfo>::class.java).toMutableList()
        channelList.add(channel)
        sp.edit().putString(packageName, Gson().toJson(channelList)).apply()
    }


    fun isPad(context: Context): Boolean {
        return context.resources.configuration.smallestScreenWidthDp >= 600
    }

}