package kg.edu.yjut.litenote.bean

import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable

data class AppInfo(
    var appName: String,
    var packageName: String,
    var icon : Drawable
)
data class ActionInfo(
    var packageName: String,
    var actionRouter: String,
    var actionName: String,
    var channels : List<ChannelInfo>
)

data class ShowActionInfo(
    var appName: String,
    var icon : Drawable,
    var packageName: String,
    var actionName: String,
    var actionRouter: String,
    var channels : List<ChannelInfo>


)

