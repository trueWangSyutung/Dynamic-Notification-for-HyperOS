package kg.edu.yjut.enhancenoticehyperos

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.edu.yjut.enhancenoticehyperos.bean.Log
import kg.edu.yjut.enhancenoticehyperos.service.MessageService
import kg.edu.yjut.enhancenoticehyperos.ui.theme.DynamicNotificationTheme
import kg.edu.yjut.enhancenoticehyperos.utils.Configs
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeBackgroundColor
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeTextColor
import kg.edu.yjut.enhancenoticehyperos.widget.EasyTextButton


enum class SettingType {
    Boolean,
    String,
    NONE
}

data class SettingItem(
    val id : String = "",
    val name : String = "",
    val type : SettingType = SettingType.Boolean,
    val defaultValue: Boolean = false,
)


data class MoreFunctionItem(
    val id : String = "",
    val name : String = "",
    val description: String = "",
    val icon : Int,
    val inquiredPermissions : List<String> = listOf(),
    val inquiredPermissionsDescription : List<String> = listOf(),
    val subSettings : List<SettingItem> = listOf()
)

class MoreFunctionActivity : ComponentActivity() {
    val functions = listOf(
        MoreFunctionItem(
            id = "yanzhengma",
            name = "验证码提取",
            description = "在一些非国行的手机系统中，验证码往往是不能够自动读取的，本功能可以帮你提取验证码，从而可以自动填写验证码。",
            icon = R.mipmap.yzm,
            inquiredPermissions = listOf(
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS,
                ),
            inquiredPermissionsDescription = listOf(
                "接收短信",
                "读取短信"
            ),
            subSettings = listOf(
                SettingItem(
                    id = "auto_fill",
                    name = "自动复制验证码",
                    type = SettingType.Boolean,
                    defaultValue = true
                )
            )
        ),

    )

    private fun isServiceRunning2(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as  (ActivityManager);
        val serviceClass = MessageService::class.java;
        for ( service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true;
            }
        }
        return false;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DynamicNotificationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )

                    ) {
                        Text(
                            "更多功能",
                            color = getDarkModeTextColor(this@MoreFunctionActivity),
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
                        functions.forEach {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                                    .background(
                                        getDarkModeBackgroundColor(
                                            context = this@MoreFunctionActivity,
                                            level = 1
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(15.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ){
                                InfoItem3(
                                    title = it.name,
                                    content = it.description,
                                )
                                Image(painter = painterResource(id = it.icon),
                                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
                                    contentDescription =  null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(20.dp)))
                                val isEnabled = remember {
                                    mutableStateOf(
                                        Configs.getSettingItems(
                                            this@MoreFunctionActivity,
                                            it.id,
                                            false
                                        )
                                    )
                                }
                                EasyTextButton(
                                    text = "开启",
                                    text2 = if (isEnabled.value) "关闭" else "开启",
                                    enabled = true,
                                ) {

                                    if (it.inquiredPermissions.isNotEmpty()) {
                                        requestPermissions(it.inquiredPermissions.toTypedArray(), 101)
                                    }
                                    it.inquiredPermissions.forEach { permission ->
                                        if(checkSelfPermission(
                                                permission ) == PackageManager.PERMISSION_GRANTED) {
                                            // 权限已授权
                                        } else {
                                            requestPermissions(arrayOf(
                                                permission
                                            ),101)
                                            while (checkSelfPermission(
                                                permission ) != PackageManager.PERMISSION_GRANTED) {
                                                // 等待
                                                println("等待权限授权...")
                                                Thread.sleep(100)
                                            }
                                            // 权限已授权
                                        }
                                    }
                                    isEnabled.value = !isEnabled.value
                                    Configs.setSettingItems(this@MoreFunctionActivity, it.id, isEnabled.value)
                                    if (isServiceRunning2()){
                                        if (!isEnabled.value){
                                            stopService(Intent(this@MoreFunctionActivity, MessageService::class.java))
                                        }
                                    } else {
                                        if (isEnabled.value){
                                            startForegroundService(Intent(this@MoreFunctionActivity, MessageService::class.java))

                                        }
                                    }
                                }
                                it.inquiredPermissions.forEachIndexed { index, s ->
                                    InfoItem2(
                                        title = s,
                                        content = it.inquiredPermissionsDescription[index]
                                    )
                                }
                                AnimatedVisibility(visible = isEnabled.value) {
                                    it.subSettings.forEach {
                                        val isEnabled2 = remember {
                                            mutableStateOf(
                                                Configs.getSettingItems(
                                                    this@MoreFunctionActivity,
                                                    it.id,
                                                    false
                                                )
                                            )
                                        }
                                        EasyTextButton(
                                            text = it.name,
                                            text2 = if (isEnabled2.value) "关闭" else "开启",
                                            enabled = true,
                                        ) {
                                            isEnabled2.value = !isEnabled2.value
                                            Configs.setSettingItems(this@MoreFunctionActivity, it.id, isEnabled2.value)
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
}

