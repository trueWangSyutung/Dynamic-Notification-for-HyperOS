package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.CalendarContract.Colors
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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Close
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import cn.tw.sar.projecter.utils.getDarkModeBackgroundColor
import cn.tw.sar.projecter.utils.getDarkModeTextColor
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.insets.statusBarsHeight
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.bean.Code
import kg.edu.yjut.litenote.bean.LogBeam
import kg.edu.yjut.litenote.helper.RegexMangerHelper
import kg.edu.yjut.litenote.miui.devicesSDK.closeUseHyperOSNotices
import kg.edu.yjut.litenote.miui.devicesSDK.getHyperOSVersion
import kg.edu.yjut.litenote.miui.devicesSDK.isLandscape
import kg.edu.yjut.litenote.miui.devicesSDK.isMoreHyperOSVersion
import kg.edu.yjut.litenote.miui.devicesSDK.isUnHyperOSNotices
import kg.edu.yjut.litenote.miui.devicesSDK.openUseHyperOSNotices
import kg.edu.yjut.litenote.utils.CodeDatebaseUtils
import kg.edu.yjut.litenote.utils.UISetting
import kg.edu.yjut.litenote.utils.Utils
import java.util.Date

@Composable
fun MyIconButton(
    type: String,
    imgID : Int,
    onClick: () -> Unit,
    isChoose : Boolean = false,
    context: Context
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
            Text(text = type, fontSize = 20.sp,
                color = getDarkModeTextColor(context))
        }
    }
}


