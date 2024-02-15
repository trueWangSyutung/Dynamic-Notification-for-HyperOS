package kg.edu.yjut.litenote.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.activity.isDarkTheme
import kg.edu.yjut.litenote.bean.ActionInfo
import kg.edu.yjut.litenote.bean.ChannelInfo

var supportList = listOf<ActionInfo>(
    ActionInfo("com.android.mms", "MyHomeActivity", "自动识别短信中的快递取件码短信，通过“舞台动效”提醒您。", listOf()),
    ActionInfo("com.tencent.mm", "ChannelActivity", "将微信消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "message_channel_new_id", "新消息"
        ),
        ChannelInfo(
            "voip_norify_channel_silent1704436236834","音视频消息"
        ),
    )),
    ActionInfo("com.ss.android.ugc.aweme", "ChannelActivity", "将抖音消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),

        )),
    ActionInfo("com.android.email", "ChannelActivity", "将邮件消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),
    ActionInfo("com.tencent.mobileqq", "ChannelActivity", "将QQ消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),
    ActionInfo("com.weico.international", "ChannelActivity", "将微博轻享版消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),
    ActionInfo("com.sina.weibo", "ChannelActivity", "将微博消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),
    ActionInfo("com.xingin.xhs", "ChannelActivity", "将小红书消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),
    ActionInfo("cn.xuexi.android", "ChannelActivity", "将学习强国消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),
    ActionInfo("com.zhihu.android", "ChannelActivity", "将知乎消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),
    ActionInfo("tv.danmaku.bili", "ChannelActivity", "将B站消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),

    ActionInfo("com.tencent.qqmusic", "ChannelActivity", "将QQ音乐消息通知通过“舞台动效”提醒您。", listOf(
        ChannelInfo(
            "mipush", "小米推送"
        ),
    )),
    //
    )

var supposedPackageName = arrayOf(
    "com.tencent.mm",
    "com.ss.android.ugc.aweme",
    "com.android.email",
    "com.tencent.mobileqq",
    "com.weico.international",
    "com.sina.weibo",
    "com.xingin.xhs",
    "cn.xuexi.android",
    "com.android.mms",
    "com.zhihu.android",
    "com.tencent.qqmusic",
    "tv.danmaku.bili"
)
var supposedIconMap = mapOf(
    "com.tencent.mm" to "wechat",
    "com.ss.android.ugc.aweme" to "dy",
    "com.android.email" to "vpn",
    "com.tencent.mobileqq" to "qq",
    "com.weico.international" to "weibo",
    "com.sina.weibo" to "weibo",
    "com.xingin.xhs" to "xhs",
    "cn.xuexi.android" to "xuexi",
    "com.zhihu.android" to "zhihu",
    "com.tencent.qqmusic" to "qqmusic",
    "tv.danmaku.bili" to "bili"
)
var suppsedColorStr = mapOf(
    "com.tencent.mm" to "#058B0E",
    "com.ss.android.ugc.aweme" to "#FFFFFF",
    "com.android.email" to "#91C8E4",
    "com.tencent.mobileqq" to "#1296DB",
    "com.weico.international" to "#FFFFFF",
    "com.sina.weibo" to "#FFFFFF",
    "com.xingin.xhs" to "#FFFFFF",
    "cn.xuexi.android" to "#D81E06",
    "com.zhihu.android" to "#1296db",
    "com.tencent.qqmusic" to "#FFD700",
    "tv.danmaku.bili" to "#03A7FD"
)
var supposedDuration = mapOf(
    "com.tencent.mm" to 6000L,
    "com.ss.android.ugc.aweme" to 4000L,
    "com.android.email" to 4000L,
    "com.tencent.mobileqq" to 6000L,
    "com.weico.international" to 4000L,
    "com.sina.weibo" to 5000L,
    "com.xingin.xhs" to 5000L,
    "cn.xuexi.android" to 5000L,
    "com.zhihu.android" to 5000L,
    "com.tencent.qqmusic" to 5000L,
    "tv.danmaku.bili" to 5000L
)

fun getIcons(str:String) : Int{
    var icon = when(str){
        "dy" -> R.drawable.dy
        "wechat" -> R.drawable.wechat
        "vpn" -> R.drawable.vpn
        "qq" -> R.drawable.qq
        "logo" -> R.drawable.dd
        "weibo" -> R.drawable.weibo
        "xhs" -> R.drawable.xhs
        "xuexi" -> R.drawable.xuexi
        "zhihu" -> R.drawable.zhihu
        "qqmusic" -> R.drawable.qqmusic
        "bili" -> R.drawable.bili
        "logo" -> R.drawable.logo
        else -> R.drawable.ic_kuaidi_foreground
    }
    return icon
}



@Composable
fun UISetting(context: Context) {
    rememberSystemUiController().setStatusBarColor(
        Color.Transparent, darkIcons =!isDarkTheme(context)
    )
    rememberSystemUiController().setNavigationBarColor(
        Color.Transparent, darkIcons =!isDarkTheme(context)
    )
    rememberSystemUiController().setSystemBarsColor(
        Color.Transparent, darkIcons =!isDarkTheme(context)
    )
}

fun getSystemPackages( context : Context) : ArrayList<String> {
    var  installedPackageList = ArrayList<String>();
   var  installedPackageInfoList = context.getPackageManager().getInstalledPackages(PackageManager.MATCH_SYSTEM_ONLY);

    for ( packageInfo in installedPackageInfoList) {
        installedPackageList.add(packageInfo.packageName);
    }
    return installedPackageList;
}

fun isSystemApplication( context : Context,  packageName :String ):Boolean{
    var mPackageManager = context.packageManager;
    try {
        var  packageInfo = mPackageManager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
        if((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM)!=0){
            return true;
        } else {
            return false;
        }
    } catch ( e :PackageManager.NameNotFoundException) {
        e.printStackTrace();
    }
    return false;
}

