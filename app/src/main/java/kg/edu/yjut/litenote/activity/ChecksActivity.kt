package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Colors
import kg.edu.yjut.litenote.MainActivity
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme
import java.lang.Exception

@Composable
@Preview(showBackground = true)
fun ItemButton(
    text: String = "悬浮窗权限",
    isOK : Boolean = true,
    click: () -> Unit = {}
) {
    var ok  =  remember { mutableStateOf(isOK) }
    IconButton(
        onClick = click,
        modifier = Modifier
            .padding(10.dp, 1.dp, 10.dp, 1.dp)
            .fillMaxWidth()
            .height(60.dp)

    ){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

    ) {
                Text(text = text, fontSize = 14.sp)
                // 根据 ok 的值显示不同的图标，图标在 res/mipmap 文件夹下
                Icon(
                    painter = if (ok.value) painterResource(id = R.mipmap.yes) else painterResource(id = R.mipmap.no),
                    contentDescription = null,
                    modifier = Modifier
                        .width(50.dp)
                        .fillMaxHeight(),
                    tint = if (ok.value) Color.Green else Color.Red
                )
        }

    }

}
@Composable
@Preview(showBackground = true)
fun StartUse(
    click: () -> Unit = {}
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
         IconButton(
             onClick = click,
             modifier = Modifier
                 .width(60.dp)
                 .height(60.dp)
                 .background(Color.LightGray, MaterialTheme.shapes.extraLarge)
         ) {
             Text(text = "开始", color = Color.White)
         }
    }
}


class ChecksActivity : ComponentActivity() {
    var isAgreeUA = false
    var isAgreePA = false
    var isNotificationListenersEnabledFlag = false
    var isCanDrawOverlays = false
    fun isNotificationListenersEnabled(): Boolean {
        val pkgName = packageName
        // 如果 api
        val flat: String =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onRestart() {

        super.onRestart()
        var sp = getSharedPreferences("config", MODE_PRIVATE)
        isAgreeUA = sp.getBoolean("isAgreeUA", false)
        isAgreePA = sp.getBoolean("isAgreePA", false)
        isNotificationListenersEnabledFlag = isNotificationListenersEnabled()
        isCanDrawOverlays = Settings.canDrawOverlays(this)

    }

    override fun onResume() {
        super.onResume()
        var sp = getSharedPreferences("config", MODE_PRIVATE)
        isAgreeUA = sp.getBoolean("isAgreeUA", false)
        isAgreePA = sp.getBoolean("isAgreePA", false)
        isNotificationListenersEnabledFlag = isNotificationListenersEnabled()
        isCanDrawOverlays = Settings.canDrawOverlays(this)


    }
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sp = getSharedPreferences("config", MODE_PRIVATE)
        isAgreeUA = sp.getBoolean("isAgreeUA", false)
        isAgreePA = sp.getBoolean("isAgreePA", false)
        isNotificationListenersEnabledFlag = isNotificationListenersEnabled()
        isCanDrawOverlays = Settings.canDrawOverlays(this)

        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .verticalScroll(
                                    rememberScrollState()
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                        // 显示图片 assets 文件夹下的图片
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.logo_color
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(200.dp),

                                )
                        }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                               Text(
                                        text = "欢迎使用 LiteNote(取件码)",
                                        fontSize = 20.sp
                                    )
                            }

                        // 显示菜单
                           AnimatedVisibility(
                               visible =  !isAgreePA || !isAgreeUA  || !isNotificationListenersEnabledFlag   || !isCanDrawOverlays
                           ) {
                               Text(
                                   text = "第一次使用将为您检查权限",
                                   fontSize = 20.sp,
                                   color = Color.White
                               )
                                   Column(
                                       modifier = Modifier
                                           .fillMaxWidth()
                                           .background(Color.LightGray, MaterialTheme.shapes.medium),

                                   ) {
                                   ItemButton(
                                       text = "用户协议",
                                       isOK = isAgreeUA
                                   ) {
                                       startActivity(Intent(this@ChecksActivity, UserAgreementActivity::class.java))
                                       finish()
                                   }
                                   ItemButton(
                                       text = "隐私协议",
                                       isOK = isAgreePA
                                   ) {
                                       startActivity(Intent(this@ChecksActivity, PrivacyAgreementActivity::class.java))
                                       finish()
                                   }
                                   ItemButton(
                                       text = "通知监听权限",
                                       isOK = isNotificationListenersEnabledFlag
                                   ) {
                                       startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                                   }
                                   ItemButton(
                                       text = "悬浮窗权限",
                                       isOK = isCanDrawOverlays
                                   ) {
                                       startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
                                   }
                               }
                           }

                            AnimatedVisibility(
                                visible =  isAgreePA && isAgreeUA  && isNotificationListenersEnabledFlag   && isCanDrawOverlays
                            ) {

                                StartUse {
                                    var sp = getSharedPreferences("config", MODE_PRIVATE)
                                    var editor = sp.edit()
                                    editor.putBoolean("isFirst", false)
                                    editor.apply()

                                    startActivity(Intent(this@ChecksActivity, MainActivity::class.java))
                                    finish()
                                }

                            }

                            // 显示提示信息
                            Text(
                                modifier = Modifier.padding(0.dp,15.dp,0.dp,0.dp),
                                text = "为了保证服务的正常运行，请您务必开启通知读取权限、自启动权限、后台无限制权限、该应用不申请网络权限、您的所有通知信息将不会上传到服务器",
                                fontWeight = FontWeight(2)
                            )

                        }


                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    LiteNoteTheme {
        Greeting2("Android")
    }
}