package kg.edu.yjut.enhancenoticehyperos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kg.edu.yjut.enhancenoticehyperos.miui.devicesSDK.isMoreHyperOSVersion
import kg.edu.yjut.enhancenoticehyperos.ui.theme.DynamicNotificationTheme
import kg.edu.yjut.enhancenoticehyperos.utils.Configs
import kg.edu.yjut.enhancenoticehyperos.utils.SoftwareMode
import kg.edu.yjut.enhancenoticehyperos.widget.EasyButton
import kg.edu.yjut.enhancenoticehyperos.widget.EasySelectDialog
import kg.edu.yjut.enhancenoticehyperos.widget.EasySwitchButton
import kg.edu.yjut.enhancenoticehyperos.widget.SelectDialogItems

class SettingsActivity : ComponentActivity() {
    val TAG = "SettingsActivity"
    val softwareMode = mutableStateOf(SoftwareMode.CompatibleMode)
    val softwareModes = listOf(
        SelectDialogItems<SoftwareMode>("适配模式", SoftwareMode.CompatibleMode),
        SelectDialogItems<SoftwareMode>("全部应用模式", SoftwareMode.AllApplication)
    )
    override fun onResume() {
        super.onResume()
        softwareMode.value = Configs.getSoftwareMode(this@SettingsActivity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DynamicNotificationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(
                                rememberScrollState()
                            )
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    )  {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "配置中心",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // 信息卡片
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                val currindex = remember {
                                    mutableStateOf(softwareModes.indexOfFirst { it.value == softwareMode.value })
                                }
                                InfoItem2(
                                    "软件模式",
                                    "选择软件运行的模式\n适配模式：仅已经适配的应用会显示灵动通知\n全部应用模式：所有应用都会显示灵动通知"
                                )
                                EasySelectDialog<SoftwareMode>(
                                    context = this@SettingsActivity,
                                    selcted = softwareModes[currindex.value],
                                    typelist = softwareModes,
                                    title = "选择软件模式",
                                    onConfirm = { mode ->
                                        Configs.setSoftwareMode(this@SettingsActivity, mode.value)
                                        softwareMode.value = mode.value
                                        currindex.value = softwareModes.indexOfFirst { it == mode }
                                    },
                                    )
                                AnimatedVisibility(visible = isMoreHyperOSVersion(1f)) {
                                    Column {
                                        Spacer(modifier = Modifier.height(20.dp))

                                        InfoItem2(
                                            "启用HyperOS的灵动额头",
                                            "启用后，灵动通知将会出现在HyperOS的灵动额头上，而不是灵动岛悬浮窗。"
                                        )
                                        val enabled = remember {
                                            mutableStateOf(Configs.checkUseHyperOSNotices(this@SettingsActivity))
                                        }
                                        EasySwitchButton(
                                            text = "启用HyperOS的灵动额头",
                                            onCheckedChange = {
                                                Configs.setUseHyperOSNotices(this@SettingsActivity, it)
                                                enabled.value = it
                                            },
                                            isChecked = enabled.value,

                                            )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                InfoItem2(
                                    "软件通知设置",
                                    "在软件通知设置中，您可以自由决定是否启用灵动通知"
                                )
                                Row {
                                    EasyButton(
                                        text = "软件通知设置",
                                    ){
                                        val intent = Intent(this@SettingsActivity, MainHomeActivity::class.java)
                                        intent.putExtra("from", "settings")
                                        startActivity(intent)
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Column {
                                    InfoItem2(
                                        "接管系统通知",
                                        "启用后，所有通知将会被拦截，并由灵动通知服务代为发送。\n关闭后，灵动岛和系统通知都会显示"
                                    )
                                    val enabled = remember {
                                        mutableStateOf(Configs.checkInterceptSystemNotices(this@SettingsActivity))
                                    }
                                    EasySwitchButton(
                                        text = "启用接管系统通知",
                                        onCheckedChange = {
                                            Configs.setInterceptSystemNotices(this@SettingsActivity, it)
                                            enabled.value = it
                                        },
                                        isChecked = enabled.value,

                                        )
                                }
                            }


                        }

                    }
                }
            }
        }
    }
}
