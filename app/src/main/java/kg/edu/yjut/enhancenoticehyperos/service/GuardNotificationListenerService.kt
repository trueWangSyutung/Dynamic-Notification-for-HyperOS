package kg.edu.yjut.enhancenoticehyperos.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kg.edu.yjut.enhancenoticehyperos.MainActivity
import kg.edu.yjut.enhancenoticehyperos.R
import kg.edu.yjut.enhancenoticehyperos.utils.MyStoreTools
import kg.edu.yjut.enhancenoticehyperos.utils.getIcons
import kg.edu.yjut.enhancenoticehyperos.utils.getSystemPackages
import kg.edu.yjut.enhancenoticehyperos.utils.supposedDuration
import kg.edu.yjut.enhancenoticehyperos.utils.supposedIconMap
import kg.edu.yjut.enhancenoticehyperos.utils.supposedPackageName
import kg.edu.yjut.enhancenoticehyperos.utils.suppsedColorStr

import kg.edu.yjut.enhancenoticehyperos.bean.ChannelInfo
import kg.edu.yjut.enhancenoticehyperos.miui.MiuiStringToast
import kg.edu.yjut.enhancenoticehyperos.miui.ToastConfig
import kg.edu.yjut.enhancenoticehyperos.miui.devicesSDK.isUnHyperOSNotices
import kg.edu.yjut.enhancenoticehyperos.utils.Configs
import kg.edu.yjut.enhancenoticehyperos.utils.SoftwareMode

import kotlin.random.Random


