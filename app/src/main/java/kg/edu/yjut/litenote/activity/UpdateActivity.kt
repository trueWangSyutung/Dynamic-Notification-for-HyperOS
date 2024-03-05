package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.insets.statusBarsHeight
import com.google.gson.Gson
import kg.edu.yjut.litenote.R
import kg.edu.yjut.litenote.bean.HttpBeam
import kg.edu.yjut.litenote.bean.InfoData
import kg.edu.yjut.litenote.utils.DownloadUtil
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException


class UpdateActivity : ComponentActivity() {
    var infoData = mutableStateOf<InfoData? >(null)
    var newInfoData = mutableStateOf<InfoData? >(null)
    var haveNewVersion = mutableStateOf(false)
    var showInfo = mutableStateOf(false)
    var percent = mutableStateOf(0.0f)
    var buttonIndex = mutableStateOf(0)
    var buttonText = arrayOf(
        "检查更新",
        "立即更新",
        "下载中",
        "去安装"
    )

    fun requestPermission() {
        // 请求权限\
        var permissions = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.REQUEST_INSTALL_PACKAGES,
            android.Manifest.permission.INSTALL_PACKAGES
        )
        requestPermissions(permissions, 1)

    }


    fun getCurrentInfoData() {
        // 读取本地缓存
        val sharedPreferences = getSharedPreferences("infoData", Context.MODE_PRIVATE)
        val result = sharedPreferences.getString("infoData", "")


        val currentVersionCode = packageManager.getPackageInfo(packageName, 0).versionCode

        val url = "https://ota.yjut.edu.kg/getCurrentVersion?version_code=${currentVersionCode}&apk_type=hyperos"
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val call: Call = okHttpClient.newCall(request)
        Thread {
            try {
                val response: Response = call.execute()
                val result = response.body()!!.string()
                // 解析json Gson
                var gson = Gson()
                var root = gson.fromJson(result, HttpBeam::class.java)
                // 如果code为200，表示请求成功
                if (root.code == 200) {
                    runOnUiThread {
                        infoData.value = root.data
                        // 保存到 SharedPreferences
                        val sharedPreferences = getSharedPreferences("infoData", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("infoData", result)
                        editor.apply()

                    }

                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
    fun checkUpdate(
        context: Context,
    ) {
        // 获取当前版本 code
        val currentVersionCode = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        var url = "https://ota.yjut.edu.kg/checkUpdate?version_code=${currentVersionCode}&apk_type=hyperos"
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val call: Call = okHttpClient.newCall(request)
        Thread {
            try {
                val response: Response = call.execute()
                val result = response.body()!!.string()
                // 解析json Gson
                var gson = Gson()
                var root = gson.fromJson(result, HttpBeam::class.java)
                // 如果code为200，表示请求成功
                if (root.code == 200) {
                    runOnUiThread {
                        if (root.msg == "success") {
                            haveNewVersion.value = true
                            newInfoData.value = root.data
                            buttonIndex.value = 1
                        }else{
                            haveNewVersion.value = false
                            Toast.makeText(context, "当前已是最新版本", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()

    }
    fun getUpdateStatus(){
        var sharedPreferences = getSharedPreferences("infoData", Context.MODE_PRIVATE)
        var result = sharedPreferences.getString("updatePath", "")
        if(result != ""){
            buttonIndex.value = 3
        }
        var updateInfo = sharedPreferences.getString("updateInfo", "")
        if (updateInfo != "") {
            haveNewVersion.value = true
            newInfoData.value = Gson().fromJson(updateInfo, InfoData::class.java)
        } else {
            haveNewVersion.value = false
        }


    }
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        getCurrentInfoData()
        getUpdateStatus()
        setContent {
            MaterialTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {
                        Column(
                            modifier = Modifier
                                .padding(10.dp, 120.dp, 10.dp, 10.dp)
                                .verticalScroll(rememberScrollState())

                        ) {
                            Spacer(
                                modifier = Modifier
                                    .statusBarsHeight()
                                    .fillMaxWidth()
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween,
                            ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .padding(0.dp, 15.dp)
                                            .width(150.dp)
                                            .height(150.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = MaterialTheme.shapes.extraLarge
                                            )
                                    ) {
                                        Icon(
                                            painter =  rememberDrawablePainter(drawable = resources.getDrawable(
                                                R.drawable.logo)),
                                            contentDescription = "icon",
                                            // 设置颜色
                                            tint =  Color.White,
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .height(200.dp)
                                                .width(200.dp)
                                        )
                                    }
                                    // 显示版本信息
                                    AnimatedVisibility(visible = infoData.value != null && newInfoData.value == null) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "${infoData.value?.apk_size}",
                                                fontSize = 20.sp
                                            )
                                            Text(
                                                text = "${infoData.value?.version_code}",
                                                fontSize = 10.sp
                                            )

                                        }

                                    }

                                    AnimatedVisibility(visible = newInfoData.value != null) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "发现了新版本!",
                                                fontSize = 20.sp,
                                                color = Color(0xFF6E85B7)
                                            )
                                            Text(
                                                text = "${newInfoData.value?.apk_size}",
                                                fontSize = 20.sp
                                            )
                                            Text(
                                                text = "${newInfoData.value?.version_code}",
                                                fontSize = 10.sp
                                            )

                                        }

                                    }

                                    AnimatedVisibility(visible =  infoData.value != null && !showInfo.value &&!haveNewVersion.value ) {
                                        Text(
                                            text = "查看当前版本更新信息",
                                            fontSize = 15.sp,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(0.dp, 15.dp)
                                                .clickable {
                                                    showInfo.value = true
                                                },
                                            color = Color.Blue
                                        )
                                    }

                                    AnimatedVisibility(visible = haveNewVersion.value) {
                                        Column(
                                            horizontalAlignment = Alignment.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp)
                                                .clickable {
                                                }
                                                .background(
                                                    color = Color.LightGray,
                                                    shape = MaterialTheme.shapes.medium
                                                )
                                                .padding(20.dp)
                                        ) {
                                            var newStr = newInfoData.value?.version_info?.replace("\\n", "\n")
                                            Text(
                                                text = "${newStr}",
                                                fontSize = 20.sp
                                            )
                                        }
                                    }

                                    // 显示更新信息
                                    AnimatedVisibility(visible = showInfo.value &&!haveNewVersion.value) {
                                        Column(
                                            horizontalAlignment = Alignment.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp)
                                                .height(200.dp)
                                                .clickable {
                                                    showInfo.value = false
                                                }
                                                .background(
                                                    color = Color.LightGray,
                                                    shape = MaterialTheme.shapes.medium
                                                )
                                                .padding(20.dp)
                                        ) {
                                            var newStr = infoData.value?.version_info?.replace("\\n", "\n")
                                            Text(
                                                text = "${newStr}",
                                                fontSize = 20.sp
                                            )
                                        }
                                    }

                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .background(
                                            color = Color.Transparent,
                                            shape = MaterialTheme.shapes.medium,
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {

                                    AnimatedVisibility(visible = buttonIndex.value==2) {
                                        LinearProgressIndicator(
                                            progress = percent.value,
                                            modifier = Modifier
                                                .width(300.dp)
                                                .padding(10.dp)
                                                .height(10.dp)
                                                .background(
                                                    color = Color.Transparent,
                                                    shape = MaterialTheme.shapes.medium,
                                                ),
                                            color = Color(0xFF6E85B7),
                                            trackColor = Color(0xFFB2C8DF)
                                        )
                                    }
                                    AnimatedVisibility(visible = infoData.value != null || newInfoData.value != null) {

                                        Button(onClick = {
                                            if(buttonIndex.value == 0){
                                                // 显示loading
                                                checkUpdate(this@UpdateActivity)

                                            }else if(buttonIndex.value == 1){
                                                // 下载
                                                buttonIndex.value = 2
                                                // 发起OkHttp请求,下载apk,保存到本地 download目录
                                                val url = newInfoData.value?.apk_url
                                                // 获取 download 目录
                                                // 浏览器打开下载地址
                                                var intent = Intent()
                                                intent.action = "android.intent.action.VIEW"
                                                intent.data = android.net.Uri.parse(url)
                                                startActivity(intent)


                                                //var path = "download/hyperos"
                                                /**
                                                 * DownloadUtil.get().download(url!!, path, object : DownloadUtil.OnDownloadListener {
                                                 *                                                     override fun onDownloadSuccess() {
                                                 *                                                         //成功
                                                 *                                                         runOnUiThread {
                                                 *                                                             buttonIndex.value = 3
                                                 *                                                             // 保存更新信息
                                                 *                                                             val sharedPreferences = getSharedPreferences("infoData", Context.MODE_PRIVATE)
                                                 *                                                             val editor = sharedPreferences.edit()
                                                 *                                                             var gson = Gson()
                                                 *                                                             editor.putString("updateInfo", gson.toJson(newInfoData.value))
                                                 *                                                             editor.putString("updatePath", path)
                                                 *
                                                 *                                                             editor.apply()
                                                 *
                                                 *
                                                 *                                                         }
                                                 *                                                     }
                                                 *
                                                 *                                                     override fun onDownloading(progress: Int) {
                                                 *                                                         //进度
                                                 *                                                         runOnUiThread {
                                                 *                                                             percent.value = progress.toFloat()
                                                 *                                                         }
                                                 *                                                     }
                                                 *
                                                 *                                                     override fun onDownloadFailed() {
                                                 *                                                         //失败
                                                 *                                                         runOnUiThread {
                                                 *                                                             Toast.makeText(this@UpdateActivity, "下载失败", Toast.LENGTH_SHORT).show()
                                                 *                                                             buttonIndex.value = 1
                                                 *                                                         }
                                                 *                                                     }
                                                 *                                                 })
                                                 *
                                                 */

                                            }else if(buttonIndex.value == 3){
                                                // 安装
                                                // 获取下载目录 sdcards/download/hyperos
                                                openFile()

                                            }
                                        }) {
                                            Text(text = buttonText[buttonIndex.value], color = Color.White)
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

    @Throws(IOException::class, IOException::class)
    private fun isExistDir(saveDir: String): String? {
        // 下载位置
        val downloadFile =
            File(Environment.getExternalStorageDirectory(), saveDir)
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile()
        }
        return downloadFile.absolutePath
    }
    private fun openFile() {
        var sharedPreferences = getSharedPreferences("infoData", Context.MODE_PRIVATE)
        var result = sharedPreferences.getString("updatePath", "")
        var updateInfo = sharedPreferences.getString("updateInfo", "")
        var gson = Gson()
        var infoData = gson.fromJson(updateInfo, InfoData::class.java)

        val f: File = File(isExistDir("download/hyperos"), DownloadUtil.getNameFromUrl(infoData.apk_url))

        android.util.Log.d("path", f.path)
        // 打开文件
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        val uri = FileProvider.getUriForFile(
            this,
            applicationContext.packageName + ".provider",
                f
        )

        intent.setDataAndType(uri, "application/vnd.android.package-archive")


        startActivity(intent)
        // 清除缓存
        val editor = sharedPreferences.edit()
        editor.remove("updateInfo")
        editor.remove("updatePath")
        editor.apply()

    }
}

