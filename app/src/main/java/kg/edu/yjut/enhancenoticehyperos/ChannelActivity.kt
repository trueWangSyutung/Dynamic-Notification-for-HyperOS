package kg.edu.yjut.enhancenoticehyperos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.gson.Gson
import kg.edu.yjut.enhancenoticehyperos.bean.ChannelInfo
import kg.edu.yjut.enhancenoticehyperos.utils.MyStoreTools

class ChannelActivity : ComponentActivity() {
    var appName = mutableStateOf("")
    lateinit var appIcon : Drawable
    var packageName = mutableStateOf("")


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var channelStr = intent.getStringExtra("channel")
        // 转为 数组 对象

        packageName.value = intent.getStringExtra("packageName")!!


        val applicaton = packageManager.getApplicationInfo(packageName.value, 0)!!

        // 获取 app 的 信息
        appName.value = packageManager.getApplicationLabel(applicaton).toString()
        appIcon = packageManager.getApplicationIcon(applicaton)

        var sp = getSharedPreferences("application_config", Context.MODE_PRIVATE)
        var channel = MyStoreTools.getChannalList(this, packageName.value)

        var that = this
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold { padding ->
                        Column(
                            modifier = Modifier.padding(padding)
                                .fillMaxWidth()
                                .verticalScroll(
                                    rememberScrollState()
                                )
                                .padding(10.dp)
                        ) {
                            Spacer(modifier = Modifier
                                .fillMaxWidth())
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()

                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Image(
                                    painter = rememberDrawablePainter(drawable = appIcon),
                                    contentDescription = "appIcon"
                                )
                                Text(
                                    text = "${appName.value}",
                                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                    fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                                )
                                Text(text = "${packageName.value}")

                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()

                            ) {
                                for (index in 0 until channel.size){
                                    val item = channel[index]
                                    var checked = remember {
                                        mutableStateOf(
                                            sp.getBoolean(
                                                "${packageName.value}_${item.channelName}",
                                                true
                                            )
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = item.channelStr)

                                        Switch(checked = checked.value, onCheckedChange = {
                                            checked.value = it
                                            sp.edit().putBoolean(
                                                "${packageName.value}_${item.channelName}",
                                                it
                                            ).apply()

                                        })

                                    }
                                }


                            }
                        }
                    }
                }
            }
        }
    }
}

