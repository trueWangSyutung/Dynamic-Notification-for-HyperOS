package kg.edu.yjut.enhancenoticehyperos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kg.edu.yjut.enhancenoticehyperos.ui.theme.DynamicNotificationTheme
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.annotation.SuppressLint

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeBackgroundColor
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeTextColor

@Composable
fun AboutPage(
    modifier: Modifier = Modifier,
    context: Context
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 标题

        Image(painter = painterResource(id = R.mipmap.ic_launcher_round),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 40.dp, bottom = 40.dp),
            contentDescription = "logo")
        Text(
            text = "开放学术共同体",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = getDarkModeTextColor(context)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // 主要宣言
        Text(
            text = "一个自由平等独立开放的学术共同体，秉承\"科技无国界\"、\"学术无国界\"的无国界主义以及光荣而伟大的共产主义，" +
                    "及高度开放性、包容性和全球化的学术价值观，致力于推动全球科学与学术的发展，不受地域、文化和政治界限的束缚。",
            fontSize = 16.sp,
            color = getDarkModeTextColor(context),
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 作者信息
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    getDarkModeBackgroundColor(context, 1),
                    RoundedCornerShape(10.dp)
                )
                .padding(15.dp)
        ) {
            Text(
                text = "作者",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = getDarkModeTextColor(context)
            )

            Spacer(modifier = Modifier.height(10.dp))

            listOf(
                "在读研究生",
                "共产主义者",
                "理想主义者",
                "独立开发者",
                "Linux爱好者"
            ).forEach { item ->
                Text(
                    text = "• $item",
                    color = getDarkModeTextColor(context),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))



        // 位置信息
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    getDarkModeBackgroundColor(context, 1),
                    RoundedCornerShape(10.dp)
                )
                .padding(15.dp)
        ) {
            Text(
                text = "我们的位置",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = getDarkModeTextColor(context)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "我们位于中华人民共和国🇨🇳",
                color = getDarkModeTextColor(context),
                textAlign = TextAlign.Justify
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:wangxudong@oac.ac.cn")
                        }
                        context.startActivity(intent)
                    }.padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "联系邮箱",
                    color = getDarkModeTextColor(context)
                )
                Row(

                ) {
                    Text(
                        text = "wangxudong@oac.ac.cn",
                        color = getDarkModeTextColor(context)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "跳转到邮箱",
                        tint = getDarkModeTextColor(context)
                    )
                }
            }

        }


    }
}
class AboutActivity : ComponentActivity() {
    private fun getAppVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            "未知版本"
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getAppVersionCode(): Long {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }
    }

    private fun getSDKInfo(): String {
        val str = when (applicationInfo.minSdkVersion) {
            Build.VERSION_CODES.O -> "Android 8 Oreo"
            Build.VERSION_CODES.P -> "Android 9 Pie"
            Build.VERSION_CODES.Q -> "Android 10 Q"
            Build.VERSION_CODES.R -> "Android 11 R"
            Build.VERSION_CODES.S -> "Android 12 S"
            Build.VERSION_CODES.S_V2 -> "Android 13 S"
            Build.VERSION_CODES.TIRAMISU -> "Android 13 T"
            Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> "Android 14 U"
            else -> "未知版本"
        }
        return  "最小支持版本: ${str}"
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DynamicNotificationTheme {
                Scaffold(

                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding).verticalScroll(
                                rememberScrollState()
                            )
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(20.dp))

                        // 显示居左文字
                        Text(
                            text = "应用信息",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
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
                                InfoItem("软件名称", resources.getString(R.string.app_name))
                                InfoItem("软件版本", "v${getAppVersionName()} (${getAppVersionCode()})")
                                InfoItem("软件作者", "汝阳县斯普锐思软件技术工作室")
                                InfoItem("SDK信息", getSDKInfo())

                                //  InfoItem("设备Token", PushAgent.getInstance(this@AppInfoActivity).registrationId)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 显示居左文字
                        Text(
                            text = "开发者信息",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // 开发者信息
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                InfoItem("开发者", "开放学术共同体" )
                                InfoItem("开发者主页", "https://oac.ac.cn")
                                InfoItem("开发者邮箱", "wangxudong@oac.ac.cn")
                                InfoItem("开发者微信公众号", "OAC开放学术")



                            }
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun InfoItem(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun InfoItem2(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun InfoItem3(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = content,
            fontSize = 14.sp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}