package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.DatabaseUtils
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.wear.compose.material.Icon
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme
import kg.edu.yjut.litenote.bean.AppInfo
import kg.edu.yjut.litenote.bean.LogBeam
import kg.edu.yjut.litenote.utils.CodeDatebaseUtils
import kg.edu.yjut.litenote.utils.UISetting

fun closeSoftwareNotification(context: Context, packageName: String, channelName: String)  : Boolean{
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.deleteNotificationChannel(channelName)
    notificationManager.deleteNotificationChannelGroup(channelName)
    return true
}

@Composable
@SuppressLint("UnusedMaterial3Api")
@Preview(showBackground = true)
fun LogItem(
            context: Context = LocalContext.current,
            logBeam: LogBeam = LogBeam(
    1,"fdsfdsff","fdgdfgdfg","fgdfgfdg","fgdfgfdg","2024-01-05"
)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
            .padding(10.dp)
    ) {
        Text(
            text = logBeam.title,
            fontSize = 20.sp
        )
        Text(
            text = logBeam.content,
            fontSize = 10.sp
        )

        Text(
            text = "应用包名：${logBeam.packageName}",
            fontSize = 10.sp
        )
        Text(
            text = "通知渠道：${logBeam.channelName}",
            fontSize = 10.sp
        )
        Text(
            text = "通知时间：${logBeam.insertTime}",
            fontSize = 10.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                          // 获取 sharedPreference
                          var  sp = context.getSharedPreferences("application_config", Context.MODE_PRIVATE)
                            // 获取 app 的 信息
                        sp.edit().putBoolean(
                            "${logBeam.packageName}_${logBeam.channelName}",
                            false
                        ).apply()
                },
                modifier = Modifier.padding(5.dp),

            ) {
               Row(
                     verticalAlignment = Alignment.CenterVertically
               ) {
                   Icon(
                       imageVector = Icons.Sharp.Close,
                       contentDescription = "Localized description",
                       tint = Color.White
                   )
                   Text(text = "禁用") }
            }


        }



    }
}

fun isDarkTheme(context: Context) : Boolean{
    return context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
}
class LogActivity : ComponentActivity() {
    var appName = mutableStateOf("")
    lateinit var appIcon : Drawable
    var packageName = mutableStateOf("")
    @SuppressLint("MutableCollectionMutableState")
    var lists = mutableStateListOf<LogBeam> ()
    val  page = mutableStateOf(1)
    val  pageSize = mutableStateOf(10)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageName.value = intent.getStringExtra("packageName")!!


        val applicaton = packageManager.getApplicationInfo(packageName.value, 0)!!
        var db = CodeDatebaseUtils.openOrCreateDatabase(this)
        lists.addAll(CodeDatebaseUtils.getLogsByPackageNameAndTime(db, packageName.value, page.value))

        Log.d("LogActivity", "onCreate: ${lists}")

        WindowCompat.setDecorFitsSystemWindows(window, false)




        // 获取 app 的 信息
        appName.value = packageManager.getApplicationLabel(applicaton).toString()
        appIcon = packageManager.getApplicationIcon(applicaton)
        setContent {
            MaterialTheme {
                UISetting(this@LogActivity)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        floatingActionButton = {
                            Column(    modifier = Modifier
                                .fillMaxWidth(),
                                horizontalAlignment = Alignment.End

                            )
                            {
                                    // 加载更多
                                    Button(
                                        onClick = {
                                            page.value = page.value + 1
                                            var db = CodeDatebaseUtils.openOrCreateDatabase(this@LogActivity)
                                            var list = CodeDatebaseUtils.getLogsByPackageNameAndTime(
                                                db,
                                                packageName.value,
                                                page.value
                                            )
                                            if (list.size > 0) {
                                                lists.addAll(list)
                                            } else {
                                                page.value = page.value - 1
                                                Toast.makeText(this@LogActivity, "没有更多了", Toast.LENGTH_SHORT).show()
                                            }

                                        },
                                        modifier = Modifier.padding(5.dp),
                                    ) {
                                     Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End
                                     ) {
                                         Icon(
                                             imageVector = Icons.Filled.Refresh,
                                             contentDescription = "Localized description"
                                         )
                                         Text(text = "加载更多")
                                     }
                                }

                                Button(
                                    onClick = {
                                        CodeDatebaseUtils.deleteLogsByPackageName(db, packageName.value)
                                        Toast.makeText(this@LogActivity, "清除成功", Toast.LENGTH_SHORT).show()
                                        lists.clear()

                                    },
                                    modifier = Modifier.padding(5.dp),

                                    ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End

                                    ){
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Localized description"
                                        )
                                        Text(text = "清除日志")
                                    }

                                }
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth()
                                .verticalScroll(
                                    rememberScrollState()
                                )
                                .padding(10.dp)
                        ) {
                            Spacer(modifier = Modifier
                                .statusBarsHeight()
                                .fillMaxWidth())
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = appName.value,
                                    fontSize = 30.sp
                                )
                                Image(
                                    painter = rememberDrawablePainter(appIcon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                            for (log in lists) {
                                LogItem(this@LogActivity, log)
                            }







                        }
                    }
                }
            }
        }
    }
}
