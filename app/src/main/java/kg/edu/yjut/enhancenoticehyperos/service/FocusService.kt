/*
 * Copyright (C) 2024 The LiteNote Project
 * @author OpenAcademic
 * @version 1.0
 * 
 */
package  kg.edu.yjut.enhancenoticehyperos.service

import android.app.Service
import android.content.Intent
import android.os.IBinder


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kg.edu.yjut.enhancenoticehyperos.MainActivity
import kg.edu.yjut.enhancenoticehyperos.R
import kg.edu.yjut.enhancenoticehyperos.miui.devicesSDK.isLandscape


class FocusService : Service() {

    private val TAG = FocusService::class.java.simpleName
    private var mFloatWindowManager: FocusWindowManager? = null
    private var mHomeKeyObserverReceiver: HomeKeyObserverReceiver? = null
    private var logMode = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (mFloatWindowManager != null) {
            // 清除
            mFloatWindowManager!!.removeWindow()
        }

        mFloatWindowManager = FocusWindowManager(applicationContext)
        mHomeKeyObserverReceiver = HomeKeyObserverReceiver()
        // 获取传递给服务的数据 通过 sharePreference
        val sp = getSharedPreferences("curr_data", MODE_PRIVATE)

        val config = CodeConfig(
            intent?.getStringExtra("text")!!,
            intent.getStringExtra("express")!!,
            intent.getIntExtra("type", 0),
            intent.getStringExtra("from")!!
        )


        logMode = intent.getBooleanExtra("logMode", false)
        registerReceiver(mHomeKeyObserverReceiver, IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS),
            RECEIVER_NOT_EXPORTED
        )
        mFloatWindowManager!!.createWindow(
            config = config,
            function = {
                stopSelf()
            },
            copy = {
                // 写入剪切板
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = android.content.ClipData.newPlainText("text", config.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "已复制到剪切板", Toast.LENGTH_SHORT).show()
                stopSelf()

            },
            save = {
                // 保存到数据库

                stopSelf()

            },
            landscape = isLandscape(applicationContext)
        )
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mFloatWindowManager?.removeWindow()
        if (mHomeKeyObserverReceiver != null) {
            unregisterReceiver(mHomeKeyObserverReceiver)
        }
    }

    override fun onCreate() {
        var id = "service.FocusService"
        var name = "取件码服务"
        var description = "FocusService"



        var intent = Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        var notification: Notification = NotificationCompat.Builder(this, id)
            .setContentTitle("取件码服务正在启动")
            .setContentText("取件码服务正在启动，点击进入应用")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.dd)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.dd))
            .setContentIntent(pendingIntent)
            .build()


        var manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        var channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true);
        channel.setShowBadge(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            channel.lockscreenVisibility = Notification.FOREGROUND_SERVICE_DEFAULT;
        }else{
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC;
        }
        manager.createNotificationChannel(channel);

        var notification1 = notification;
        // 通知id
        var notificationId = 4
        // 显示通知
        startForeground(notificationId, notification1)




    }
}