package kg.edu.yjut.litenote.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.CalendarContract
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kg.edu.yjut.litenote.MainActivity
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.activity.MyHomeActivity
import kg.edu.yjut.litenote.bean.ChannelInfo
import kg.edu.yjut.litenote.miuiStringToast.MiuiStringToast
import kg.edu.yjut.litenote.miuiStringToast.ToastConfig
import kg.edu.yjut.litenote.utils.CodeDatebaseUtils
import kg.edu.yjut.litenote.utils.MyStoreTools
import kg.edu.yjut.litenote.utils.getIcons
import kg.edu.yjut.litenote.utils.supposedDuration
import kg.edu.yjut.litenote.utils.supposedIconMap
import kg.edu.yjut.litenote.utils.supposedPackageName
import kg.edu.yjut.litenote.utils.suppsedColorStr
import java.util.Calendar
import kotlin.random.Random


class GuardNotificationListenerService : NotificationListenerService() {
    val TAG = GuardNotificationListenerService::class.java.simpleName

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
            println(extras.toString())

            var action = sp_action.getBoolean("com.android.mms", true )
            if (action){
                if (extras != null) {
                    val title = extras.getString("android.title")
                    val content = extras.get("android.text").toString()
                    // 获取通知时间
                    val postTime = sbn.postTime
                    val postTimeStr = postTime.toString()
                    // 获取通知发送者
                    val sender = sbn.notification.tickerText
                    println("title: $title")
                    // 提取出内容中 的 【xxxx】 (公司名)
                    // 到xxxx驿站 (驿站名)
                    // 凭xxxx在、凭xxx至、凭xxx到、凭xxx取 （取件码）

                    var sb = getSharedPreferences("regex_manager", MODE_PRIVATE)

                    var regexCode = sb.getString("qujianRegex", "凭(.*?)在|凭(.*?)至|凭(.*?)到|凭(.*?)取")!!
                    var regexCompany = sb.getString("companyRegex", "【(.*?)】")!!
                    var regexYizhan = sb.getString("yizhanRegex", "到(.*?)驿站")!!
                    val intent = sbn.notification.contentIntent


                    // 匹配公司名
                    val company = regexCompany.toRegex().find(content!!)
                    // 匹配驿站名
                    val yizhan = regexYizhan.toRegex().find(content)
                    // 匹配取件码
                    val code = regexCode.toRegex().find(content)
                    println("company: $company")
                    println("yizhan: $yizhan")
                    println("code: $code")
                    // 如果三者都匹配到了
                    if (company != null && yizhan != null && code != null) {
                        var companyStr = company.value
                        // 去除【】
                        companyStr = companyStr.substring(1, companyStr.length - 1)
                        var yizhanStr = yizhan.value
                        // 去除到
                        yizhanStr = yizhanStr.substring(1, yizhanStr.length)
                        var codeStr = code.value
                        // 去除凭
                        codeStr = codeStr.substring(1, codeStr.length - 1)
                        println("companyStr: $companyStr")
                        println("yizhanStr: $yizhanStr")
                        println("codeStr: $codeStr")
                        var arrayN = arrayOf(companyStr, yizhanStr, codeStr)
                        // 发送焦点通知
                        // 获取系统名称
                        // 转为数组
                        var isWrireRili =   sp_action.getBoolean("mms_isWriteCander", false)
                        if (isWrireRili) {
                            Log.d(TAG, "不写入数据")
                        }else{
                            Log.d(TAG, "写入自有数据库")
                            var db = CodeDatebaseUtils.openOrCreateDatabase(this)
                            var contentValues =  ContentValues()
                            contentValues.put("code", codeStr)

                            contentValues.put("yz", yizhanStr)
                            contentValues.put("kd", companyStr)
                            CodeDatebaseUtils.insertData(db, contentValues)
                        }
                        val dm = resources.displayMetrics
                        val width = dm.widthPixels
                        val height = dm.heightPixels
                        // 判断是否是横屏
                        var isLandscape = width > height
                        // 如果是横屏
                        if (isLandscape) {
                            // 发送横屏通知,横屏通知不可以使用，灵动岛
                            PostNotice(
                                "取件码通知",
                                "取件码：${codeStr}，请前往${yizhanStr}取件",
                                intent,
                                "logo"
                            )

                        } else {
                            // 发送竖屏通知，竖屏可以使用，灵动岛
                            MiuiStringToast.showStringToast(this,  ToastConfig(
                                "取件码：${codeStr}，请前往${yizhanStr}取件",
                                "#1296DB",
                                "dd",
                                6000L,
                                intent
                            ))
                        }


                        // 添加到日历
                        var calendar = Calendar.getInstance()


                        return arrayN
                    }
                    return Array(0, {""})


                }else{
                    return Array(0, {""})
                }
            }else{
                Log.d(TAG, "功能已关闭")
            }
        }
        else{
            // 如果在 supposedPackageName 中能找到
            // 则说明是其他应用的通知 supposedPackageName.contains(packageName)

            if (supposedPackageName.contains(packageName)) {
                var action = sp_action.getBoolean(packageName, true)
                // 获取应用信息


                if (action) {
                    println(extras.toString())
                    if (extras != null) {
                        val title = extras.getString("android.title")
                        val content = extras.getString("android.text")
                        val intent = sbn.notification.contentIntent
                        // 获取通道
                        val channel = sbn.notification.channelId

                        // 检查通道是否存在
                        var isExist = MyStoreTools.checkChannel(this, packageName, channel)
                        if (!isExist) {
                            var channelOpen = true
                            // 如果不存在，添加通道
                            MyStoreTools.addChannel(this, packageName, ChannelInfo(channel, channel))

                        }




                        var channelOpen = sp_action.getBoolean("${packageName}_${channel}", true)
                        if (channelOpen) {
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
                                    PostNotice(
                                        title!!,
                                        content!!,
                                        intent,
                                        supposedIconMap[packageName]!!
                                    )
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

                } else {
                    Log.d(TAG, "功能已关闭")
                }
            }
            else{
                Log.d(TAG, "暂未支持此应用")

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
        var id = "kg.edu.yjut.litenote.service.ZhuanFaService"
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
    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        var id = "kg.edu.yjut.litenote.service.GuardNotificationListenerService"
        var name = "监听服务"
        var description = "GuardNotificationListenerService"

        var intent = Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_MUTABLE)
        var notification: Notification = NotificationCompat.Builder(this, id)
            .setContentTitle("通知监听服务正在启动")
            .setContentText("通知监听服务正在启动，点击进入应用")
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
        var notificationId = 2
        // 显示通知
        startForeground(notificationId, notification1)
        MiuiStringToast.showStringToast(this,  ToastConfig(
            "通知监听服务正在启动",
            "#1296DB",
            "dd",
            4000L,
            null
        ))



    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d(TAG, "onNotificationPosted: ${getMsg(sbn!!)}")
        var arrays = getMsg(sbn)


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
        Log.d(TAG, "onNotificationRemoved: ${getMsg(sbn!!)}")

    }






}