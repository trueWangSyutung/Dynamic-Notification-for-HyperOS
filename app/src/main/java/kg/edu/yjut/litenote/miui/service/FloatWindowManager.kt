package kg.edu.yjut.litenote.miui.service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kg.edu.yjut.litenote.miui.ToastConfig

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
        logMode: Boolean = false
    ) {
        mToastConfig = config
        // 对象配置操作使用apply，额外的处理使用also
        mlogMode = logMode
        var view = mFloatLayout
        // 获取屏幕的宽高 dp
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dpi = mContext.resources.displayMetrics.density
        val height = wm.defaultDisplay.height / dpi
        val width = wm.defaultDisplay.width  / dpi
        var sp = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)

        // 设置悬浮窗的宽高
        Log.d(TAG, "onInit: ${width} ${height}")
        // 设置位置
        // 屏幕水平正中心
        mLayoutParams = WindowManager.LayoutParams().apply {
            type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            // 设置图片格式，效果为背景透明
            format = PixelFormat.RGBA_8888
            // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            // 调整悬浮窗显示的停靠位置为右侧置顶
            gravity = Gravity.TOP or Gravity.END
            // 以屏幕左上角为原点，设置x、y初始值
            x = sp.getInt("lastX", 0)
            y = sp.getInt("lastY", 0)


        }
        mLayoutParams!!.width = sp.getInt("lastWight", width.toInt())
        mLayoutParams!!.height = sp.getInt("lastHeight", 100)
        // 设置悬浮窗的宽高
        view.setText(config.text)
        view.setTextSize(sp.getInt("lastTextSize", 10).toFloat())
        view.setHorizontallyScrolling()
        // 将 #RRGGBB 格式的颜色转换为 Color 对象
        var color = Color(
            android.graphics.Color.parseColor(config.textColor)
        )

        view.setTextColor(color.toArgb())
        view.setImage(config.image, mContext, "drawable")
        if (config.intent != null){
            view.setOnClickListener(config.intent!!)
        } else {
            view.setOnClickListener(null)
        }
        // 设置拖动事件
        if (logMode){
            setOnTouchListener()
        }


        mWindowManager.addView(view, mLayoutParams)

        isShowing = true
    }

    // 设置拖动事件
    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListener() {
        mFloatLayout.setOnTouchListener(View.OnTouchListener { v, event ->
            // 获取相对屏幕的坐标，即以屏幕左上角为原点
            mLayoutParams!!.x = event.rawX.toInt()
            mLayoutParams!!.y = event.rawY.toInt()
            // 更新悬浮窗的位置
            var sp = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)
            var editor = sp.edit()
            editor.putInt("lastX", mLayoutParams!!.x)
            editor.putInt("lastY", mLayoutParams!!.y)
            editor.apply()

            // 刷新
            mWindowManager.updateViewLayout(mFloatLayout, mLayoutParams)
            false
        })
    }

    fun showWindow() {
        if (!isShowing) {
            if (mLayoutParams == null) {
                createWindow(
                    mToastConfig!!
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