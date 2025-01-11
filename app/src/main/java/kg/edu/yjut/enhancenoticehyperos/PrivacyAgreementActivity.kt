package kg.edu.yjut.enhancenoticehyperos

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier


class PrivacyAgreementActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
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


                        }
                    }
                }
            }
        }
    }
}
