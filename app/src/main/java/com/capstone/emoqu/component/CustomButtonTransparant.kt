package com.capstone.emoqu.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.capstone.emoqu.R

class CustomButtonTransparant @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {
    private var txtColor: Int = 0
    private var color: Drawable


    init {
        txtColor = ContextCompat.getColor(context, R.color.light_text_grey_12)
        color = ContextCompat.getDrawable(context, R.drawable.bg_stroke) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setTextColor(txtColor)
        textSize = 14f
        gravity = Gravity.CENTER
    }
}
