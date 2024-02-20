package kg.edu.yjut.litenote.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.widget.RemoteViews
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Refresh
import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.activity.MyHomeActivity
import kg.edu.yjut.litenote.bean.Code
import kg.edu.yjut.litenote.utils.CodeDatebaseUtils
import java.security.AccessController.getContext


fun isDarkTheme(
    context: Context,
): Boolean {
    // 判断是否是深色主题
    var isDarkTheme = false
    val currentNightMode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
    isDarkTheme = currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
    return isDarkTheme
}







/**
 * Implementation of App Widget functionality.
 */
class NearExpressWidgetCompose : GlanceAppWidget() {
    var lists = mutableStateListOf<Code>()
    var db: SQLiteDatabase? = null
    var page = mutableStateOf(1)
    fun getWidth(context: Context): Int {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                context,
                NearExpressWidget::class.java
            )
        )
        val options = appWidgetManager.getAppWidgetOptions(appWidgetIds[0])
        return options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
    }
    var c: Context? = null
    @Composable
    @Preview()
    fun MyContent(
        isDarkTheme: Boolean = false,
        width: Int = 300
    ) {
        Row(
            modifier = GlanceModifier.fillMaxSize().background(
                if (isDarkTheme) ColorProvider(Color(0xFF424769)) else ColorProvider(androidx.compose.ui.graphics.Color.White)
            ).padding(15.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // 左边 2/3 显示列表
            Column(
                modifier = GlanceModifier.width(
                    (width * 2/ 3).dp
                ).background(
                    if (isDarkTheme) ColorProvider(Color(0xFF7077A1)) else ColorProvider(Color(0xFFFFE4C9))
                ).cornerRadius(15.dp).fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                lists.forEach {
                    Column(
                        modifier = GlanceModifier.padding(2.dp).fillMaxWidth().padding(2.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text =
                        it.code,
                            maxLines = 1,
                            style = TextStyle(
                                color = if (isDarkTheme) ColorProvider(androidx.compose.ui.graphics.Color.White) else ColorProvider(androidx.compose.ui.graphics.Color.Black),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        Text(text =
                        "${it.kd} - ${it.yz}",
                            maxLines = 1,

                            style = TextStyle(
                                color = if (isDarkTheme) ColorProvider(androidx.compose.ui.graphics.Color.White) else ColorProvider(androidx.compose.ui.graphics.Color.Black),
                                fontSize = 10.sp
                            )
                        )
                    }

                }
            }
            // 右边 1/3 显示按钮
            Column(
                modifier = GlanceModifier.width(
                    (width / 3).dp
                ).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.Bottom
            ) {
                // 点击按钮跳转到主页
                // 点击按钮跳转到主页
                Image(
                    provider = ImageProvider(
                        Icon.createWithResource(
                            "kg.edu.yjut.enhancenoticehyperos",
                            R.drawable.dd
                        )
                    ),
                    contentDescription = "",
                    modifier = GlanceModifier.height(
                        50.dp
                    ).clickable {
                        lists.clear()
                        page.value += 1
                        var list = CodeDatebaseUtils.getAllCodes(
                            db,
                            0,
                            page.value,
                            3
                        )
                        if (list.size == 0) {
                            page.value = 1
                            list = CodeDatebaseUtils.getAllCodes(
                                db,
                                0,
                                page.value,
                                3
                            )
                            lists.addAll(list)
                        }else{
                            lists.addAll(list)
                        }

                        println("点击按钮")
                        println(lists)
                    }

                )
                Text(text = "下一页", style = TextStyle(
                    color = if (isDarkTheme) ColorProvider(androidx.compose.ui.graphics.Color.White) else ColorProvider(androidx.compose.ui.graphics.Color.Black),
                    fontSize = 10.sp
                ))
            }



        }
    }
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        db = CodeDatebaseUtils.openOrCreateDatabase(context)
        lists.addAll(
            CodeDatebaseUtils.getAllCodes(
                db,
                0,
                page.value,
                3
            )
        )
        // 获取小组件宽度、高度
        val width = getWidth(context)
        println("宽度：$width")
        provideContent {
            // create your AppWidget here
            MyContent(
                isDarkTheme = isDarkTheme(context),
                width = width
            )
        }

    }

}



