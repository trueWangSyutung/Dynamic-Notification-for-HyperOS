/*
 * Copyright (C) 2024 The LiteNote Project
 * @author OpenAcademic
 * @version 1.0
 * 
 */
package  kg.edu.yjut.enhancenoticehyperos.receive

import android.annotation.SuppressLint
import android.app.Service.CLIPBOARD_SERVICE
import android.content.BroadcastReceiver
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import kg.edu.yjut.enhancenoticehyperos.miui.MiuiStringToast
import kg.edu.yjut.enhancenoticehyperos.miui.devicesSDK.isMoreHyperOSVersion
import kg.edu.yjut.enhancenoticehyperos.miui.devicesSDK.isUnHyperOSNotices
import kg.edu.yjut.enhancenoticehyperos.service.CodeConfig
import kg.edu.yjut.enhancenoticehyperos.service.FocusService
import kg.edu.yjut.enhancenoticehyperos.utils.Configs
import kg.edu.yjut.enhancenoticehyperos.utils.PickupCodeUtils

import java.text.SimpleDateFormat
import kotlin.concurrent.thread


class MessageReciever : BroadcastReceiver() {
    private final var SMS_RECEIVER_ACTION = "android.provider.Telephony.SMS_RECEIVED"
    private final var TAG = "com.example.litenote.reciever.MessageReciever"

    override fun onReceive(context: Context, intent: Intent) {
        var sBuilder = StringBuilder();
        val format = intent.getStringExtra("format");
        var from = ""
        if(SMS_RECEIVER_ACTION.equals(intent.getAction()))
        {
            val bundle = intent.getExtras();
            if(null != bundle)
            {
                val pdus = bundle.get("pdus") as Array<Any>;
                val messages = arrayOfNulls<SmsMessage>(pdus.size);
                for(i in messages.indices)
                {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray,format);
                }
                for(msg in messages)
                {
                    if (msg != null) {
                        //sBuilder.append("来自：").append(msg.getDisplayOriginatingAddress()).append("\n")
                        //    .append("短信内容：")
                        sBuilder.append(msg.getDisplayMessageBody()).append("\n")
                        from = msg.getDisplayOriginatingAddress()   // 获取短信发送方

                    };
                }
            }
        }

        if (sBuilder.toString().isEmpty()) {
            return;
        }
        else{
            if (Configs.getSettingItems(context,"yanzhengma",false)){
                if (PickupCodeUtils.isYanzhengma(sBuilder.toString())){
                    var str = sBuilder.toString().replace(Regex("[0-9]{11}"),"")
                    var code = PickupCodeUtils.getYanzhengma(str)
                    var from = from

                    if (code.isNotEmpty()){
                        try {
                            showQujianma(
                                context,
                                CodeConfig(
                                    code,
                                    "",
                                    1,
                                    from
                                )
                            )
                            return
                        }catch (e:Exception){
                            return
                        }
                        return
                    }else{
                        return
                    }
                }
            }
        }

        Log.d("MessageReciever", sBuilder.toString());
    }
    @SuppressLint("SimpleDateFormat")
    private fun timeStempToTime(timest: Long,mode:Int): String {
        //val ptimest=1000L*timest
        // 转换为 2024-12-12 12:12
        if (mode == 1){
            val sdf =  SimpleDateFormat("yyyy-MM-dd");
            val dateStr = sdf.format(java.util.Date(timest).time);
            return dateStr;
        }else if (mode == 2){
            val sdf =  SimpleDateFormat("yyyy-MM");
            val dateStr = sdf.format(java.util.Date(timest).time);
            return dateStr;
        }else if (mode == 3){
            val sdf =  SimpleDateFormat("yyyy");
            val dateStr = sdf.format(java.util.Date(timest).time);
            return dateStr;
        }else{
            val sdf =  SimpleDateFormat("yyyy-MM-dd HH:mm");
            val dateStr = sdf.format(java.util.Date(timest).time);
            return dateStr;
        }

    }
    @SuppressLint("WrongConstant")
    fun showQujianma(
        context: Context,
        config: CodeConfig
    ){
        if (Settings.canDrawOverlays(context)) {
            val intent = Intent(context, FocusService::class.java)
            intent.putExtra("text", config.text)
            intent.putExtra("express", config.express)
            intent.putExtra("type", config.type)
            intent.putExtra("from", config.from)
            intent.putExtra("logMode", false)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 检查 auto_fill
                if (Configs.getSettingItems(context,"auto_fill",false)){
                    // 将验证码 自动填充 到 剪切板
                    // 写入剪切板
                    val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = android.content.ClipData.newPlainText("text", config.text)
                    clipboard.setPrimaryClip(clip)
                }
                ContextCompat.startForegroundService(context, intent)
            } else {
                context.startService(intent)
            }





        }

    }



}