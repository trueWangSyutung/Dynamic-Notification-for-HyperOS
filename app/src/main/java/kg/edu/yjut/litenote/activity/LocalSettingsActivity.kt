package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme
import kg.edu.yjut.litenote.miui.ToastConfig
import kg.edu.yjut.litenote.miui.service.DynamicIslandService

class LocalSettingsActivity : ComponentActivity() {
    fun onStartService(
        config: ToastConfig
    ){
        val sp = getSharedPreferences("data", NotificationListenerService.MODE_PRIVATE)
        // 获取传递给服务的数据 通过 sharePreference
        sp.edit().putString("text", config.text).apply()
        sp.edit().putString("textColor", config.textColor).apply()
        sp.edit().putString("image", config.image).apply()
        sp.edit().putLong("duration", config.duration).apply()
        sp.edit().putString("intent", config.intent.toString()).apply()
        sp.edit().putBoolean("logMode", true).apply()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(this, DynamicIslandService::class.java)
            ContextCompat.startForegroundService(this, intent)
        } else {
            val intent = Intent(this, DynamicIslandService::class.java)
            startService(intent)
        }
    }
    private var sliderPosition = mutableFloatStateOf(0f)
    private var xPosition = mutableFloatStateOf(0f)
    private var yPosition = mutableFloatStateOf(0f)
    private var lastTextSize = mutableFloatStateOf(0f)

    override fun onDestroy() {
        super.onDestroy()
        val newintent = Intent(this@LocalSettingsActivity, DynamicIslandService::class.java)
        stopService(newintent)
        // 重新启动服务
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ServiceCast",
        "UnusedMaterialScaffoldPaddingParameter"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStartService(
            ToastConfig(
                "灵动通知 For HyperOS 通知监听服务已启动",
                "#1296DB",
                "dd",
                6000L,
                null
            )
        )
        var sp = getSharedPreferences("data", NotificationListenerService.MODE_PRIVATE)
        // 获取屏幕的宽高 dp
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val dpi = resources.displayMetrics.density
        val height = wm.defaultDisplay.height
        val width = wm.defaultDisplay.width
        sliderPosition.value = sp.getInt("lastWight", 0).toFloat()
        xPosition.value = sp.getInt("lastX", 0).toFloat()
        yPosition.value = sp.getInt("lastY", 0).toFloat()
        lastTextSize.value = sp.getInt("lastTextSize", 10).toFloat()
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {
                        // 设置 选择条
                        Column(
                            modifier = Modifier.fillMaxSize().padding(30.dp),
                            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Bottom,
                        ) {
                            Text(
                                text = "灵动岛长度\n${(sliderPosition.value)}",
                                color = MaterialTheme.colorScheme.primary
                            )

                            Slider(
                                value = sliderPosition.value/width,
                                onValueChange = {
                                    Log.d("LocalSettingsActivity", "onCreate: ${it}")
                                    sliderPosition.value = it*width
                                    sp.edit().putInt("lastWight", sliderPosition.floatValue.toInt()).apply()
                                    // 终止服务
                                    val newintent = Intent(this@LocalSettingsActivity, DynamicIslandService::class.java)
                                    stopService(newintent)
                                    // 重新启动服务
                                    onStartService(
                                        ToastConfig(
                                            "灵动通知 For HyperOS 通知监听服务已启动",
                                            "#1296DB",
                                            "dd",
                                            6000L,
                                            null
                                        )
                                    )
                                }
                            )

                            Text(
                                text = "水平位置\n${(xPosition.value)}",
                                color = MaterialTheme.colorScheme.primary
                            )

                            Slider(
                                value = xPosition.value/width,
                                valueRange = 0f..0.5f,

                                onValueChange = {
                                    Log.d("LocalSettingsActivity", "onCreate: ${it}")
                                    xPosition.value = it*width
                                    sp.edit().putInt("lastX", xPosition.floatValue.toInt()).apply()
                                    // 终止服务
                                    val newintent = Intent(this@LocalSettingsActivity, DynamicIslandService::class.java)
                                    stopService(newintent)
                                    // 重新启动服务
                                    onStartService(
                                        ToastConfig(
                                            "灵动通知 For HyperOS 通知监听服务已启动",
                                            "#1296DB",
                                            "dd",
                                            6000L,
                                            null
                                        )
                                    )
                                }
                            )

                            Text(
                                text = "垂直位置\n${(yPosition.value)}",
                                color = MaterialTheme.colorScheme.primary
                            )

                            Slider(
                                value = yPosition.value/height,
                                valueRange = 0f..0.5f,
                                onValueChange = {
                                    Log.d("LocalSettingsActivity", "onCreate: ${it}")
                                    yPosition.value = it*height
                                    sp.edit().putInt("lastY", yPosition.floatValue.toInt()).apply()
                                    // 终止服务
                                    val newintent = Intent(this@LocalSettingsActivity, DynamicIslandService::class.java)
                                    stopService(newintent)
                                    // 重新启动服务
                                    onStartService(
                                        ToastConfig(
                                            "灵动通知 For HyperOS 通知监听服务已启动",
                                            "#1296DB",
                                            "dd",
                                            6000L,
                                            null
                                        )
                                    )
                                }
                            )

                            Text(
                                text = "文字大小\n${(lastTextSize.value)}",
                                color = MaterialTheme.colorScheme.primary
                            )

                            Slider(
                                value = lastTextSize.value/20,
                                onValueChange = {
                                    Log.d("LocalSettingsActivity", "onCreate: ${it}")
                                    lastTextSize.value = it*20
                                    sp.edit().putInt("lastTextSize", lastTextSize.floatValue.toInt()).apply()
                                    // 终止服务
                                    val newintent = Intent(this@LocalSettingsActivity, DynamicIslandService::class.java)
                                    stopService(newintent)
                                    // 重新启动服务
                                    onStartService(
                                        ToastConfig(
                                            "灵动通知 For HyperOS 通知监听服务已启动",
                                            "#1296DB",
                                            "dd",
                                            6000L,
                                            null
                                        )
                                    )
                                }
                            )
                        }



                    }

                }
            }
        }
    }
}