@Composable
@SuppressLint("UnusedMaterial3Api")
@Preview(showBackground = true)
fun HomeLogItem(
    context: Context = LocalContext.current,
    logBeam: LogBeam = LogBeam(
        1,"fdsfdsff","fdgdfgdfg","fgdfgfdg","fgdfgfdg","2024-01-05"
    ),
    width : Float = 300f
) {
    var unShow = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .width(width.dp)
            .padding(5.dp).background(
                getDarkModeBackgroundColor(context,1),
                MaterialTheme.shapes.medium
            )
            .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
            .padding(10.dp)
    ) {
        // 获取包名对应的应用名称、图标
        var appManager = context.packageManager
        // 判断 app 是否已经安装

        var isAppInstalled = Utils.isAppInstalled(context, logBeam.packageName)
        var appInfo: ApplicationInfo? = null;
        var appName = "未知应用"
        var appIcon = context.resources.getDrawable(R.mipmap.ic_launcher)
        try{
             appInfo = appManager.getApplicationInfo(logBeam.packageName, 0)
             appName = appManager.getApplicationLabel(appInfo).toString()
             appIcon = appManager.getApplicationIcon(appInfo)
        } catch (e: Exception){
             appName = "已删除应用"
             appIcon = context.resources.getDrawable(R.mipmap.ic_launcher)
             appInfo = null
        }


        // 获取屏幕宽度 dp
        var screenWidth = width


        Row(
            modifier = Modifier
                .width(width.dp)
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Image(
                        painter = rememberDrawablePainter(drawable = appIcon),
                        contentDescription = "icon",
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = appName,
                        fontSize = 15.sp,
                        color = getDarkModeTextColor(context)
                    )
                }
                Text(
                    text = logBeam.title,
                    modifier = Modifier.width(screenWidth.dp*2/3),
                    fontSize = 10.sp,
                    maxLines = 1,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    color = getDarkModeTextColor(context)

                )
                Text(
                    text = logBeam.content,
                    fontSize = 10.sp,
                    maxLines = 2,
                    softWrap = true,
                    // 行距
                    lineHeight = 10.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = getDarkModeTextColor(context)
                )

                Text(
                    text = "通知时间：${logBeam.insertTime}",
                    fontSize = 10.sp,
                    color = getDarkModeTextColor(context)
                )


            }
            IconButton(onClick = {
                unShow.value = !unShow.value
            }) {
                Icon(
                    imageVector = if (unShow.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Localized description",
                    tint = getDarkModeTextColor(context),
                    modifier = Modifier
                        .padding(5.dp)
                        .width(50.dp)
                        .height(50.dp)
                )
            }
        }

        AnimatedVisibility(visible = unShow.value) {
            Column {
                Text(
                    text = "应用包名：${logBeam.packageName}",
                    fontSize = 10.sp,
                    color = getDarkModeTextColor(context)
                )
                Text(
                    text = "通知渠道：${logBeam.channelName}",
                    fontSize = 10.sp,
                    color = getDarkModeTextColor(context)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            // 获取 sharedPreference
                            var  sp = context.getSharedPreferences("application_config", Context.MODE_PRIVATE)
                            // 获取 app 的 信息
                            sp.edit().putBoolean(
                                "${logBeam.packageName}_${logBeam.channelName}",
                                false
                            ).apply()
                        },
                        modifier = Modifier.padding(5.dp),

                        ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.wear.compose.material.Icon(
                                imageVector = Icons.Sharp.Close,
                                contentDescription = "Localized description",
                                tint = Color.White
                            )
                            Text(text = "禁用") }
                    }


                }
            }

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
    widthDp : Float = 300f,
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
            modifier = Modifier
                .width(((widthDp * 2) / 3).dp)
                .fillMaxHeight()
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
                .width(((widthDp * 1) / 3).dp)

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


    var labelList = listOf(
        "首页",
        "取件码",
        "设置",
        "关于"
     )
    var iconList = listOf(
        Icons.Filled.Home,
        Icons.Filled.Star,
        Icons.Filled.Settings,
        Icons.Filled.Info
    )


    @Composable
    fun contentView(
        selectIndex:Int,
        contxt:Context,
        widthDp : Float,
        acp : SharedPreferences,
        isLand : Boolean
    ){
        var currModifier : Modifier?
        if (isLand){
            currModifier = Modifier
                .padding(20.dp)
        }else{
            currModifier = Modifier
                .padding(20.dp, 120.dp, 20.dp, 100.dp)
        }
        if (selectIndex == 0){

            Column(
                modifier = currModifier
                    .fillMaxHeight()
                    .verticalScroll(
                        rememberScrollState()
                    )
                ,

                ) {
                Spacer(
                    modifier = Modifier
                        .statusBarsHeight()
                        .fillMaxWidth()
                )
                Text(
                    text = "历史通知",
                    fontSize = 40.sp,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp),
                    color = getDarkModeTextColor(this@MyHomeActivity)
                )
                val sharePreference = getSharedPreferences("config", Context.MODE_PRIVATE)
                var stateShow = remember {
                    mutableStateOf(sharePreference.getBoolean("isUpdatePA", false))
                }
                AnimatedVisibility(visible = !stateShow.value ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
                            .padding(3.dp)
                            .clickable {
                                var intent = Intent(
                                    this@MyHomeActivity,
                                    PrivacyAgreementActivity::class.java
                                )
                                intent.putExtra("type", "update")
                                startActivity(
                                    intent
                                )
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(3.dp)
                        )
                        // 让文字滚动
                        Text(text = "我们近期更新了隐私政策，点击查看", fontSize = 12.sp,
                            color = getDarkModeTextColor(this@MyHomeActivity))
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (currdays.value < 7) {
                                currdays.value += 1
                                loglists.clear()
                                logpage.value = 1
                                loglists.addAll(
                                    CodeDatebaseUtils.getLogsByTime(
                                        db, logpage.value, currdays.value
                                    )
                                )
                            }else{
                                Toast.makeText(
                                    contxt,
                                    "我们只能保存最近7天的数据",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        // 下一天
                        Text(text = "上一天")
                    }

                    var currDate = Date()
                    // 显示当前日期
                    Text(
                        text = Utils.getFormatDate(
                            currDate,
                            currdays.value
                        ),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(3.dp),
                        color = getDarkModeTextColor(this@MyHomeActivity)
                    )


                    Button(
                        onClick = {
                            if (currdays.value > 0){
                                currdays.value -= 1
                                loglists.clear()
                                logpage.value = 1
                                loglists.addAll(
                                    CodeDatebaseUtils.getLogsByTime(
                                        db, logpage.value, currdays.value
                                    )
                                )
                            }else{
                                Toast.makeText(
                                    contxt,
                                    "你往后翻啥翻，还没过呢",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        },
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        // 上一天
                        Text(text = "下一天")
                    }
                }


                Column(
                    modifier = Modifier
                    ,

                    ) {
                    if (isLandscape(this@MyHomeActivity)){
                        // 每两个一行
                        for (index in 0 until loglists.size) {
                            if (index % 2 == 0) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    HomeLogItem(
                                        this@MyHomeActivity,
                                        logBeam = loglists[index],
                                        width = widthDp/2

                                    )
                                    if (index + 1 < loglists.size) {
                                        HomeLogItem(
                                            this@MyHomeActivity,
                                            logBeam = loglists[index + 1],
                                            width = widthDp/2,

                                        )
                                    }
                                }
                            }

                        }
                    }else{
                        for (i in loglists) {
                            HomeLogItem( this@MyHomeActivity,
                                logBeam = i,
                                width = widthDp,

                            )
                        }
                    }


                    // 加载更多
                    AnimatedVisibility(visible = loglists.size != 0) {

                        Button(
                            onClick = {
                                logpage.value += 1
                                var newLogs = CodeDatebaseUtils.getLogsByTime(
                                    db, logpage.value, currdays.value
                                )
                                if (newLogs.size > 0) {
                                    loglists.addAll(newLogs)
                                } else {
                                    logpage.value -= 1
                                    Toast.makeText(
                                        contxt,
                                        "没有更多数据了",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(text = "加载更多",
                                color = getDarkModeTextColor(this@MyHomeActivity))
                        }
                    }
                    AnimatedVisibility(visible = loglists.size == 0) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "没有通知",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(10.dp),
                                color = getDarkModeTextColor(this@MyHomeActivity)
                            )
                        }

                    }
                }

            }
        }
        else if (selectIndex == 1) {

            var isReflash = remember {
                mutableStateOf(false)
            }
            // 主页
            Column(
                modifier = currModifier
                    .verticalScroll(rememberScrollState())

            ) {
                Spacer(modifier = Modifier
                    .statusBarsHeight()
                    .fillMaxWidth())
                AppBar(this@MyHomeActivity)

                // 设置下拉刷新


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
                                }, typeState.value == 0,this@MyHomeActivity
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

                                }, typeState.value == 1,this@MyHomeActivity

                            )


                        }

                        for (i in list) {
                            CodeContainer(i.code, i.yz, i.kd,i.status,
                                i.id,widthDp
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
                                    Text(text = "上一页",
                                        color = getDarkModeTextColor(this@MyHomeActivity))
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
                                    Text(text = "下一页",
                                        color = getDarkModeTextColor(this@MyHomeActivity))
                                }
                            }
                        }
                    }


                }



            }

        }
        else if (selectIndex == 2) {
            // 设置
            Column(
                modifier = currModifier
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
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp),
                    color = getDarkModeTextColor(this@MyHomeActivity)
                )



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
                                    .height(30.dp)
                                    .width(30.dp)
                            )

                            Text(text = "不写入取件码",
                                color = getDarkModeTextColor(this@MyHomeActivity))
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
                        modifier = Modifier.padding(10.dp),
                        color = getDarkModeTextColor(this@MyHomeActivity)
                    )

                    if (isMoreHyperOSVersion(1f)){
                        // 如果是 HyperOS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.height(70.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "icon",
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .height(30.dp)
                                        .width(30.dp),
                                    tint = Color(0xFF91C8E4)

                                )

                                Text(text = "不使用HyperOS灵动额头",
                                    color = getDarkModeTextColor(this@MyHomeActivity))
                            }
                            Switch(checked = isUseSystemNotice.value, onCheckedChange = {
                                isUseSystemNotice.value = it
                                if (it){
                                    // 开启
                                    closeUseHyperOSNotices(this@MyHomeActivity)
                                }else{
                                    // 关闭
                                    openUseHyperOSNotices(this@MyHomeActivity)
                                }
                            })
                        }
                        Text(
                            text = "如果该功能开启，您将不再使用HyperOS灵动额头。而使用软件的灵动岛",
                            modifier = Modifier.padding(10.dp),
                            color = getDarkModeTextColor(this@MyHomeActivity)
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                startActivity(
                                    Intent(
                                        this@MyHomeActivity,
                                        RuleActivity::class.java
                                    )
                                )
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            modifier = Modifier.height(70.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.zz),
                                contentDescription = "icon",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .height(30.dp)
                                    .width(30.dp)
                            )

                            Text(text = "取件码正则规则设置",
                                color = getDarkModeTextColor(this@MyHomeActivity))
                        }

                    }





                }



                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            startActivity(
                                Intent(
                                    this@MyHomeActivity,
                                    MainHomeActivity::class.java
                                )
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier.height(70.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.nt),
                            contentDescription = "icon",
                            modifier = Modifier
                                .padding(10.dp)
                                .height(30.dp)
                                .width(30.dp)
                        )

                        Text(text = "通知设置",
                            color = getDarkModeTextColor(this@MyHomeActivity))
                    }


                }


                if (getHyperOSVersion()==0f || isUnHyperOSNotices(this@MyHomeActivity)){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                startActivity(
                                    Intent(
                                        this@MyHomeActivity,
                                        LocalSettingsActivity::class.java
                                    )
                                )
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            modifier = Modifier.height(70.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.dd),
                                contentDescription = "icon",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .height(30.dp)
                                    .width(30.dp)
                            )

                            Text(text = "灵动岛位置设置",
                                color = getDarkModeTextColor(this@MyHomeActivity))
                        }


                    }
                }
            }
        }
        else if (selectIndex == 3){
            // 关于 APP 页面


            Column(
                modifier = currModifier
                    .verticalScroll(
                        rememberScrollState()
                    ),


                ) {
                Spacer(
                    modifier = Modifier
                        .statusBarsHeight()
                        .fillMaxWidth()
                )


                // 显示 Logo
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Icon(
                        painter =  rememberDrawablePainter(drawable = resources.getDrawable(R.drawable.logo)),
                        contentDescription = "icon",
                        // 设置颜色
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(10.dp)
                            .height(200.dp)
                            .width(200.dp)
                    )
                }

                // 显示版本号
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .background(
                             getDarkModeBackgroundColor(
                                                this@MyHomeActivity,
                                                1
                                            ),MaterialTheme.shapes.medium

                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start

                ){
                    Text(
                        text = "版本号: ${Utils.getVersionName(this@MyHomeActivity)}(${Utils.getVersionCode(this@MyHomeActivity)})",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        color = getDarkModeTextColor(this@MyHomeActivity)
                    )
                }



                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .background(
                             getDarkModeBackgroundColor(
                                                this@MyHomeActivity,
                                                1
                                            ),MaterialTheme.shapes.medium
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start

                ){
                    Text(
                        text = "开发者: ${Utils.getDeveloper(this@MyHomeActivity)}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        color = getDarkModeTextColor(this@MyHomeActivity)
                    )
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .background(
                             getDarkModeBackgroundColor(
                                                this@MyHomeActivity,
                                                1
                                            ),MaterialTheme.shapes.medium
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium
                        )
                        .clickable {
                            var intent = Intent(
                                this@MyHomeActivity,
                                PrivacyAgreementActivity::class.java
                            )
                            intent.putExtra("type", "look")

                            startActivity(
                                intent
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "隐私协议",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        color = getDarkModeTextColor(this@MyHomeActivity)
                    )


                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .background(
                             getDarkModeBackgroundColor(
                                                this@MyHomeActivity,
                                                1
                                            ),MaterialTheme.shapes.medium
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium
                        )
                        .clickable {
                            var intent = Intent(
                                this@MyHomeActivity,
                                UserAgreementActivity::class.java
                            )
                            intent.putExtra("type", "look")

                            startActivity(
                                intent
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "用户协议",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        color = getDarkModeTextColor(this@MyHomeActivity)
                    )


                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .background(
                             getDarkModeBackgroundColor(
                                                this@MyHomeActivity,
                                                1
                                            ),MaterialTheme.shapes.medium
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium
                        )
                        .clickable {
                            var intent = Intent()
                            intent.action = "android.intent.action.VIEW"
                            intent.data =
                                android.net.Uri.parse("https://github.com/trueWangSyutung/Dynamic-Notification-for-HyperOS")
                            startActivity(intent)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Github开源仓库",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        color = getDarkModeTextColor(this@MyHomeActivity)
                    )


                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .background(
                            getDarkModeBackgroundColor(
                                this@MyHomeActivity,
                                1
                            ),MaterialTheme.shapes.medium
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium
                        )
                        .clickable {
                            startActivity(
                                Intent(
                                    this@MyHomeActivity,
                                    UpdateActivity::class.java
                                )
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "检查更新",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        color = getDarkModeTextColor(this@MyHomeActivity)
                    )

                }


            }



        }
    }


    var isWriteCander = mutableStateOf(false)
    var loglists = mutableStateListOf<LogBeam> ()
    val logpage = mutableStateOf(1)
    var currdays = mutableStateOf(0)
    var isUseSystemNotice = mutableStateOf(false)

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

        isUseSystemNotice.value = isUnHyperOSNotices(this@MyHomeActivity)

        Log.d("MyHomeActivity", "list.size = ${list}")

        var contxt = this

        // 加载sharepreference

        var widthDp = Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density

        loglists.addAll(CodeDatebaseUtils.getLogsByTime(
            db, logpage.value,currdays.value
        ))

        setContent {
            MaterialTheme {
                UISetting(context = this@MyHomeActivity)

                Surface(
                    modifier = Modifier
                        .fillMaxSize()


                ) {
                    var selectIndex by remember {
                        mutableStateOf(0)
                    }

                    Scaffold(

                        bottomBar = {

                            AnimatedVisibility(visible = !isLandscape(this@MyHomeActivity)) {

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth().height(100.dp)
                                            .background(
                                                getDarkModeBackgroundColor(this@MyHomeActivity, 1)
                                            )
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
                                                        tint = if (selectIndex == index) MaterialTheme.colorScheme.primary else getDarkModeTextColor(this@MyHomeActivity)
                                                    )
                                                    Text(text = label, fontSize = 8.sp, color = getDarkModeTextColor(this@MyHomeActivity))
                                                }

                                            }
                                        }
                                    }

                            }
                        },
                        floatingActionButton = {
                            if (selectIndex == 1 || selectIndex == 0){
                                IconButton(
                                    onClick = {
                                        if (selectIndex == 0) {
                                            /// 刷新
                                            loglists.clear()
                                            logpage.value = 1
                                            loglists.addAll(CodeDatebaseUtils.getLogsByTime(
                                                db, logpage.value,currdays.value
                                            ))

                                        }else if (selectIndex == 1) {
                                            // 刷新
                                            list.clear()
                                            page.value = 1
                                            list.addAll(
                                                CodeDatebaseUtils.getAllCodes(
                                                    db, typeState.value, page.value - 1, pageSize
                                                )
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .size(60.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.shapes.extraLarge
                                        )

                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = "add",
                                        tint = Color.White,
                                    )
                                }
                            }

                        }

                        ) {
                        var wDp = Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density

                        if (isLandscape(this@MyHomeActivity)) {
                            // 横屏
                            Row(
                                modifier = Modifier
                                    .background(
                                        getDarkModeBackgroundColor(
                                            this@MyHomeActivity,
                                            0
                                        )
                                    )
                                    .fillMaxSize()
                            ){
                                Column(
                                    modifier = Modifier
                                        .width(50.dp)
                                        .fillMaxHeight()
                                        .background(
                                            getDarkModeBackgroundColor(
                                                this@MyHomeActivity,
                                                1
                                            )
                                        )
                                        .shadow(1.dp)
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    labelList.forEachIndexed { index, label ->
                                        IconButton(
                                            modifier = Modifier
                                                .fillMaxWidth(),
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
                                                    .padding(2.dp)
                                            ) {
                                                androidx.compose.material3.Icon(
                                                    imageVector = iconList[index],
                                                    contentDescription = label,
                                                    tint = if (selectIndex == index) MaterialTheme.colorScheme.primary else getDarkModeTextColor(this@MyHomeActivity)
                                                )
                                                Text(text = label, fontSize = 8.sp, color = getDarkModeTextColor(this@MyHomeActivity))
                                            }

                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .width((wDp - 50).dp)
                                        .fillMaxHeight()
                                ) {
                                    contentView(selectIndex, contxt, (wDp - 100), acp,true)
                                }

                            }
                        }else{
                            Column(
                                modifier = Modifier
                                    .background(
                                        getDarkModeBackgroundColor(
                                            this@MyHomeActivity,
                                            0
                                        )
                                    )
                                    .fillMaxSize()
                            ) {
                                contentView(selectIndex, contxt, wDp, acp,false)

                            }

                        }

                    }
                }
            }
        }
    }
}




@Composable
fun AppBar(
    context: Context
) {
    // 构建一个顶部栏
    Text(
        text = "取件码",
        fontSize = 40.sp,
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp),
        color = getDarkModeTextColor(context)
    )


}


