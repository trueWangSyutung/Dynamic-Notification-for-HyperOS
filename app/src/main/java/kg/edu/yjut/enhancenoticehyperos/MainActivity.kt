package kg.edu.yjut.enhancenoticehyperos

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.edu.yjut.enhancenoticehyperos.ui.theme.DynamicNotificationTheme
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeBackgroundColor
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeTextColor
import kg.edu.yjut.enhancenoticehyperos.service.GuardNotificationListenerService
import kg.edu.yjut.enhancenoticehyperos.utils.Configs
import kg.edu.yjut.enhancenoticehyperos.widget.EasyDialog

class MainActivity : ComponentActivity() {
    val TAG = "MainActivity"
    val isAgreed = mutableStateOf(false)
    val isFirstLaunch = mutableStateOf(true)
    val canDrawOverlays = mutableStateOf(false)
    val foregroundService = mutableStateOf(false)
    val postNotifications = mutableStateOf(false)
    val isNotificationListenersEnabled = mutableStateOf(false)
    val isServiceRunning = mutableStateOf(false)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun initYubei() {
        canDrawOverlays.value = Settings.canDrawOverlays(this)
        foregroundService.value =( checkSelfPermission(android.Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED )
        Log.d(TAG, "foregroundService: ${checkSelfPermission(android.Manifest.permission.FOREGROUND_SERVICE)}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            postNotifications.value = ( checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED )
        } else {
            postNotifications.value = true
        }
        isNotificationListenersEnabled.value = isNotificationListenersEnabled()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun initService() {
        if (isNotificationListenersEnabled.value && canDrawOverlays.value && foregroundService.value && postNotifications.value) {
            startForegroundService(Intent(this, GuardNotificationListenerService::class.java))
            // 检查 GuardNotificationListenerService 是否正在运行
            if (isServiceRunning()) {
                isServiceRunning.value = true

            } else {
                startForegroundService(Intent(this, GuardNotificationListenerService::class.java))

            }
        }

    }
    private fun isServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as  (ActivityManager);
        val serviceClass = GuardNotificationListenerService::class.java;
        for ( service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true;
            }
        }
        return false;
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        isAgreed.value = Configs.checkAgreeUser(this@MainActivity)
        isFirstLaunch.value = Configs.checkFirstLaunch(this@MainActivity)
        if (isAgreed.value) {
            initYubei()
            initService()
        }


    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DynamicNotificationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EasyDialog(
                        modifier = Modifier
                            .width(300.dp)
                            .height(400.dp)
                            .background(
                                getDarkModeBackgroundColor(this@MainActivity, 1),
                                RoundedCornerShape(16.dp)
                            )
                            .clip(RoundedCornerShape(16.dp)),
                        show = isFirstLaunch.value,
                        onDismissRequest = {
                            Configs.setFirstLaunch(this@MainActivity, false)
                            isFirstLaunch.value = false
                        },
                        content = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                                    .fillMaxHeight()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.8f)
                                ) {
                                    Web("file:///android_asset/ysxy.html")
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    TextButton(onClick = {
                                        Configs.setFirstLaunch(this@MainActivity, false)
                                        isFirstLaunch.value = false
                                    }) {
                                        Text(text = "拒绝", color = getDarkModeTextColor(this@MainActivity))
                                    }
                                    TextButton(onClick = {
                                        Configs.setAgreeUser(this@MainActivity, true)
                                        isAgreed.value = true
                                        isFirstLaunch.value = false
                                        Configs.setFirstLaunch(this@MainActivity, false)
                                        initYubei()
                                        initService()
                                    }) {
                                        Text(text = "同意", color = getDarkModeTextColor(this@MainActivity))
                                    }
                                    
                                }


                            }
                        }
                    )
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(
                                Color.Transparent
                            ).verticalScroll(rememberScrollState())
                            .padding(
                                20.dp
                            )
                    ) {
                        Text(
                            resources.getString(R.string.app_name),
                            color = getDarkModeTextColor(this@MainActivity),
                            fontSize = 40.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    15.dp
                                ),
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold
                        )
                        if (isServiceRunning.value) {
                            Row(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .background(
                                        Color(0xFF13227A),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(10.dp)
                                    .fillMaxWidth()
                                    .height(120.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_play_circle_outline_24),
                                    contentDescription = "Notifications Active",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 10.dp)
                                )
                                Text(
                                    "服务正在运行",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = 15.dp
                                        ),
                                    maxLines = 1,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 15.dp, vertical = 2.dp)
                                    .background(
                                        getDarkModeBackgroundColor(
                                            context = this@MainActivity,
                                            level = 1
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(10.dp)
                                    .clickable {
                                        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .fillMaxWidth()
                                    .height(120.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                            ){
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                    tint = getDarkModeTextColor(this@MainActivity)
                                )
                                Text(
                                    "配置中心",
                                    color = getDarkModeTextColor(this@MainActivity),
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = 15.dp
                                        ),
                                    maxLines = 1,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold
                                )

                            }
                        }
                        else{
                            Row(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .background(
                                        getDarkModeBackgroundColor(
                                            context = this@MainActivity,
                                            level = 1
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(10.dp)
                                    .clickable {
                                        initYubei()
                                    }
                                    .fillMaxWidth()
                                    .height(120.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                            ){
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_pause_circle_outline_24) ,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                    tint = getDarkModeTextColor(this@MainActivity)
                                )
                                Text(
                                    "服务未运行",
                                    color = getDarkModeTextColor(this@MainActivity),
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = 15.dp
                                        ),
                                    maxLines = 1,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold
                                )

                            }
                            if (!isAgreed.value){
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 15.dp, vertical = 5.dp)
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            isFirstLaunch.value = true
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                ){
                                    Icon(
                                        painter = painterResource(id = R.drawable.init) ,
                                        contentDescription = null,
                                        modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                        tint = getDarkModeTextColor(this@MainActivity)
                                    )
                                    Text(
                                        "您还未曾同意用户的隐私协议",
                                        color = getDarkModeTextColor(this@MainActivity),
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()

                                            .padding(
                                                vertical = 15.dp
                                            ),
                                        maxLines = 1,
                                        textAlign = TextAlign.Start,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            else{
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 15.dp, vertical = 2.dp)
                                        .background(
                                            getDarkModeBackgroundColor(
                                                context = this@MainActivity,
                                                level = 1
                                            ),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(10.dp)
                                        .clickable {
                                            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                ){
                                    Icon(
                                        imageVector = Icons.Outlined.Settings,
                                        contentDescription = null,
                                        modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                        tint = getDarkModeTextColor(this@MainActivity)
                                    )
                                    Text(
                                        "配置中心",
                                        color = getDarkModeTextColor(this@MainActivity),
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                vertical = 15.dp
                                            ),
                                        maxLines = 1,
                                        textAlign = TextAlign.Start,
                                        fontWeight = FontWeight.Bold
                                    )

                                }
                                if (!canDrawOverlays.value) {
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 25.dp, vertical = 5.dp)

                                            .fillMaxWidth()
                                            .clickable {
                                                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Icon(
                                            painter = painterResource(id = R.drawable.xfc) ,
                                            contentDescription = null,
                                            modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                            tint = getDarkModeTextColor(this@MainActivity)
                                        )
                                        Text(
                                            "请授予悬浮窗权限",
                                            color = getDarkModeTextColor(this@MainActivity),
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()

                                                .padding(
                                                    vertical = 15.dp
                                                ),
                                            maxLines = 1,
                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                if (!foregroundService.value) {
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 25.dp, vertical = 5.dp)
                                            .fillMaxWidth()
                                            .clickable {
                                                requestPermissions(
                                                    arrayOf(android.Manifest.permission.FOREGROUND_SERVICE),
                                                    1
                                                )
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Icon(
                                            painter = painterResource(id = R.drawable.tz) ,
                                            contentDescription = null,
                                            modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                            tint = getDarkModeTextColor(this@MainActivity)
                                        )
                                        Text(
                                            "请授予前台服务权限",
                                            color = getDarkModeTextColor(this@MainActivity),
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    vertical = 15.dp
                                                ),
                                            maxLines = 1,
                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }


                                }
                                if (!isNotificationListenersEnabled.value){
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 25.dp, vertical = 5.dp)

                                            .fillMaxWidth()
                                            .clickable {
                                                val intent =
                                                    Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                                                startActivity(intent)
                                                requestPermissions(
                                                    arrayOf(android.Manifest.permission.FOREGROUND_SERVICE),
                                                    1
                                                )

                                                //requestPermissions(arrayOf("android.permission.POST_NOTIFICATIONS"), 2)
                                            }
                                        ,
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.tz) ,
                                            contentDescription = null,
                                            modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                            tint = getDarkModeTextColor(this@MainActivity)
                                        )
                                        Text(
                                            "请授予通知监听权限",
                                            color = getDarkModeTextColor(this@MainActivity),
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    vertical = 15.dp
                                                ),
                                            maxLines = 1,
                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                if (!postNotifications.value) {
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 25.dp, vertical = 5.dp)

                                            .fillMaxWidth()
                                            .clickable {
                                                requestPermissions(
                                                    arrayOf("android.permission.POST_NOTIFICATIONS"),
                                                    2
                                                )
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Icon(
                                            painter = painterResource(id = R.drawable.tz) ,
                                            contentDescription = null,
                                            modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                            tint = getDarkModeTextColor(this@MainActivity)
                                        )
                                        Text(
                                            "请授予通知权限",
                                            color = getDarkModeTextColor(this@MainActivity),
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    vertical = 15.dp
                                                ),
                                            maxLines = 1,
                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                        }


                        // 隐私协议
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 25.dp, vertical = 5.dp)

                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(
                                        this@MainActivity,
                                        PrivacyAgreementActivity::class.java
                                    )
                                    startActivity(intent)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.pa) ,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                tint = getDarkModeTextColor(this@MainActivity)
                            )
                            Text(
                                "隐私协议",
                                color = getDarkModeTextColor(this@MainActivity),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 15.dp
                                    ),
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // 用户协议
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 25.dp, vertical = 5.dp)

                                .fillMaxWidth()
                                .clickable {
                                    val intent =
                                        Intent(this@MainActivity, UserAgreementActivity::class.java)
                                    startActivity(intent)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.ua) ,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                tint = getDarkModeTextColor(this@MainActivity)
                            )
                            Text(
                                "用户协议",
                                color = getDarkModeTextColor(this@MainActivity),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 15.dp
                                    ),
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 25.dp, vertical = 5.dp)

                                .fillMaxWidth()
                                .clickable {
                                    val intent =
                                        Intent(this@MainActivity, AboutActivity::class.java)
                                    startActivity(intent)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.about) ,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp).padding(end = 10.dp),
                                tint = getDarkModeTextColor(this@MainActivity)
                            )
                            Text(
                                "关于" + resources.getString(R.string.app_name),
                                color = getDarkModeTextColor(this@MainActivity),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 15.dp
                                    ),
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold
                            )
                        }



                    }
                }
            }
        }
    }
}
