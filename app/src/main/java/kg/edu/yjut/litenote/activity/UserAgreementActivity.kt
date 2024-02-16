package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kg.edu.yjut.litenote.MainActivity
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme

class UserAgreementActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 获取 手机宽度
        val dm = resources.displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels
        val intent = intent
        val type = intent.getStringExtra("type")
        var isShow = false
        // 如果不是 look 则不显示下方按钮
        if (type == "look") {
            isShow = true
        }

        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(
                                    rememberScrollState()
                                )
                        ) {
                            Web("file:///android_asset/yhxy.html")

                            AnimatedVisibility(visible = !isShow) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            var sp = getSharedPreferences("config", MODE_PRIVATE)
                                            var editor = sp.edit()
                                            editor.putBoolean("isAgreeUA", true)
                                            editor.apply()
                                            startActivity(
                                                Intent(
                                                    this@UserAgreementActivity,
                                                    ChecksActivity::class.java
                                                )
                                            )
                                            finish()
                                        },
                                        modifier = Modifier.padding(5.dp).width(100.dp),
                                    ) {
                                        Text(text = "同意")
                                    }
                                    Button(
                                        onClick = {
                                            startActivity(
                                                Intent(
                                                    this@UserAgreementActivity,
                                                    ChecksActivity::class.java
                                                )
                                            )
                                            finish()
                                        },
                                        modifier = Modifier.padding(5.dp).width(100.dp),
                                    ) {
                                        Text(text = "拒绝")
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.Center,

                ) {
                    Text(text = "LiteNote (取件码)", fontSize = 30.sp)
                    Text(text = "隐私协议", fontSize = 30.sp)
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 200.dp, 0.dp, 0.dp)
            ) {
                // 加载 assets 文件夹下的 yhxy.html
                Text(text = "更新日期：2024/2/7", fontSize = 10.sp)
                Text(text = "生效日期：2024/2/7", fontSize = 10.sp)




            }
        }
    }
}

@Composable
fun Web(
    url : String = "file:///android_asset/yhxy.html"
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val webView = android.webkit.WebView(context)
            webView.settings.javaScriptEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.domStorageEnabled = true
            webView.settings.loadsImagesAutomatically = true
            webView.settings.mediaPlaybackRequiresUserGesture = false
            webView.webViewClient = android.webkit.WebViewClient()
            webView.loadUrl(url)
            webView
        })
}