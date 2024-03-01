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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import kg.edu.yjut.litenote.miui.devicesSDK.isLandscape
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

    private var sliderPositionHP = mutableFloatStateOf(0f)
    private var xPositionHP = mutableFloatStateOf(0f)
    private var yPositionHP = mutableFloatStateOf(0f)
    private var lastTextSizeHP = mutableFloatStateOf(0f)

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

        sliderPositionHP.value = sp.getInt("lastWightHP", 0).toFloat()
        xPositionHP.value = sp.getInt("lastXHP", 0).toFloat()
        yPositionHP.value = sp.getInt("lastYHP", 0).toFloat()
        lastTextSizeHP.value = sp.getInt("lastTextSizeHP", 10).toFloat()


        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {
                        if (isLandscape(this@LocalSettingsActivity)){
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(30.dp)
                            ) {
                                Text(
                                    text = "旋转屏幕进入竖屏",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(30.dp).verticalScroll(rememberScrollState()),
                                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Bottom,
                                ) {
                                    Text(
                                        text = "灵动岛长度\n${(sliderPositionHP.value)}",
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Slider(
                                        value = sliderPositionHP.value/width,
                                        onValueChange = {
                                            Log.d("LocalSettingsActivity", "onCreate: ${it}")
                                            sliderPositionHP.value = it*width
                                            sp.edit().putInt("lastWightHP", sliderPositionHP.floatValue.toInt()).apply()
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
                                        text = "水平位置\n${(xPositionHP.value)}",
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Slider(
                                        value = xPositionHP.value/width,
                                        valueRange = 0f..0.5f,

                                        onValueChange = {
                                            Log.d("LocalSettingsActivity", "onCreate: ${it}")
                                            xPositionHP.value = it*width
                                            sp.edit().putInt("lastXHP", xPositionHP.floatValue.toInt()).apply()
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
                                        text = "垂直位置\n${(yPositionHP.value)}",
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Slider(
                                        value = yPositionHP.value/height,
                                        valueRange = 0f..0.5f,
                                        onValueChange = {
                                            Log.d("LocalSettingsActivity", "onCreate: ${it}")
                                            yPositionHP.value = it*height
                                            sp.edit().putInt("lastYHP", yPositionHP.floatValue.toInt()).apply()
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
                                        text = "文字大小\n${(lastTextSizeHP.value)}",
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Slider(
                                        value = lastTextSizeHP.value/20,
                                        onValueChange = {
                                            Log.d("LocalSettingsActivity", "onCreate: ${it}")
                                            lastTextSizeHP.value = it*20
                                            sp.edit().putInt("lastTextSizeHP", lastTextSizeHP.floatValue.toInt()).apply()
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
                        }else{
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(30.dp)
                            ) {
                                Text(
                                    text = "旋转屏幕进入竖屏",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(30.dp).verticalScroll(rememberScrollState()),
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

                        // 设置 选择条




                    }

                }
            }
        }
    }
}

