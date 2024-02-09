package kg.edu.yjut.litenote.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.View
import android.widget.RemoteViews
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.activity.MyHomeActivity
import kg.edu.yjut.litenote.utils.CodeDatebaseUtils

/**
 * Implementation of App Widget functionality.
 */
class NearExpressWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}




@SuppressLint("RemoteViewLayout", "NewApi", "DiscouragedApi")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.near_express_widget)

    var img = RemoteViews(context.packageName, R.id.currImg)


    var db = CodeDatebaseUtils.openOrCreateDatabase(context)
    var list = CodeDatebaseUtils.getNearDateNum(db,4)
    println(list)

    var index = 1
    // 将list中的数据添加到codes_layout中
    for (i in list) {
        var code = i.code
        var text = i.code + " (" + i.yz + ")"
        // 更新组件
        views.setTextViewText(context.resources.getIdentifier("code_" + index, "id", context.packageName), text)
        index++
    }

    if (list.size < 4) {
        // 从 i  到 4
        for (i in index .. 4) {
            views.setViewVisibility(context.resources.getIdentifier("code_${i}_c" , "id", context.packageName), View.GONE)
        }
    }


    appWidgetManager.updateAppWidget(appWidgetId, views)
}

