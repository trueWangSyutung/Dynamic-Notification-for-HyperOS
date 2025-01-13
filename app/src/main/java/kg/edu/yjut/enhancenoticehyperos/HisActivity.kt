package kg.edu.yjut.enhancenoticehyperos

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.twotone.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kg.edu.yjut.enhancenoticehyperos.base.CodeDatabase
import kg.edu.yjut.enhancenoticehyperos.entity.NoticeHistory
import kg.edu.yjut.enhancenoticehyperos.ui.theme.DynamicNotificationTheme
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeBackgroundColor
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeTextColor
import kg.edu.yjut.enhancenoticehyperos.utils.timeStempToTime

class HisActivity : ComponentActivity() {
    val page = mutableStateOf(0)
    val pagesize = 15
    val histories = mutableStateListOf<NoticeHistory>()

    fun initData(init:Boolean) {
        if (init) {
            histories.clear()
            page.value = 1
        }else{
            page.value++
        }
        val list = CodeDatabase.getDatabase(this@HisActivity).noticeDao().getNoticeList(
            pagesize,
            pagesize * (page.value - 1)
        )
        if (list.isEmpty() && page.value > 1) {
            page.value--
            Toast.makeText(this@HisActivity, "没有更多数据了", Toast.LENGTH_SHORT).show()
            return
        }

        histories.addAll(list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(true)
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
                            "历史通知",
                            color = getDarkModeTextColor(this@HisActivity),
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
                        histories.forEachIndexed { index, noticeHistory ->
                            val app_info = remember {
                                packageManager.getApplicationInfo(
                                    noticeHistory.packageName,
                                    0
                                )
                            }
                            val label = remember {
                                packageManager.getApplicationLabel(
                                    app_info
                                ).toString()
                            }
                            val icon = remember {
                                packageManager.getApplicationIcon(
                                    app_info
                                )
                            }
                            val isShowdetail = remember {
                                mutableStateOf(false)
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                                    .background(
                                        getDarkModeBackgroundColor(
                                            context = this@HisActivity,
                                            level = 1
                                        ),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(15.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Row {
                                        Icon(
                                            rememberDrawablePainter(drawable = icon),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 10.dp)
                                        )
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                label,
                                                color = getDarkModeTextColor(this@HisActivity),
                                                fontSize = 20.sp,
                                                maxLines = 1,
                                                textAlign = TextAlign.Start,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                timeStempToTime(noticeHistory.sendTime, 9),
                                                color = getDarkModeTextColor(this@HisActivity),
                                                fontSize = 10.sp,
                                                maxLines = 1,
                                                textAlign = TextAlign.Start,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    IconButton(onClick = {
                                        isShowdetail.value = !isShowdetail.value
                                    }
                                    ) {
                                        Icon(
                                            if (isShowdetail.value) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(30.dp)
                                        )
                                    }
                                }
                                AnimatedVisibility(visible = isShowdetail.value) {
                                    Column {
                                        Text(
                                            noticeHistory.title+"\n"+noticeHistory.content,
                                            color = getDarkModeTextColor(this@HisActivity),
                                            fontSize = 15.sp,
                                            maxLines = 3,
                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Normal,

                                            )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            noticeHistory.chennel,
                                            color = getDarkModeTextColor(this@HisActivity),
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Normal,

                                            )
                                        val chanelisopen = remember {
                                            val sp = getSharedPreferences("application_config", Context.MODE_PRIVATE)
                                            mutableStateOf(
                                                sp.getBoolean(
                                                    "${noticeHistory.packageName}_${noticeHistory.chennel}",
                                                    true
                                                )
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            if (chanelisopen.value) {
                                                TextButton(onClick = {
                                                    var sp = getSharedPreferences(
                                                        "application_config",
                                                        Context.MODE_PRIVATE
                                                    )
                                                    sp.edit().putBoolean(
                                                        "${noticeHistory.packageName}_${noticeHistory.chennel}",
                                                        false
                                                    ).apply()
                                                }) {
                                                    Text(
                                                        "禁用该通道",
                                                        color = getDarkModeTextColor(this@HisActivity),
                                                        fontSize = 12.sp,
                                                        maxLines = 1,
                                                        textAlign = TextAlign.Start,
                                                        fontWeight = FontWeight.Normal,
                                                    )
                                                }
                                            } else {
                                                TextButton(onClick = {
                                                    var sp = getSharedPreferences(
                                                        "application_config",
                                                        Context.MODE_PRIVATE
                                                    )
                                                    sp.edit().putBoolean(
                                                        "${noticeHistory.packageName}_${noticeHistory.chennel}",
                                                        true
                                                    ).apply()
                                                }) {
                                                    Text(
                                                        "通道已禁用",
                                                        color = getDarkModeTextColor(this@HisActivity),
                                                        fontSize = 12.sp,
                                                        maxLines = 1,
                                                        textAlign = TextAlign.Start,
                                                        fontWeight = FontWeight.Normal,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }



                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ){
                            TextButton(onClick = {
                                initData(false)
                            }) {
                                Text(
                                    "加载更多",
                                    color = getDarkModeTextColor(this@HisActivity),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                )
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
fun GreetingPreview() {
    DynamicNotificationTheme {
        Greeting("Android")
    }
}