package kg.edu.yjut.litenote

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kg.edu.yjut.litenote.activity.ChecksActivity
import kg.edu.yjut.litenote.activity.MyHomeActivity
import kg.edu.yjut.litenote.activity.UpdateActivity
import kg.edu.yjut.litenote.bean.HttpBeam
import kg.edu.yjut.litenote.helper.RegexMangerHelper
import kg.edu.yjut.litenote.miui.MiuiStringToast
import kg.edu.yjut.litenote.miui.ToastConfig
import kg.edu.yjut.litenote.service.GuardNotificationListenerService
import kg.edu.yjut.litenote.utils.CodeDatebaseUtils
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity() {
    fun isNotificationListenersEnabled(): Boolean {
        val pkgName = packageName
        val flat: String =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        println(flat)
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun checkUpdate(
        context: Context,
    ) {
        // 获取当前版本 code
        val currentVersionCode = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        var url = "https://ota.yjut.edu.kg/checkUpdate?version_code=${currentVersionCode}&apk_type=hyperos"
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val call: Call = okHttpClient.newCall(request)
        Thread {
            try {
                val response: Response = call.execute()
                val result = response.body()!!.string()
                // 解析json Gson
                var gson = Gson()
                var root = gson.fromJson(result, HttpBeam::class.java)
                // 如果code为200，表示请求成功
                if (root.code == 200) {
                    runOnUiThread {
                        if (root.msg == "success") {
                            // 如果有更新
                            var pendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                Intent(context, UpdateActivity::class.java),
                                PendingIntent.FLAG_UPDATE_CURRENT
                            )

                            MiuiStringToast.showStringToast(
                                this, ToastConfig(
                                    "有新版本",
                                    "#1296DB",
                                    "dd",
                                    6000L,
                                    pendingIntent
                                )
                            )
                        }else{
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()

    }

    @RequiresApi(Build.VERSION_CODES.P  )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 隐藏标题栏
        supportActionBar?.hide()
        // 状态栏透明
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        // 导航栏透明
        window.navigationBarColor = Color.TRANSPARENT

        var sp = getSharedPreferences("config", Context.MODE_PRIVATE)
        var isFirst = sp.getBoolean("isFirst", true)

        if (isFirst){

            startActivity(Intent(this, ChecksActivity::class.java))
            finish()

        }else{

            // 检查是否有读取通知权限
            if (!isNotificationListenersEnabled()) {
                startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                var permissions = arrayOf("android.permission.FOREGROUND_SERVICE_DATA_SYNC", "android.permission.POST_NOTIFICATIONS")
                // 检查权限
                var checkSelfPermission = checkSelfPermission(permissions[0])
                if (checkSelfPermission == -1){
                    requestPermissions(permissions, 0)
                }
            }
            var permissions2 = arrayOf("android.permission.POST_NOTIFICATIONS","WRITE_CALENDAR")
            // 检查权限
            var checkSelfPermission2 = checkSelfPermission(permissions2[0])

            if (checkSelfPermission2 == -1){
                requestPermissions(permissions2, 0)
            }

            var permissions3 = arrayOf("WRITE_CALENDAR")
            var checkSelfPermission3 = checkSelfPermission(permissions2[1])
            if (checkSelfPermission3 == -1){
                requestPermissions(permissions3, 0)
            }




            // SharedPreferences
            RegexMangerHelper.initRegexManger(this)
            // 启动服务startForegroundService
            // 让后台服务一直运行
            if (!Settings.canDrawOverlays(this)){
                Toast.makeText(this, "请打开悬浮窗权限", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            }

            var permissions = arrayOf("android.permission.FOREGROUND_SERVICE")
            // 检查权限
            var checkSelfPermission = checkSelfPermission(permissions[0])
            if (checkSelfPermission == -1){
                requestPermissions(permissions, 0)
            }

            startForegroundService(Intent(this, GuardNotificationListenerService::class.java))



            var db = CodeDatebaseUtils.openOrCreateDatabase(this)
            Log.d("MainActivity", "onCreate: $db")
            var out = CodeDatebaseUtils.getNearDate(db)
            println(out)
            Log.d("MainActivity", "onCreate: $out")


            startActivity(Intent(this, MyHomeActivity::class.java))
            finish()
        }






    }
}