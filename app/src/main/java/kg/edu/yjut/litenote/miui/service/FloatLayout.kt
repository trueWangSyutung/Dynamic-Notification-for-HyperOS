package kg.edu.yjut.litenote.miui.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Scroller
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kg.edu.yjut.litenote.R
import org.w3c.dom.Text

class FloatLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private  var mText: TextView
    private  var mImg: ImageView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.lld, this, true)
        mText = view.findViewById(R.id.contentText)
        mImg = view.findViewById(R.id.img)

    }

    fun setText(text: String) {
        mText.text = text

    }
    fun setTextSize(size: Float) {
        mText.textSize = size
    }

    fun setTextColor(color: Int) {
        mText.setTextColor(color)
    }

    @SuppressLint("DiscouragedApi")
    fun setImage(resId: String, context: Context, type : String) {
        if (type == "drawable") {
            mImg.setImageResource(context.resources.getIdentifier(resId, type, context.packageName))
        }
    }

    // 设置文本框可以水平滚动
    fun setHorizontallyScrolling() {
        mText.setHorizontallyScrolling(true)
        // 设置自动滚动
        mText.isSelected = true
        // 设置滚动速度
        mText.setScroller(Scroller(context))

    }

    fun setOnClickListener(pendingIntent: PendingIntent) {
        mImg.setOnClickListener(OnClickListener {
            pendingIntent.send(
                context,
                0,
                Intent(),
                null,
                null,
                null
            )
        })

    }





}