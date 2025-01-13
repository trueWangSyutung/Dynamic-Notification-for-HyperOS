/*
 * Copyright (C) 2024 The LiteNote Project
 * @author OpenAcademic
 * @version 1.0
 * 
 */
package  kg.edu.yjut.enhancenoticehyperos.service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import kg.edu.yjut.enhancenoticehyperos.service.FocusLayout

class FocusWindowManager constructor(context: Context) {

    var isShowing = false
    private val TAG = FocusWindowManager::class.java.simpleName
    private var mContext: Context = context
    private var mFloatLayout = FocusLayout(mContext)
    private var mLayoutParams: WindowManager.LayoutParams? = null
    private var mWindowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var mToastConfig: CodeConfig? = null
    @SuppressLint("WrongConstant")
    fun createWindow(
        config: CodeConfig,
        function: () -> Unit,
        copy : () -> Unit,
        save : () -> Unit,
        landscape: Boolean
    ) {
        mToastConfig = config
        // 对象配置操作使用apply，额外的处理使用also

        // 获取屏幕的宽高 dp
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dpi = mContext.resources.displayMetrics.density
        val height = wm.defaultDisplay.height
        val width = wm.defaultDisplay.width
        var sp = mContext.getSharedPreferences("curr_data", Context.MODE_PRIVATE)

        // 设置悬浮窗的宽高
        Log.d(TAG, "onInit: ${width} ${height}")

        if (!landscape){
            mLayoutParams = WindowManager.LayoutParams().apply {
                type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY or WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
                // 设置图片格式，效果为背景透明
                format = PixelFormat.RGBA_8888
                // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
                flags =       WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

                // 调整悬浮窗显示的停靠位置为右侧置顶
                gravity = Gravity.TOP or Gravity.START
                // 以屏幕左上角为原点，设置x、y初始值
                x = 10
                y = 20


            }
            mLayoutParams!!.width = (width - 20).toInt()
            mLayoutParams!!.height = ((height)/5).toInt()
            // 设置悬浮窗的宽高
            //mFloatLayout.setHorizontallyScrolling()

        }else{
            mLayoutParams = WindowManager.LayoutParams().apply {
                type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                // 设置图片格式，效果为背景透明
                format = PixelFormat.RGBA_8888
                // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
                flags =
                       WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                       WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                       WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                // 调整悬浮窗显示的停靠位置为右侧置顶
                gravity = Gravity.TOP or Gravity.START
                // 以屏幕左上角为原点，设置x、y初始值
                x = (width/4).toInt()
                y = 20


            }
            mLayoutParams!!.width = (width/2).toInt()
            mLayoutParams!!.height = (width/7).toInt()
            // 设置悬浮窗的宽高
            mFloatLayout.setTextSize(sp.getInt("lastTextSize", 10).toFloat())
            //mFloatLayout.setHorizontallyScrolling()

        }
        mFloatLayout.setText(config.text)
        if (config.type == 0){
            mFloatLayout.setShowText("您收到快递取件码")
            mFloatLayout.setExpressText(config.express)
            mFloatLayout.setSaveUnVisible()


        }else{
            mFloatLayout.setShowText("您收到短信验证码")
            mFloatLayout.setSaveUnVisible()
            mFloatLayout.setExpressText(config.from)

        }


        // 设置位置
        // 屏幕水平正中心
        mFloatLayout!!.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        mFloatLayout.setOnClickListener(function)
        mFloatLayout.setOnCopyClickListener(copy)
        mFloatLayout.setOnSaveClickListener(save)

        mWindowManager.addView(mFloatLayout, mLayoutParams)

        isShowing = true
    }

    // 设置拖动事件




    fun removeWindow() {
        mWindowManager.removeView(mFloatLayout)
        // 保存悬浮窗的位置


        isShowing = false
    }

}