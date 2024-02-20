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
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.activity.MyHomeActivity
import kg.edu.yjut.litenote.utils.CodeDatebaseUtils

/**
 * Implementation of App Widget functionality.
 */
class NearExpressWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NearExpressWidgetCompose()
}


