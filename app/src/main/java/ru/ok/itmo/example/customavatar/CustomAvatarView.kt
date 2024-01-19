package ru.ok.itmo.example.customavatar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import kotlin.math.min
import kotlin.random.Random

class CustomAvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var fillPaint: Paint = Paint()
    private var textPaint: Paint = Paint()
    private var backgroundColor: Int
    private var bitmap: Bitmap? = null
    private var textSizeW: Int = 0
    private var textSizeH: Int = 0
    private var text: String = ""

    init {
        backgroundColor =
            Color.rgb(
                Random.nextInt(0, 215),
                Random.nextInt(0, 215),
                Random.nextInt(0, 215)
            )
        fillPaint.color = backgroundColor

        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
    }

    private val circle = RectF()


    fun setBgColor(@ColorRes colorId: Int) {
        backgroundColor = resources.getColor(colorId, context.theme)
        invalidate()
    }


    fun setText(text: String) {
        if (text.isNotBlank()) {
            val title = text.split(" ".toRegex())
                .take(2)
                .joinToString("") { it[0].uppercase() }

            val bounds = Rect()
            textPaint.getTextBounds(title, 0, title.length, bounds)

            this.text = title
            textSizeW = bounds.width()
            textSizeH = bounds.height()
        }

        invalidate()
    }

    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val save = canvas.save()
        val radius = min(width, height) / 2f

        circle.set(0f, 0f, radius * 2f, radius * 2f)


        if (bitmap != null) {
            bitmap!!.width = (radius * 2).toInt()
            bitmap!!.height = (radius * 2).toInt()
            canvas.drawBitmap(bitmap!!, 0f, 0f, fillPaint)

            return
        }


        val char: Char
        if (text.isNotEmpty()) {
            char = text.get(0)
        } else {
            char = 'A'
        }

        val num = char.toInt()

        backgroundColor =
            Color.rgb(
                num + 30,
                num - 30,
                num
            )
        fillPaint.apply { color = backgroundColor }
        canvas.drawRoundRect(circle, radius, radius, fillPaint)
        canvas.drawText(text, radius - textSizeW / 2, radius + textSizeH / 2, textPaint)
        canvas.restoreToCount(save)
    }
}
