package cn.zs.book.sanwei.ui.view

import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import cn.zs.book.sanwei.entity.NamePoint

/**
 * @name [BookPageView]
 * @author Administrator
 * @time 2021/11/10 14:00
 * @describe
 */
/**
 * 限制翻页最大距离：
 *  1. C点坐标不能小于0
 *
 */
internal enum class Style {
    LEFT, //点击左侧区域
    TOP_RIGHT,//点击右上角区域，f点在右上角
    MIDDLE,//点击中间区域，弹出菜单
    BOTTOM_RIGHT,//点击右下角区域，f点在右下角
    RIGHT//点击在右侧区域，横向翻页
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun BookPageView(modifier: Modifier) {

    var startPoint = PointF(0f, 0f) //开始点
    var delta = Offset.Zero
    val movePoint: MutableState<PointF?> = mutableStateOf(null)

    var style: Style? = null

    /**
     * Canvas 外重组时不会再次触发
     */
    val points = arrayListOf<NamePoint>()

    val circlePaint = Paint()
    circlePaint.color = Color.Red

    val linePaint = Paint()
    linePaint.color = Color.Red
    linePaint.style = PaintingStyle.Stroke
    linePaint.strokeWidth = 4f

    val pathPaint = Paint()
    pathPaint.isAntiAlias = true
    pathPaint.color = Color.Green

    val a = NamePoint("a") //触摸点
    val f = NamePoint("f")
    val g = NamePoint("g")
    val n = NamePoint("n")

    val e = NamePoint("e")  //贝塞尔曲线1控制点
    val b = NamePoint("b") //贝塞尔曲线1开始点
    val c = NamePoint("c") //贝塞尔曲线1结束点

    val h = NamePoint("h") //贝塞尔曲线2控制点
    val k = NamePoint("k") //贝塞尔曲线2开始点
    val j = NamePoint("j")  //贝塞尔曲线2结束点

    val d = NamePoint("d") //贝塞尔曲线1顶点
    val i = NamePoint("i")//贝塞尔曲线2顶点

    val m = NamePoint("m")
    val o = NamePoint("o")

    points.add(a)
    points.add(f)
    points.add(g)
    points.add(b)
    points.add(d)
    points.add(e)
    points.add(c)
    points.add(k)
    points.add(i)
    points.add(j)
    points.add(h)

    val pathA = Path()
    val pathB = Path()
    val pathC = Path()

    Log.e("BookPageView ", "${pathPaint.blendMode}")

    Canvas(modifier = modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset ->
                if (offset.x < size.width / 3) {
                    style = Style.LEFT
                } else if (offset.x > size.width / 3 && offset.y < size.height / 3) {
                    style = Style.TOP_RIGHT
                } else if (offset.x > size.width / 3 && offset.x < size.width * 2 / 3 && offset.y > size
                        .height / 3 && offset.y < size
                        .height * 2 / 3
                ) {
                    style = Style.MIDDLE
                } else if (offset.x > size.width * 2 / 3 && offset.y > size
                        .height / 3 && offset.y < size
                        .height * 2 / 3
                ) {
                    style = Style.RIGHT
                } else if (offset.x > size.width / 3 && offset.y > size
                        .height * 2 / 3
                ) {
                    style = Style.BOTTOM_RIGHT
                }

                a.x = offset.x
                a.y = offset.y
                when (style) {
                    Style.LEFT, Style.RIGHT -> {

                    }
                    Style.TOP_RIGHT -> {
                        f.x = size.width.toFloat()
                        f.y = 0f
                    }
                    Style.MIDDLE -> {

                    }
                    Style.BOTTOM_RIGHT -> {
                        f.x = size.width.toFloat()
                        f.y = size.height.toFloat()
                    }
                }

                // 拖动开始
                startPoint.x = offset.x
                startPoint.y = offset.y

            },
            onDragEnd = {
                // 拖动结束
                movePoint.value = null

            },
            onDragCancel = {
                // 拖动取消
                movePoint.value = null
            },
            onDrag = { change: PointerInputChange, dragAmount: Offset ->


                // 拖动时
                if (movePoint.value == null) {
                    movePoint.value = PointF(
                        startPoint.x + dragAmount.x, startPoint.y +
                                dragAmount.y
                    )
                } else {
                    if (Math.abs(dragAmount.x) > 0) {
                        delta = dragAmount
                        movePoint.value = PointF(
                            movePoint.value!!.x + dragAmount.x, movePoint.value!!.y +
                                    dragAmount.y
                        )
                    }
                }

                Log.e("BookPageView 手动移动", "${dragAmount.x}")
            }
        )
    }) {

    }


    fun setTouchPoint(x: Float, y: Float, style: Style) {
        TODO("Not yet implemented")
    }

}


private fun crossPointLines(
    point1: NamePoint,
    point2: NamePoint,
    point3: NamePoint,
    point4: NamePoint,
): PointF {
    var x1 = point1.x
    var y1 = point1.y
    var x2 = point2.x
    var y2 = point2.y
    var x3 = point3.x
    var y3 = point3.y
    var x4 = point4.x
    var y4 = point4.y

    val x = ((x3 * y4 - x4 * y3) / (x3 - x4) - (x1 * y2 - x2 * y1) / (x1 - x2)) / ((y1 - y2) /
            (x1 - x2) - (y3 - y4) / (x3 - x4))
    val y = (y1 - y2) / (x1 - x2) * x + (x1 * y2 - x2 * y1) / (x1 - x2)

    return PointF(x, y)
}

