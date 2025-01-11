package kg.edu.yjut.enhancenoticehyperos.miui.service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kg.edu.yjut.enhancenoticehyperos.miui.ToastConfig
import kg.edu.yjut.enhancenoticehyperos.miui.devicesSDK.isLandscape


class FloatWindowManager constructor(context: Context) {

    var isShowing = false
    private val TAG = FloatWindowManager::class.java.simpleName
    private var mContext: Context = context
    private var mFloatLayout = FloatLayout(mContext)
    private var mLayoutParams: WindowManager.LayoutParams? = null
    private var mWindowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var mToastConfig: ToastConfig? = null
    private var mlogMode = false
    fun createWindow(
        config: ToastConfig,
        logMode: Boolean = false,
        landscape: Boolean
    ) {
        mToastConfig = config
        // 对象配置操作使用apply，额外的处理使用also
        mlogMode = logMode
        var view = mFloatLayout
        // 获取屏幕的宽高 dp
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dpi = mContext.resources.displayMetrics.density
        val height = wm.defaultDisplay.height
        val width = wm.defaultDisplay.width

        // 设置悬浮窗的宽高
        Log.d(TAG, "onInit: ${width} ${height}")
        if (landscape){
            var islandwidth = width/3
            mLayoutParams = WindowManager.LayoutParams().apply {
                type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                // 设置图片格式，效果为背景透明
                format = PixelFormat.RGBA_8888
                // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                // 调整悬浮窗显示的停靠位置为右侧置顶
                gravity = Gravity.TOP or Gravity.END

                // 以屏幕左上角为原点，设置x、y初始值
                x = width/2 - islandwidth/2
                y = 20


            }
            mLayoutParams!!.width = islandwidth
            mLayoutParams!!.height = 120
            // 设置悬浮窗的宽高
            view.setText(config.text)
            view.setTextSize(15f)
            view.setHorizontallyScrolling()

        }else{
            var islandwidth = width*2/3
            mLayoutParams = WindowManager.LayoutParams().apply {
                type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                // 设置图片格式，效果为背景透明
                format = PixelFormat.RGBA_8888
                // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                // 调整悬浮窗显示的停靠位置为右侧置顶
                gravity = Gravity.TOP or Gravity.END
                // 以屏幕左上角为原点，设置x、y初始值
                x = width/2 - islandwidth/2
                y = 20


            }
            mLayoutParams!!.width = islandwidth
            mLayoutParams!!.height = 100
            // 设置悬浮窗的宽高
            view.setText(config.text)
            view.setTextSize(15f)
            view.setHorizontallyScrolling()
        }
        // 设置位置
        // 屏幕水平正中心


        // 将 #RRGGBB 格式的颜色转换为 Color 对象
        var color = Color(
            android.graphics.Color.parseColor(config.textColor)
        )

        view.setTextColor(color.toArgb())
        view.setImage(config.image, mContext, "drawable")
        Log.d(TAG, "onInit: ${config.intent}")
        if (config.intent != null){
            view.setOnClickListener(config.intent!!)
        } else {
            view.setOnClickListener(null)
        }

        mWindowManager.addView(view, mLayoutParams)

        isShowing = true
    }


    fun showWindow() {
        if (!isShowing) {
            if (mLayoutParams == null) {
                createWindow(
                    mToastConfig!!,
                    landscape = isLandscape(mContext)
                )
            } else {
                mWindowManager.addView(mFloatLayout, mLayoutParams)
                isShowing = true
            }
        }
    }

    fun removeWindow() {
        mWindowManager.removeView(mFloatLayout)
        // 保存悬浮窗的位置


        isShowing = false
    }

}