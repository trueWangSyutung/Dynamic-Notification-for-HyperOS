package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.insets.statusBarsHeight
import com.google.gson.Gson
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme
import kg.edu.yjut.litenote.bean.ActionInfo
import kg.edu.yjut.litenote.bean.AppInfo
import kg.edu.yjut.litenote.bean.ChannelInfo
import kg.edu.yjut.litenote.bean.ShowActionInfo
import kg.edu.yjut.litenote.utils.MyStoreTools
import kg.edu.yjut.litenote.utils.UISetting
import kg.edu.yjut.litenote.utils.isSystemApplication
import kg.edu.yjut.litenote.utils.supportList
import kg.edu.yjut.litenote.utils.supposedPackageName

@Composable
@Preview
fun ProgressIndicator( bfb : Float = 0.5f) {
    Row(
        modifier = Modifier
            .width(100.dp)
            .border(0.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .width(100.dp * bfb)
                .border(0.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

        }

    }
}

@Composable
@Preview
fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 显示进度条

        Image(
           painter = painterResource(id = R.drawable.loading) ,
            contentDescription = "loading...",

            modifier = Modifier
                .padding(10.dp)

        )
        Text(text = "加载中" , modifier = Modifier.padding(10.dp),fontSize = 30.sp, fontWeight = FontWeight.Bold)
    }
}

class MainHomeActivity : ComponentActivity() {



    @SuppressLint("QueryPermissionsNeeded")
    private fun getPkgList(context:Context): List<AppInfo> {
        val packages: MutableList<AppInfo> = ArrayList()
        try {
            val packageInfos: List<PackageInfo> = context.packageManager.getInstalledPackages(
                  PackageManager.MATCH_UNINSTALLED_PACKAGES
            )
            for (info in packageInfos) {
                val pkg = info.packageName
                if (pkg ==     "kg.edu.yjut.enhancenoticehyperos") {

                    continue
                }
                if (pkg ==     "com.android.mms") {
                    packages.add(
                        AppInfo(
                            info.applicationInfo.loadLabel(context.packageManager).toString(),
                            pkg,
                            info.applicationInfo.loadIcon(context.packageManager)

                        )
                    )
                    continue
                }
                // 过滤掉系统应用
                if (isSystemApplication(context, pkg)) {
                    continue
                }
                packages.add(
                    AppInfo(
                    info.applicationInfo.loadLabel(context.packageManager).toString(),
                    pkg,
                    info.applicationInfo.loadIcon(context.packageManager)

                    )
                )
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return packages
    }

    var apps = mutableListOf<ShowActionInfo>()
    var isLoaded = mutableStateOf(false)
    @SuppressLint("QueryPermissionsNeeded", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var permissions2 = arrayOf("android.permission.QUERY_ALL_PACKAGES")
        // 检查权限
        var checkSelfPermission2 = checkSelfPermission(permissions2[0])
        if (checkSelfPermission2 == -1){
            requestPermissions(permissions2, 0)
        }

        Thread{
            val packages = getPkgList(this)
            Log.d("appall", packages.toString())
            var curr = mutableListOf<ShowActionInfo>()

            for (pkg in packages) {
                 // 检查是否在 supportList 中
                // 将 supportList 中的 packageName 列出来
                var supportFunctions = supportList.map { it.packageName }
                if (supportFunctions.contains(pkg.packageName)) {
                    if (pkg.packageName ==     "com.android.mms") {
                        curr.add(
                            ShowActionInfo(
                                "${pkg.appName} (支持取件码存储）" ,
                                pkg.icon,
                                pkg.packageName,
                                "短信",
                                "MyHomeActivity",
                                mutableListOf<ChannelInfo>()
                            )
                        )
                        continue
                    }
                        curr.add(ShowActionInfo(
                        pkg.appName,
                        pkg.icon,
                        pkg.packageName,
                        supportList[supportFunctions.indexOf(pkg.packageName)].actionName,
                        supportList[supportFunctions.indexOf(pkg.packageName)].actionRouter,
                        MyStoreTools.getChannalList(this, pkg.packageName)
                    ))
                }else{
                    curr.add(ShowActionInfo(
                        pkg.appName,
                        pkg.icon,
                        pkg.packageName,
                        "未知",
                        "ChannelActivity",
                        mutableListOf<ChannelInfo>()
                    ))

                }
            }
            Log.d("apps", curr.toString())
            runOnUiThread {
                apps = curr
                isLoaded.value = true
            }
        }.start()
        // 读取手机上的所有应用，包括系统应用
        var content = this
        WindowCompat.setDecorFitsSystemWindows(window, false)

        var sp = getSharedPreferences("application_config", Context.MODE_PRIVATE)

        setContent {
            UISetting(context = this@MainHomeActivity)

            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {
                        Column(
                            modifier = Modifier.padding(10.dp)
                                .fillMaxWidth()
                                .verticalScroll(
                                    rememberScrollState()
                                )
                                .padding(10.dp)
                        ) {
                            Spacer(modifier = Modifier
                                .statusBarsHeight()
                                .fillMaxWidth())
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "支持列表",
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            AnimatedVisibility(visible =  !isLoaded.value) {
                                // 加载中
                                LoadingView()
                            }
                            AnimatedVisibility(visible =  isLoaded.value) {
                                Column {
                                    for (index in 0 until apps.size) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            var check = remember {
                                                mutableStateOf(
                                                    sp.getBoolean(
                                                        apps[index].packageName,
                                                        true
                                                    )
                                                )
                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    modifier = Modifier.height(70.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    IconButton(
                                                        onClick = {
                                                            // 点击图标，打开应用
                                                            if (check.value) {
                                                                var intent = Intent(
                                                                    content,
                                                                    Class.forName("kg.edu.yjut.litenote.activity." + apps[index].actionRouter)
                                                                )
                                                                if (apps[index].actionRouter == "ChannelActivity") {
                                                                    val jsonStr =
                                                                        Gson().toJson(apps[index].channels)
                                                                    Log.d("jsonStr", jsonStr)

                                                                    intent.putExtra(
                                                                        "channel",
                                                                        jsonStr
                                                                    )
                                                                    intent.putExtra(
                                                                        "packageName",
                                                                        apps[index].packageName
                                                                    )
                                                                }
                                                                startActivity(intent)

                                                            } else {
                                                                Toast.makeText(
                                                                    content,
                                                                    "请先启用该功能",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        },
                                                        modifier = Modifier
                                                            .height(100.dp)
                                                            .width(100.dp)
                                                    ) {
                                                        Image(
                                                            painter = rememberDrawablePainter(
                                                                drawable = apps[index].icon
                                                            ),
                                                            contentDescription = apps[index].packageName,
                                                            modifier = Modifier
                                                                .padding(10.dp)
                                                                .height(100.dp)
                                                                .width(100.dp)
                                                        )
                                                    }

                                                    Text(text = apps[index].appName)
                                                }
                                                Switch(checked = check.value, onCheckedChange = {
                                                    check.value = it
                                                    var editor = sp.edit()
                                                    editor.putBoolean(apps[index].packageName, it)
                                                    editor.apply()
                                                })
                                            }
                                            // supportFunctions

                                            // configActions
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



@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    LiteNoteTheme {
        Greeting3("Android")
    }
}