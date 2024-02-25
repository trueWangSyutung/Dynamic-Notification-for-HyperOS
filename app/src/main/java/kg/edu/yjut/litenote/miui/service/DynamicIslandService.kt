package kg.edu.yjut.litenote.miui.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kg.edu.yjut.litenote.MainActivity
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.miui.ToastConfig

class DynamicIslandService : Service() {

    private val TAG = DynamicIslandService::class.java.simpleName
    private var mFloatWindowManager: FloatWindowManager? = null
    private var mHomeKeyObserverReceiver: HomeKeyObserverReceiver? = null
    private var logMode = false
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        var id = "service.DynamicIslandService"
        var name = "灵动岛服务"
        var description = "GuardNotificationListenerService"



        var intent = Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        var notification: Notification = NotificationCompat.Builder(this, id)
            .setContentTitle("灵动岛服务正在启动")
            .setContentText("灵动岛服务正在启动，点击进入应用")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_kuaidi)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_kuaidi))
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
        var notificationId = 3
        // 显示通知
        startForeground(notificationId, notification1)



        mFloatWindowManager = FloatWindowManager(applicationContext)
        mHomeKeyObserverReceiver = HomeKeyObserverReceiver()
        // 获取传递给服务的数据 通过 sharePreference
        val sp = getSharedPreferences("data", MODE_PRIVATE)
        var pendingIntentText = sp.getString("intent", "default")
        // 将 toSting var转换为 PendingIntent
        var my = PendingIntent.getActivity(this, 0, Intent(pendingIntentText), FLAG_IMMUTABLE)

        val config = ToastConfig(
            sp.getString("text", "default")!!,
            sp.getString("textColor", "default")!!,
            sp.getString("image", "default")!!,
            sp.getLong("duration", 3000L),
            null
        )

        logMode = sp.getBoolean("logMode", false)


        registerReceiver(mHomeKeyObserverReceiver, IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS),
            RECEIVER_NOT_EXPORTED
        )
        mFloatWindowManager!!.createWindow(
            config,
            logMode
        )

        if (!logMode) {
            // 等待 3 秒后，移除悬浮窗
            Thread {
                Thread.sleep(config.duration)
                // 结束服务
                stopSelf()
            }.start()

        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mFloatWindowManager?.removeWindow()
        if (mHomeKeyObserverReceiver != null) {
            unregisterReceiver(mHomeKeyObserverReceiver)
        }
    }


}