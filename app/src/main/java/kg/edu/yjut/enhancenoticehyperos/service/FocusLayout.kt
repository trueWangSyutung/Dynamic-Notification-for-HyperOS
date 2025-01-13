/*
 * Copyright (C) 2024 The LiteNote Project
 * @author OpenAcademic
 * @version 1.0
 * 
 */
package  kg.edu.yjut.enhancenoticehyperos.service

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Scroller
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kg.edu.yjut.enhancenoticehyperos.R


@SuppressLint("MissingInflatedId")
class FocusLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private  var mText: TextView
    private  var mView: View
    private  var mSpaceArea : View
    private  var mCopy : ImageView
    private  var mSave : ImageView
    private  var mShowText: TextView
    private  var mExpressText: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.qujianma2, this, true)
        mView  = view
        mText = view.findViewById(R.id.contentqjmText)
        mSpaceArea = view.findViewById(R.id.spaceArea)
        mCopy = view.findViewById(R.id.copy)
        mSave = view.findViewById(R.id.save)
        mShowText = view.findViewById(R.id.contentqjmShow)
        mExpressText = view.findViewById(R.id.contentqjmKuaidi)

    }

    fun setSaveUnVisible() {
        mSave.visibility = View.GONE
    }
    fun setExpressText(text: String) {
        mExpressText.text = text
    }

    fun setShowText(text: String) {
        mShowText.text = text
    }

    fun setText(text: String) {
        mText.text = text
        // 设置超出隐藏
        mText.ellipsize = TextUtils.TruncateAt.END
        mText.maxLines = 1


    }
    fun setTextSize(size: Float) {
        mText.textSize = size
    }




    // 设置文本框可以水平滚动
    fun setHorizontallyScrolling() {
        mText.setHorizontallyScrolling(true)
        // 设置自动滚动
        mText.isSelected = true
        // 设置滚动速度
        mText.setScroller(Scroller(context))

    }

    fun setOnClickListener(
        function: () -> Unit
    ) {
        mSpaceArea.setOnClickListener {
            // 结束服务
            function()
        }
    }

    fun setOnCopyClickListener(
        function: () -> Unit
    ) {
        mCopy.setOnClickListener {
            // 结束服务
            function()
        }
    }

    fun setOnSaveClickListener(
        function: () -> Unit
    ) {
        mSave.setOnClickListener {
            // 结束服务
            function()
        }
    }






}