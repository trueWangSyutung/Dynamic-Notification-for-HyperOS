package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import kg.edu.yjut.litenote.MainActivity
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme

class PrivacyAgreementActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dm = resources.displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels
        // 获取传递过来的数据
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
                            Web("file:///android_asset/ysxy.html")
                            AnimatedVisibility(visible = !isShow) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Button(onClick = {
                                        var sp = getSharedPreferences("config", MODE_PRIVATE)
                                        var editor = sp.edit()
                                        editor.putBoolean("isAgreePA", true)
                                        editor.apply()
                                        startActivity(Intent(this@PrivacyAgreementActivity, MainActivity::class.java))
                                        finish()
                                    },
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .width(100.dp),
                                    ) {
                                        Text(text = "同意")
                                    }
                                    Button(onClick = {
                                        startActivity(Intent(this@PrivacyAgreementActivity, MainActivity::class.java))
                                        finish()
                                    },
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .width(100.dp),
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    LiteNoteTheme {
        Greeting("Android")
    }
}