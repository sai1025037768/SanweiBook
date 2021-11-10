package cn.zs.book.sanwei.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.geometry.Offset
import android.graphics.Bitmap

import android.graphics.RectF




/**
 * @name [XferModeView]
 * @author Administrator
 * @time 2021/11/9 14:00
 * @describe
 */
class XferModeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val size = 300
    val radius = size / 3f




    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val circlPaint = Paint()
        circlPaint.color = Color.RED
//        canvas.drawCircle(radius, radius, radius, circlPaint)
        val dstBitmap = makeDst(radius.toInt() * 2, radius.toInt() * 2)
        val srcBitmap = makeSrc(radius.toInt() * 2, radius.toInt()* 2)


        val saveLayer = canvas.saveLayer(null, null)
        canvas.drawBitmap(dstBitmap, 0f, 0f, circlPaint)


        val rectPaint = Paint()
        rectPaint.color = Color.BLUE
//        rectPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
//        val rect = Rect(
//            radius.toInt(), radius.toInt(), measuredWidth, measuredHeight
//        )
//
////        canvas.drawRect(rect, rectPaint)
//
        circlPaint.color = Color.BLUE
        circlPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        canvas.drawBitmap(srcBitmap, radius, radius, circlPaint)
        circlPaint.xfermode =null
        canvas.restoreToCount(saveLayer)
    }

    fun makeDst(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = Color.RED
        c.drawOval(RectF(0f, 0f, (w * 3 / 4).toFloat(), (h * 3 / 4).toFloat()), p)
        return bm
    }

    // create a bitmap with a rect, used for the "src" image
    fun makeSrc(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = Color.BLUE
        c.drawRect(0f,0f, w.toFloat(), h.toFloat(), p
        )
        return bm
    }

}