package kg.edu.yjut.litenote.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight
import kg.edu.yjut.litenote.activity.ui.theme.LiteNoteTheme
import kg.edu.yjut.litenote.utils.UISetting

class RuleActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sb = getSharedPreferences("regex_manager", MODE_PRIVATE)
        var codeRegex = mutableStateOf(sb.getString("qujianRegex", "凭(.*?)在|凭(.*?)至|凭(.*?)到|凭(.*?)取")!!)
        var regexCompany = mutableStateOf( sb.getString("companyRegex", "【(.*?)】")!!)
        var regexYizhan = mutableStateOf(sb.getString("yizhanRegex", "到(.*?)驿站")!!)
        println(
            "codeRegex = ${codeRegex.value} regexCompany = ${regexCompany.value} regexYizhan = ${regexYizhan.value}"
        )
        var contxt = this

        setContent {
            MaterialTheme {
                UISetting(context = this@RuleActivity)

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(

                    ){
                       var bac_code = codeRegex.value
                          var bac_company = regexCompany.value
                            var bac_yizhan = regexYizhan.value


                       Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Top,

                       ) {
                           Spacer(modifier = Modifier
                               .statusBarsHeight()
                               .fillMaxWidth())
                           TopAppBar(
                               title = {
                                   Text(text = "规则设置")
                               },
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(0.dp, 10.dp, 0.dp, 10.dp)
                           )
                           Text(
                               text = "所有的格式 均为 凭(.*?)取, 其中(.*?)为取件码的通配符，不可更改，多个正则用|分割",
                                 modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 10.dp)

                           )
                           TextField(
                               value = codeRegex.value,
                               onValueChange = {
                                   codeRegex.value = it
                                   // 检查是否是合法的正则

                               },
                               label = { Text(text = "取件码正则") },
                               modifier = Modifier
                                   .fillMaxWidth(),

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
                                           codeRegex.value.toRegex()
                                           var sharedPreferencesHelper = contxt.getSharedPreferences("regex_manager", MODE_PRIVATE)
                                           var editor = sharedPreferencesHelper.edit()
                                           editor.putString("qujianRegex", codeRegex.value)
                                           editor.apply()
                                           Toast.makeText(
                                               contxt,
                                               "保存成功",
                                               Toast.LENGTH_SHORT
                                           ).show()

                                           bac_code = codeRegex.value

                                       } catch (e: Exception) {
                                           Toast.makeText(
                                               contxt,
                                               "正则表达式不合法",
                                               Toast.LENGTH_SHORT
                                           ).show()
                                           codeRegex.value = bac_code
                                       }


                                   },
                                   modifier = Modifier.padding(10.dp)
                               ) {
                                   Text(text = "保存")
                               }
                           }

                           TextField(
                               value = regexCompany.value,
                               onValueChange = {

                                   regexCompany.value = it
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
                                           regexCompany.value.toRegex()
                                           var sharedPreferencesHelper = contxt.getSharedPreferences("regex_manager", MODE_PRIVATE)
                                           var editor = sharedPreferencesHelper.edit()
                                           editor.putString("companyRegex", regexCompany.value)
                                           editor.apply()
                                           Toast.makeText(
                                               contxt,
                                               "保存成功",
                                               Toast.LENGTH_SHORT
                                           ).show()
                                           bac_company = regexCompany.value

                                       } catch (e: Exception) {
                                           Toast.makeText(
                                               contxt,
                                               "正则表达式不合法",
                                               Toast.LENGTH_SHORT
                                           ).show()
                                           regexCompany.value = bac_company
                                       }

                                   },
                                   modifier = Modifier.padding(10.dp)
                               ) {
                                   Text(text = "保存")
                               }
                           }

                           TextField(
                               value = regexYizhan.value,
                               onValueChange = {
                                   regexYizhan.value = it
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
                                           regexYizhan.value.toRegex()
                                           var sharedPreferencesHelper = contxt.getSharedPreferences("regex_manager", MODE_PRIVATE)
                                           var editor = sharedPreferencesHelper.edit()
                                           editor.putString("yizhanRegex", regexYizhan.value)
                                           editor.apply()
                                           Toast.makeText(
                                               contxt,
                                               "保存成功",
                                               Toast.LENGTH_SHORT
                                           ).show()
                                           bac_yizhan = regexYizhan.value
                                       } catch (e: Exception) {
                                           Toast.makeText(
                                               contxt,
                                               "正则表达式不合法",
                                               Toast.LENGTH_SHORT
                                           ).show()
                                           regexYizhan.value = bac_yizhan
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

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    LiteNoteTheme {
        Greeting2("Android")
    }
}