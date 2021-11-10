package cn.zs.book.sanwei

import android.graphics.LinearGradient
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

class CustomViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//
//            Surface(
//                shape = RectangleShape,
//            ) {
//                CustomView()
//            }
//
//        }

        setContentView(R.layout.activity_custom_view)
    }
}

@Composable
fun CustomView() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        //距离左边屏幕距离
        val marginToLeft = 180f
        //距离屏幕下边距离
        val marginToBootom = 240f

        drawIntoCanvas { canvas ->
            val paint = Paint()
            paint.style = PaintingStyle.Fill
            paint.color = Color.Red

            canvas.translate(0f, height)
            canvas.scale(1f, -1f)

            canvas.translate(marginToLeft, marginToBootom)
//            canvas.drawCircle(Offset(100f, 100f), 100f, paint)

            //2.平行x轴线
            val line_paint = Paint()
            line_paint.strokeWidth = 2f
            line_paint.style = PaintingStyle.Stroke
            line_paint.color = Color(188, 188, 188, 100)
            //x轴距离右边也留80距离
//x轴距离右边也留80距离
            val x_scaleWidth1 = (size.width - marginToLeft - 80f)
            val grid_width1 = x_scaleWidth1 / 6

            val linePath = Path()
            linePath.lineTo(x_scaleWidth1, 0f)
            canvas.drawPath(linePath, line_paint)

            Log.e("CustomView", "Y轴间隔：${grid_width1 - 40f}")

            canvas.save()
            (0 until 3).forEach { index ->
                canvas.translate(0f, grid_width1 - 40f)
                canvas.drawPath(linePath, line_paint)
            }
            canvas.restore()

            drawTextXAxis(x_scaleWidth1, marginToLeft, grid_width1, canvas, paint)
            drawTextYAxis(marginToBootom, grid_width1, canvas, paint)

            val dataList = arrayListOf(0, 0, 0, 256, 0, 256, 0)
            drawCub2Circle(x_scaleWidth1, marginToLeft, grid_width1, dataList, canvas)

        }


    }
}

fun DrawScope.drawTextXAxis(
    x_scaleWidth: Float,
    marginToLeft: Float,
    grid_width: Float,
    canvas: androidx.compose.ui.graphics.Canvas,
    paint: Paint
) {


    var x_scaleWidth1 = x_scaleWidth
    var grid_width1 = grid_width
    x_scaleWidth1 = (size.width - marginToLeft - 80f)
    grid_width1 = x_scaleWidth1 / 6
    val text_paint = android.graphics.Paint()
    text_paint.strokeWidth = 2f
//    text_paint.style = android.graphics.Paint.Style.STROKE
    text_paint.color = Color.Red.toArgb()
    text_paint.textSize = 28f

    val textRect = Rect()
    canvas.save()

    canvas.scale(1f, -1f)
    (0 until 7).forEach { index ->
        canvas.nativeCanvas.translate(if (index == 0) 0f else grid_width1, 0f)
        val text = "11.${11 + index}"

        text_paint.getTextBounds(text, 0, text.length - 1, textRect)


        canvas.nativeCanvas.drawText(
            text, -textRect.width().toFloat() / 2, textRect.height().toFloat
                () * 2.5f,
            text_paint
        )
    }

    canvas.restore()

}

fun DrawScope.drawTextYAxis(
    marginToLeft: Float,
    grid_width: Float,
    canvas: androidx.compose.ui.graphics.Canvas,
    paint: Paint
) {
    var grid_width1 = grid_width
    val text_paint = android.graphics.Paint()
    text_paint.strokeWidth = 2f
//    text_paint.style = android.graphics.Paint.Style
    text_paint.color = Color.Red.toArgb()
    text_paint.textSize = 28f

    val textRect = Rect()

    canvas.save()
    (0 until 4).forEach { index ->
        canvas.nativeCanvas.translate(0f, if (index == 0) 0f else grid_width1 - 40f)
        var text = "0"

        if (index == 0) {
            text = "0"
        } else if (index == 1) {
            text = "500"
        } else if (index == 2) {
            text = "1k"
        } else if (index == 3) {
            text = "1.5k"
        }
        canvas.save()

        canvas.scale(1f, -1f)
        text_paint.getTextBounds(text, 0, text.length - 1, textRect)
        canvas.nativeCanvas.drawText(
            text, -textRect.width().toFloat() - 40f, textRect.height().toFloat() / 2,
            text_paint
        )
        canvas.restore()
    }
    canvas.restore()

}


fun DrawScope.drawCub2Circle(
    x_scaleWidth: Float,
    marginToLeft: Float,
    grid_width: Float,
    dataList: ArrayList<Int>,
    canvas: androidx.compose.ui.graphics.Canvas
) {
    var x_scaleWidth1 = x_scaleWidth
    var grid_width1 = grid_width
    x_scaleWidth1 = (size.width - marginToLeft - 80f)
    grid_width1 = x_scaleWidth1 / 6
    val text_paint = android.graphics.Paint()
    text_paint.strokeWidth = 2f
    text_paint.style = android.graphics.Paint.Style.FILL
    text_paint.color = android.graphics.Color.argb(100, 111, 111, 111)

    //500=grid_width-40 每个单位的长度的=像素长度
    val danweiY = (grid_width1 - 40) / 500
    val danweiX = (grid_width1)
    val linearGradient = LinearGradient(    0f,  1500 * danweiY,
        0f,
        0f,
        android.graphics.Color.argb(255, 229, 160, 144),
        android.graphics.Color.argb(255, 251, 244, 240),
        Shader.TileMode.CLAMP)

//    0f, 0,
//    0f,
//    0f,
//    android.graphics.Color.argb(255, 229, 160, 144),
//    android.graphics.Color.argb(255, 251, 244, 240),
//    Shader.TileMode.CLAMP

text_paint.shader = linearGradient
    val canvasPath = android.graphics.Path()



    for (index in 0 until dataList.size) {
        if (index == 0) {
            canvasPath.moveTo(0f, dataList[index].toFloat())
        } else {
            if (dataList[index - 1] == dataList[index]) {
                //两值相等，直接连接
                canvasPath.lineTo((grid_width1 * index), dataList[index].toFloat())
            } else {
                //两值不相等，通过曲线连接
                val point1 = PointF((grid_width1 * (index - 1)), dataList[index - 1].toFloat())
                val point2 = PointF((grid_width1 * (index)), dataList[index].toFloat())
                val center = PointF(
                    (point1.x + point2.x )/ 2, (point1.y + point2.y) / 2
                )

                val control1 = PointF(center.x,  point1.y)
                val control2 = PointF(center.x, point2.y)

                canvasPath.cubicTo(control1.x,control1.y,control2.x,control2.y, point2.x, point2.y)
            }
        }

        canvas.nativeCanvas.drawCircle(
            (grid_width1 * index),
            dataList[index].toFloat(),
            10f,
            text_paint
        )
    }

    canvas.nativeCanvas.drawPath(canvasPath, text_paint)

    //绘制外环红色线
    val line_paint = android.graphics.Paint()
    line_paint.strokeWidth = 3f
    line_paint.style = android.graphics.Paint.Style.STROKE
    line_paint.color = android.graphics.Color.argb(255, 212, 100, 77)
    canvas.nativeCanvas.drawPath(canvasPath, line_paint)



}