class GuardNotificationListenerService : NotificationListenerService() {
    val TAG = GuardNotificationListenerService::class.java.simpleName
    var syatemApplicationsPackageName = ArrayList<String>()

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()

    }

    override fun startForegroundService(service: Intent?): ComponentName? {

        return super.startForegroundService(service)
    }
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    private fun getMsg(sbn: StatusBarNotification): Array<String> {
        val extras = sbn.notification.extras

        val packageName = sbn.packageName
        println("packageName: $packageName")
        var sp_action =getSharedPreferences("application_config", Context.MODE_PRIVATE)
        Log.d(TAG, "getMsg: ${extras.toString()}")
        Log.d(TAG, "getMsg: ${sbn.toString()}")



        if (packageName.equals( "com.android.mms")) {
            Log.d(TAG, "extras: ${extras.getBundle("android.messages")}")

        }

        else{
            // 如果在 supposedPackageName 中能找到
            // 则说明是其他应用的通知 supposedPackageName.contains(packageName)
            if (supposedPackageName.contains(packageName)) {
                // 获取应用信息
                println(extras.toString())
                if (extras != null) {
                    var title = extras.getString("android.title")
                    var content = extras.getString("android.text")
                    var app = packageManager.getApplicationInfo(packageName, 0)

                    if (title == null) {
                        title = app.loadLabel(packageManager).toString()
                    }
                    if (content == null) {
                        content = "发来一条消息"
                    }
                    var intent = sbn.notification.contentIntent


                    // 获取通道
                    val channel = sbn.notification.channelId

                    // 检查通道是否存在
                    var isExist = MyStoreTools.checkChannel(this, packageName, channel)
                    if (!isExist) {
                        var channelOpen = true
                        // 如果不存在，添加通道
                        MyStoreTools.addChannel(this, packageName, ChannelInfo(channel, channel))

                    }
                    // 获取通道是否开启
                    var channelOpen = sp_action.getBoolean("${packageName}_${channel}", true)
                    if (channelOpen) {
                        // 取消该通知的 在屏幕上弹出
                        cancelNotification(sbn.key)

                        // 获取通知时间
                        val postTime = sbn.postTime
                        //  检测 当前 屏幕 宽高
                        val dm = resources.displayMetrics
                        val width = dm.widthPixels
                        val height = dm.heightPixels
                        // 判断是否是横屏
                        var isLandscape = width > height
                        // 如果是横屏
                        if (isLandscape) {
                            // 发送横屏通知,横屏通知不可以使用，灵动岛
                            // 检查 是否是 平板
                            var isPad = MyStoreTools.isPad(this)

                            if (isPad) {
                                MiuiStringToast.showStringToast(
                                    this, ToastConfig(
                                        "${title} : ${content}",
                                        suppsedColorStr[packageName]!!,
                                        supposedIconMap[packageName]!!,
                                        supposedDuration[packageName]!!,
                                        intent
                                    )
                                )
                            }else{
                                if (isUnHyperOSNotices(context = this)) {
                                    MiuiStringToast.showStringToast(
                                        this, ToastConfig(
                                            "${title} : ${content}",
                                            suppsedColorStr[packageName]!!,
                                            supposedIconMap[packageName]!!,
                                            supposedDuration[packageName]!!,
                                            intent
                                        )
                                    )
                                }else {
                                    MiuiStringToast.showStringToast(
                                        this, ToastConfig(
                                            "${title} : ${content}",
                                            "#FFFFFF",
                                            "logo",
                                            3000L,
                                            intent
                                        )
                                    )
                                }
                            }



                        } else {
                            // 发送竖屏通知，竖屏可以使用，灵动岛
                            MiuiStringToast.showStringToast(
                                this, ToastConfig(
                                    "${title} : ${content}",
                                    suppsedColorStr[packageName]!!,
                                    supposedIconMap[packageName]!!,
                                    supposedDuration[packageName]!!,
                                    intent
                                )
                            )
                        }
                    }else{
                        Log.d(TAG, "通道已关闭")

                    }

                }

            }
            else {
                // 如果在 syatemApplicationsPackageName 中能找到
                if (syatemApplicationsPackageName.contains(packageName)) {
                    Log.d(TAG, "暂未支持此应用")
                    return  Array(0, {""})
                }

                if (Configs.getSoftwareMode(context = this) == SoftwareMode.CompatibleMode){
                    Log.d(TAG, "暂未支持此应用")
                    return  Array(0, {""})
                }

                var action = sp_action.getBoolean(packageName, true)
                // 获取应用信息
                if (action) {
                    println(extras.toString())
                    if (extras != null) {
                        var title = extras.getString("android.title")
                        var content = extras.getString("android.text")
                        var app = packageManager.getApplicationInfo(packageName, 0)

                        if (title == null) {
                            title = app.loadLabel(packageManager).toString()
                        }
                        if (content == null) {
                            content = "发来一条消息"
                        }
                        var intent = sbn.notification.contentIntent
                        // 获取通道
                        if (intent == null) {
                            // 打开应用  packageName
                            intent = PendingIntent.getActivity(this, 0, packageManager.getLaunchIntentForPackage(packageName),
                                PendingIntent.FLAG_IMMUTABLE)

                        }

                        val channel = sbn.notification.channelId

                        // 检查通道是否存在
                        var isExist = MyStoreTools.checkChannel(this, packageName, channel)
                        if (!isExist) {
                            var channelOpen = true
                            // 如果不存在，添加通道
                            MyStoreTools.addChannel(this, packageName, ChannelInfo(channel, channel))

                        }
                        // 获取通道是否开启
                        var channelOpen = sp_action.getBoolean("${packageName}_${channel}", true)
                        if (channelOpen) {
                            // 取消该通知的 在屏幕上弹出
                            cancelNotification(sbn.key)
                            // 目的是 接管通知，但不弹出，而是在屏幕上显示一个自定义的视图。

                            // 获取通知时间
                            val postTime = sbn.postTime
                            //  检测 当前 屏幕 宽高
                            val dm = resources.displayMetrics
                            val width = dm.widthPixels
                            val height = dm.heightPixels
                            // 判断是否是横屏
                            var isLandscape = width > height
                            // 如果是横屏
                            if (isLandscape) {
                                // 发送横屏通知,横屏通知不可以使用，灵动岛
                                // 检查 是否是 平板
                                var isPad = MyStoreTools.isPad(this)

                                if (isPad) {
                                    MiuiStringToast.showStringToast(
                                        this, ToastConfig(
                                            "${title} : ${content}",
                                            "#FFFFFF",
                                            "logo",
                                            3000L,
                                            intent
                                        )
                                    )
                                }else{
                                    MiuiStringToast.showStringToast(
                                        this, ToastConfig(
                                            "${title} : ${content}",
                                            "#FFFFFF",
                                            "logo",
                                            3000L,
                                            intent
                                        )
                                    )
                                }



                            } else {
                                // 发送竖屏通知，竖屏可以使用，灵动岛
                                MiuiStringToast.showStringToast(
                                    this, ToastConfig(
                                        "${title} : ${content}",
                                        "#FFFFFF",
                                        "logo",
                                        3000L,
                                        intent
                                    )
                                )
                            }
                        }else{
                            Log.d(TAG, "通道已关闭")

                        }

                    }

                } else {
                    Log.d(TAG, "功能已关闭")
                }
            }


        }


        return Array(0, {""})
    }


    // 发送焦点通知，告诉用户有新的快递需要取
    @SuppressLint("ObsoleteSdkInt")
    fun PostNotice(
        title : String,
        content: String,
        intent: PendingIntent?,
        icon : String,

        ){
        // 通知栏显示
        var id = "kg.edu.yjut.enhancenoticehyperos.service.ZhuanFaService"
        var name = "横屏通知"
        var description = "横屏通知"

        var notification: Notification = NotificationCompat.Builder(this, id)
            .setContentTitle(title)
            .setContentText(content)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(getIcons(icon))
            .setLargeIcon(BitmapFactory.decodeResource(resources, getIcons(icon)))

            .setContentIntent(intent)
            .build()

        var manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC;
            // 悬浮通知
            channel.canBypassDnd()
            // 声音
            channel.setSound(null, null)
            // v震动
            channel.enableVibration(true)

            manager.createNotificationChannel(channel);
        }

        // 通知id, 随机化
        var notificationId = Random(1000).nextInt()
        // 显示通知
        manager.notify(notificationId, notification)

    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        var id = "kg.edu.yjut.enhancenoticehyperos.service.GuardNotificationListenerService"
        var name = "监听服务"
        var description = "GuardNotificationListenerService"



        var intent = Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_MUTABLE)
        var notification: Notification = NotificationCompat.Builder(this, id)
            .setContentTitle("通知监听服务正在启动")
            .setContentText("通知监听服务正在启动，点击进入应用")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
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
        var notificationId = 2
        // 显示通知
        startForeground(notificationId, notification1)


        // 获取系统应用
        syatemApplicationsPackageName = getSystemPackages(this)
        syatemApplicationsPackageName.add("kg.edu.yjut.enhancenoticehyperos")
        //  为了过滤掉该应用本身的通知
        Log.d(TAG, "onCreate: $syatemApplicationsPackageName")

        MiuiStringToast.showStringToast(
            this, ToastConfig(
                "灵动通知 For HyperOS 通知监听服务已启动",
                "#1296DB",
                "dd",
                6000L,
                pendingIntent
            )
        )



    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        var arrays = getMsg(sbn!!)


    }

    fun MIUIStrongToast(text:String){
        // 反射 com.android.systemui 中 toast包中的 MIUIStrongToast 类
        // 通过反射调用 MIUIStrongToast 类中的 show 方法

        var miuiToast = Class.forName("com.android.systemui.toast.MIUIStrongToast")
        var miuiToastConstructor = miuiToast.getDeclaredConstructor(Context::class.java)
        var miuiToastInstance = miuiToastConstructor.newInstance(this)
        var showMethod = miuiToast.getMethod("showCustomStrongToast", String::class.java)
        showMethod.invoke(miuiToastInstance, text)

        // 反射 com.android.systemui 中 toast.bean 包中的 StrongToastModel 类

        var strongToastModel = Class.forName("com.android.systemui.toast.bean.StrongToastModel")
        var strongToastModelConstructor = strongToastModel.getDeclaredConstructor(String::class.java, Int::class.java)
        var strongToastModelInstance = strongToastModelConstructor.newInstance(
            // packageName
            this.packageName,

            )
        var showMethod1 = miuiToast.getMethod("showCustomStrongToast", strongToastModel)




    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)

    }






}