package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.collection.MutableObjectList
import androidx.collection.mutableObjectListOf
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.insets.statusBarsHeight
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.bean.Code
import kg.edu.yjut.litenote.helper.RegexMangerHelper
import kg.edu.yjut.litenote.utils.CodeDatebaseUtils
import kg.edu.yjut.litenote.utils.UISetting
import kg.edu.yjut.litenote.utils.Utils

@Composable
fun MyIconButton(
    type: String,
    imgID : Int,
    onClick: () -> Unit,
    isChoose : Boolean = false,
){

    IconButton(
        modifier = Modifier
            .width(120.dp)
            .height(50.dp)
            .padding(
                horizontal = 10.dp,
            )
            .border(
                width = if (isChoose) 1.dp else 0.dp,
                color = Color.Black,
                shape = MaterialTheme.shapes.medium
            )
        // 外边距
        ,
        onClick = onClick) {
        // 添加 R.mipmap.ic_launcher 的 图片
        Row(
            // 设置垂直居中
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imgID),
                contentDescription = "Sample",
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
            Text(text = type, fontSize = 20.sp)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CodeContainer(
    code : String = "123456",
    yz : String = "测试驿站",
    kd : String = "测试快递",
    status : Int = 1,
    id:Int=0,
    click : () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(140.dp)
            .background(
                MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .padding(20.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(

        ){
            Text(text = code,
                fontSize = 35.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = yz, fontSize = 15.sp,
                color = MaterialTheme.colorScheme.secondary

            )
            Text(text = kd, fontSize = 15.sp,
                color = MaterialTheme.colorScheme.secondary

            )
        }
        var isStatus = status == 0
        IconButton(
            onClick = click,
            modifier = Modifier
                .fillMaxHeight()
                .width(80.dp)


        ){
            if (isStatus){
                Icon(
                    painter =painterResource(id = R.mipmap.qu)  ,
                contentDescription = "取件",

                tint = MaterialTheme.colorScheme.primary,


                )
            }else{
                Icon(
                    painter =painterResource(id = R.mipmap.del)  ,
                    contentDescription = "取件",

                    tint = MaterialTheme.colorScheme.primary,


                    )
            }

        }



    }

}

@Preview
@Composable
fun showMyDialog(
    title : String = "取件",
    content : String = "是否取件",
    ok: () -> Unit = {}
) {
   AlertDialog(onDismissRequest = {
                                   // 关闭对话框
    },
        title = {
            Text(text = title)
        },
        text = { Text(text = content) },
        confirmButton = {
            Button(
                onClick = ok ) {
                Text(text = "确认")
            }
        },
        dismissButton = {
            Button(onClick = {  }) {
                Text(text = "取消")
            }
        }
    )
}

class MyHomeActivity : ComponentActivity() {
    var typeState : MutableState<Int> = mutableIntStateOf(0)
    // 初始化一个列表状态 Array<Code> 类型
    var list  = mutableStateListOf<Code>()
    var page : MutableState<Int> = mutableIntStateOf(1)
    var pageSize = 15
    var db : SQLiteDatabase? = null


    var labelList = listOf("首页", "设置")
    var iconList = listOf(Icons.Filled.Home, Icons.Filled.Settings)


    var codeRegex = "【(.*?)】到(.*?)凭(.*?)"
    var regexCompany = "【(.*?)】"
    var regexYizhan = "到(.*?)凭"
    var isWriteCander = mutableStateOf(false)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 状态栏透明
        // 读取 Sqlite 数据库
        db = CodeDatebaseUtils.openOrCreateDatabase(this)
        list.addAll(CodeDatebaseUtils.getAllCodes(
            db, typeState.value ,  page.value - 1, pageSize
        ))
        var acp = getSharedPreferences("application_config", Context.MODE_PRIVATE)
        isWriteCander.value = acp.getBoolean("mms_isWriteCander", false)

        Log.d("MyHomeActivity", "list.size = ${list}")

        var contxt = this

        // 加载sharepreference
        var sb = contxt.getSharedPreferences("regex_manager", MODE_PRIVATE)

        codeRegex = sb.getString("qujianRegex", "凭(.*?)在|凭(.*?)至|凭(.*?)到|凭(.*?)取")!!
        regexCompany = sb.getString("companyRegex", "【(.*?)】")!!
        regexYizhan = sb.getString("yizhanRegex", "到(.*?)驿站")!!



        setContent {
            LiteNoteTheme {
                UISetting(context = this@MyHomeActivity)

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)


                ) {
                    var selectIndex by remember {
                        mutableStateOf(0)
                    }
                    var codeRegexHefa by remember {
                        mutableStateOf(true)
                    }

                    Scaffold(
                        topBar = {
                            AnimatedVisibility(
                                visible = selectIndex == 0,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                            ) {

                                AppBar()
                            }
                        },
                        bottomBar = {

                            BottomAppBar(
                                contentColor = MaterialTheme.colorScheme.inverseSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(10.dp)
                                    .height(100.dp)

                                    .background(MaterialTheme.colorScheme.primary),

                                ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    // 底部栏
                                    labelList.forEachIndexed { index, label ->
                                        IconButton(
                                            modifier = Modifier
                                                .weight(50f)
                                                .fillMaxHeight(),
                                            onClick = {
                                                selectIndex = index
                                                print("selectIndex = ${selectIndex}")

                                            }
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                            ) {
                                                androidx.compose.material3.Icon(
                                                    imageVector = iconList[index],
                                                    contentDescription = label,
                                                    tint = if (selectIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(text = label, fontSize = 15.sp)
                                            }

                                        }
                                    }
                                }
                            }
                        },

                        ) {

                        if (selectIndex == 0) {
                            // 主页
                            Column(
                                modifier = Modifier
                                    .padding(10.dp, 120.dp, 10.dp, 10.dp)
                                    .verticalScroll(rememberScrollState())

                            ) {
                                Spacer(modifier = Modifier
                                    .statusBarsHeight()
                                    .fillMaxWidth())
                                AnimatedVisibility(
                                    visible = !isWriteCander.value,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 0.dp, 0.dp, 10.dp)
                                ) {

                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row() {
                                            MyIconButton(
                                                "未取件", R.mipmap.no, {
                                                    typeState.value = 0
                                                    page.value = 1
                                                    list.clear()
                                                    list.addAll(
                                                        CodeDatebaseUtils.getAllCodes(
                                                            db, typeState.value, page.value - 1, pageSize
                                                        )
                                                    )
                                                }, typeState.value == 0
                                            )
                                            MyIconButton(
                                                "已取件", R.mipmap.yes, {
                                                    typeState.value = 1
                                                    page.value = 1
                                                    list.clear()
                                                    list.addAll(
                                                        CodeDatebaseUtils.getAllCodes(
                                                            db, typeState.value, page.value - 1, pageSize
                                                        )
                                                    )

                                                }, typeState.value == 1

                                            )


                                        }

                                        for (i in list) {
                                            CodeContainer(i.code, i.yz, i.kd,i.status,
                                                i.id
                                            ) {
                                                if (i.status == 0){
                                                    CodeDatebaseUtils.updateStatus(db, i.id, 1)
                                                    Toast.makeText(contxt, "取件成功", Toast.LENGTH_SHORT)
                                                        .show()
                                                }else  if (i.status == 1){
                                                    CodeDatebaseUtils.deleteData(db,i.id)
                                                    Toast.makeText(contxt, "删除成功", Toast.LENGTH_SHORT)
                                                        .show()
                                                }

                                                list.clear()
                                                page.value = 1
                                                // 重新加载数据
                                                list.addAll(
                                                    CodeDatebaseUtils.getAllCodes(
                                                        db, typeState.value, page.value - 1, pageSize
                                                    )
                                                )

                                            }

                                        }

                                        AnimatedVisibility(
                                            visible = list.size > 0,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(0.dp, 0.dp, 0.dp, 100.dp)
                                        ) {

                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                            ) {
                                                Button(
                                                    onClick = {
                                                        if (page.value > 1) {
                                                            page.value -= 1
                                                            var dataGet = CodeDatebaseUtils.getAllCodes(
                                                                db,
                                                                typeState.value,
                                                                page.value - 1,
                                                                pageSize
                                                            )
                                                            if (dataGet.size > 0) {
                                                                list.clear()
                                                                list.addAll(dataGet)
                                                            }
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .padding(10.dp)
                                                ) {
                                                    Text(text = "上一页")
                                                }
                                                Button(
                                                    onClick = {
                                                        page.value += 1
                                                        var dataGet = CodeDatebaseUtils.getAllCodes(
                                                            db, typeState.value, page.value - 1, pageSize
                                                        )
                                                        if (dataGet.size > 0) {
                                                            list.clear()
                                                            list.addAll(dataGet)
                                                        } else {
                                                            page.value -= 1
                                                            // 提示没有数据了
                                                            Toast.makeText(
                                                                contxt,
                                                                "没有数据了",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                    },
                                                    modifier = Modifier
                                                        .padding(10.dp)
                                                ) {
                                                    Text(text = "下一页")
                                                }
                                            }
                                        }
                                    }


                                }



                            }

                        } else {
                            // 设置
                            Column(
                                modifier = Modifier
                                    .padding(20.dp, 120.dp, 20.dp, 100.dp)
                                    .verticalScroll(
                                        rememberScrollState()
                                    ),

                            ) {
                                Spacer(modifier = Modifier
                                    .statusBarsHeight()
                                    .fillMaxWidth())
                                Text(
                                    text = "设置",
                                    fontSize = 40.sp,
                                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
                                )

                                var codeEditRegex by remember {
                                    mutableStateOf(codeRegex)
                                }
                                var regexEditCompany by remember {
                                    mutableStateOf(regexCompany)
                                }
                                var regexEditYizhan by remember {
                                    mutableStateOf(regexYizhan)
                                }


                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.height(70.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Image(
                                                painter = painterResource(id = R.drawable.rl),
                                                contentDescription = "icon",
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .height(50.dp)
                                                    .width(50.dp)
                                            )

                                            Text(text = "不写入取件码")
                                        }
                                        Switch(checked = isWriteCander.value, onCheckedChange = {
                                            isWriteCander.value = it
                                            var editor = acp.edit()
                                            editor.putBoolean("mms_isWriteCander", it)
                                            editor.apply()
                                        })
                                    }
                                    Text(
                                        text = "如果该功能开启，您收到的取件码不会被收录到应用数据库。",
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }


                                Text(text = "所有的格式 均为 凭(.*?)取, 其中(.*?)为取件码的通配符，不可更改，多个正则用|分割")
                                TextField(
                                    value = codeEditRegex,
                                    onValueChange = {
                                        var bak = it
                                        codeEditRegex = it
                                        // 检查是否是合法的正则

                                    },
                                    label = { Text(text = "取件码正则") },
                                    modifier = Modifier
                                        .fillMaxWidth(),

                                    isError = !codeRegexHefa,
                                    placeholder = {
                                        Text(text = "格式 凭(.*?)取, 其中(.*?)为取件码的通配符，不可更改，多个正则用|分割")
                                    }
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 10.dp, 10.dp, 0.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            try {
                                                codeEditRegex.toRegex()
                                                var sharedPreferencesHelper = contxt.getSharedPreferences("regex_manager", MODE_PRIVATE)
                                                var editor = sharedPreferencesHelper.edit()
                                                editor.putString("qujianRegex", codeEditRegex)
                                                editor.apply()
                                                Toast.makeText(
                                                    contxt,
                                                    "保存成功",
                                                    Toast.LENGTH_SHORT
                                                ).show()


                                                codeRegex = codeEditRegex
                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    contxt,
                                                    "正则表达式不合法",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                codeEditRegex = codeRegex
                                            }


                                        },
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Text(text = "保存")
                                    }
                                }
                                var bacCompany = regexCompany
                                var bacYizhan = regexYizhan

                                TextField(
                                    value = regexEditCompany,
                                    onValueChange = {

                                        regexEditCompany = it
                                    },
                                    label = { Text(text = "快递公司正则") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 10.dp, 0.dp, 0.dp),
                                    placeholder = {
                                        Text(text = "格式 【(.*?)】")
                                    }
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 10.dp, 10.dp, 0.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            try {
                                                regexEditCompany.toRegex()
                                                var sharedPreferencesHelper = contxt.getSharedPreferences("regex_manager", MODE_PRIVATE)
                                                var editor = sharedPreferencesHelper.edit()
                                                editor.putString("companyRegex", regexEditCompany)
                                                editor.apply()
                                                Toast.makeText(
                                                    contxt,
                                                    "保存成功",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                regexCompany = regexEditCompany

                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    contxt,
                                                    "正则表达式不合法",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                regexEditCompany = regexCompany
                                            }

                                        },
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Text(text = "保存")
                                    }
                                }

                                TextField(
                                    value = regexEditYizhan,
                                    onValueChange = {
                                        regexEditYizhan = it
                                    },
                                    label = { Text(text = "驿站名正则") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 10.dp, 0.dp, 0.dp),
                                    placeholder = {
                                        Text(text = "格式 到(.*?)驿站")
                                    }
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 10.dp, 10.dp, 0.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                           try {
                                                  regexEditYizhan.toRegex()
                                                  var sharedPreferencesHelper = contxt.getSharedPreferences("regex_manager", MODE_PRIVATE)
                                                  var editor = sharedPreferencesHelper.edit()
                                                  editor.putString("yizhanRegex", regexEditYizhan)
                                                  editor.apply()
                                                  Toast.makeText(
                                                    contxt,
                                                    "保存成功",
                                                    Toast.LENGTH_SHORT
                                                  ).show()
                                                  regexYizhan = regexEditYizhan
                                           } catch (e: Exception) {
                                               Toast.makeText(
                                                   contxt,
                                                   "正则表达式不合法",
                                                   Toast.LENGTH_SHORT
                                               ).show()
                                               regexEditYizhan = regexYizhan
                                           }
                                        },
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Text(text = "保存")
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




@Preview(showBackground = true)
@Composable
fun AppBar() {
    // 构建一个顶部栏
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(
                    horizontal = 20.dp,
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Center

        ) {
            Text(text = "取件码",
                // 设置字体大小
                fontSize = 30.sp,
                // 设置字体颜色

            )

            Text(text = "展示你收到的所有取件码",
                // 设置字体大小
                fontSize = 15.sp,
                // 设置字体颜色
            )

        }


    }

}


