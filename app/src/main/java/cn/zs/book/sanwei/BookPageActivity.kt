package cn.zs.book.sanwei

import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.zs.book.sanwei.entity.NamePoint

class BookPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            androidx.compose.material.Surface(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center

                ) {

                    BookPageView(
                        modifier = Modifier
                            .size(240.dp, 360.dp)
                    );

//                    BlendMode()
                }
            }


        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun BookPageView(modifier: Modifier) {

    var startPoint = PointF(0f, 0f) //开始点
    var delta = Offset.Zero
    val movePoint: MutableState<PointF?> = mutableStateOf(null)


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

    Canvas(modifier = modifier
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    // 拖动开始
                    startPoint.x = offset.x
                    startPoint.y = offset.y

                    Log.e("PointerinputScope" , "${size}")

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
                    }else{
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


        drawIntoCanvas { canvas ->

            if (movePoint.value == null) {
                pathPaint.color = Color.Green
                canvas.drawRect(0f, 0f, size.width, size.height, pathPaint)
                return@Canvas
            }

            /**
             * 以Down 触摸点为基准，View分为三个部分，上部分为右上角翻页，中间部分为横向翻页，下部分为右下角翻页
             *
             * 上部分 f点坐标为 （size.width, 0）
             * 下部分 f点坐标为 （size.width, size.height）
             * 中间部分 绘制 Rect
             */

            /**
             * 以Down 触摸点为基准，View分为三个部分，上部分为右上角翻页，中间部分为横向翻页，下部分为右下角翻页
             *
             * 上部分 f点坐标为 （size.width, 0）
             * 下部分 f点坐标为 （size.width, size.height）
             * 中间部分 绘制 Rect
             */
            movePoint.value?.let {
                a.copyFrom(it)

                //先处理中间部分
                if (startPoint.y > size.height / 3 && startPoint.y < size.height * 2 / 3) {
                    a.y = size.height - 1f

                    //下部分
                    f.x = size.width
                    f.y = size.height

                    g.x = (a.x + f.x) / 2
                    g.y = (a.y + f.y) / 2

                    m.x = g.x
                    m.y = f.y

                    e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x)
                    e.y = f.y

                    c.x = e.x - (f.x - e.x) / 2
                    if (c.x < 0) {
                        c.x = 0f
                    }
                    c.y = f.y

                    o.x = f.x
                    o.y = g.y

                    h.x = f.x
                    h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y)

                    j.x = f.x
                    j.y = h.y - (f.y - h.y) / 2
                    if (j.y < 0) {
                        j.y = 0f
                    }

                    b.copyFrom(crossPointLines(a, e, c, j))
                    k.copyFrom(crossPointLines(a, h, c, j))

                    d.x = ((c.x + b.x) / 2 + e.x) / 2
                    d.y = ((c.y + b.y) / 2 + e.y) / 2

                    i.x = ((k.x + j.x) / 2 + h.x) / 2
                    i.y = ((k.y + j.y) / 2 + h.y) / 2

                    pathA.reset()
                    pathA.lineTo(size.width, 0f)
                    pathA.lineTo(j.x, j.y)
                    pathA.quadraticBezierTo(h.x, h.y, k.x, k.y)
                    pathA.lineTo(a.x, a.y)
                    pathA.lineTo(b.x, b.y)
                    pathA.quadraticBezierTo(e.x, e.y, c.x, c.y)
                    pathA.lineTo(0f, size.height)
                    pathA.close()

                    pathC.reset()
                    pathC.moveTo(a.x, a.y)
                    pathC.lineTo(k.x, k.y)
                    pathC.lineTo(i.x, i.y)
                    pathC.lineTo(d.x, d.y)
                    pathC.lineTo(b.x, b.y)
                    pathC.close()
                } else if (startPoint.y >= size.height * 2 / 3) {
                    if(it.y < size.height * 2 / 3 ){
                        it.y = size.height * 2 / 3
                    }

                    //下部分
                    f.x = size.width
                    f.y = size.height

                    g.x = (a.x + f.x) / 2
                    g.y = (a.y + f.y) / 2

                    m.x = g.x
                    m.y = f.y

                    e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x)
                    e.y = f.y

                    c.x = e.x - (f.x - e.x) / 2
                    if (c.x < 0) {
                        c.x = 0f
                    }
                    c.y = f.y

                    o.x = f.x
                    o.y = g.y

                    h.x = f.x
                    h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y)

                    j.x = f.x
                    j.y = h.y - (f.y - h.y) / 2
                    if (j.y < 0) {
                        j.y = 0f
                    }

                    b.copyFrom(crossPointLines(a, e, c, j))
                    k.copyFrom(crossPointLines(a, h, c, j))

                    d.x = ((c.x + b.x) / 2 + e.x) / 2
                    d.y = ((c.y + b.y) / 2 + e.y) / 2

                    i.x = ((k.x + j.x) / 2 + h.x) / 2
                    i.y = ((k.y + j.y) / 2 + h.y) / 2

                    pathA.reset()
                    pathA.lineTo(size.width, 0f)
                    pathA.lineTo(j.x, j.y)
                    pathA.quadraticBezierTo(h.x, h.y, k.x, k.y)
                    pathA.lineTo(a.x, a.y)
                    pathA.lineTo(b.x, b.y)
                    pathA.quadraticBezierTo(e.x, e.y, c.x, c.y)
                    pathA.lineTo(0f, size.height)
                    pathA.close()

                    pathC.reset()
                    pathC.moveTo(a.x, a.y)
                    pathC.lineTo(k.x, k.y)
                    pathC.lineTo(i.x, i.y)
                    pathC.lineTo(d.x, d.y)
                    pathC.lineTo(b.x, b.y)
                    pathC.close()


                } else {

                    if(it.y > size.height  / 3 ){
                        it.y = size.height / 3
                    }
                    //上部分
                    f.x = size.width
                    f.y = 0f

                    g.x = (a.x + f.x) / 2
                    g.y = (a.y + f.y) / 2

                    m.x = g.x
                    m.y = f.y

                    e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x)
                    e.y = f.y

                    c.x = e.x - (f.x - e.x) / 2
                    if (c.x < 0) {
                        c.x = 0f
                    }
                    c.y = f.y

                    o.x = f.x
                    o.y = g.y

                    h.x = f.x
                    h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y)

                    j.x = f.x
                    j.y = h.y - (f.y - h.y) / 2
                    if (j.y > size.height) {
                        j.y = size.height
                    }

                    b.copyFrom(crossPointLines(a, e, c, j))
                    k.copyFrom(crossPointLines(a, h, c, j))

                    d.x = ((c.x + b.x) / 2 + e.x) / 2
                    d.y = ((c.y + b.y) / 2 + e.y) / 2

                    i.x = ((k.x + j.x) / 2 + h.x) / 2
                    i.y = ((k.y + j.y) / 2 + h.y) / 2

                    pathA.reset()
                    pathA.moveTo(0f, 0f)
                    pathA.lineTo(0f, size.height)
                    pathA.lineTo(size.width, size.height)
                    pathA.lineTo(j.x, j.y)
                    pathA.quadraticBezierTo(h.x, h.y, k.x, k.y)
                    pathA.lineTo(a.x, a.y)
                    pathA.lineTo(b.x, b.y)
                    pathA.quadraticBezierTo(e.x, e.y, c.x, c.y)
                    pathA.close()

                    pathC.reset()
                    pathC.moveTo(a.x, a.y)
                    pathC.lineTo(k.x, k.y)
                    pathC.lineTo(i.x, i.y)
                    pathC.lineTo(d.x, d.y)
                    pathC.lineTo(b.x, b.y)
                    pathC.close()
                }


                pathB.reset()
                pathB.lineTo(0f, size.height)
                pathB.lineTo(size.width, size.height)
                pathB.lineTo(size.width, 0f)
                pathB.close()




                pathPaint.color = Color.Green
                canvas.saveLayer(Rect(Offset.Zero, size), Paint())
//                val bitmap = ImageBitmap(
//                    size.width.toInt(),
//                    size.height.toInt(),
//                    ImageBitmapConfig.Argb8888
//                )
//                val bitmpaCanvas = Canvas(bitmap)


//                bitmpaCanvas.drawPath(pathA, pathPaint)
//                canvas.drawImage(bitmap, Offset.Zero, pathPaint)
                canvas.drawPath(pathA, pathPaint)



                pathPaint.color = Color.Yellow
                pathPaint.blendMode = BlendMode.DstOver


                canvas.drawPath(pathC, pathPaint)



                pathC.reset();
                pathC.moveTo(c.x, c.y)
                pathC.quadraticBezierTo(e.x, e.y, b.x, b.y)
                pathC.lineTo(a.x, a.y)
                pathC.lineTo(k.x, k.y)
                pathC.quadraticBezierTo(h.x, h.y, j.x, j.y)

//                canvas.drawPath(pathA, linePaint)
                canvas.drawPath(pathC, linePaint)

                pathPaint.color = Color.Blue
                pathPaint.blendMode = BlendMode.DstOver
                canvas.drawPath(pathB, pathPaint)

                canvas.restore()

                pathPaint.blendMode = BlendMode.SrcOver


                val textPaint = android.graphics.Paint()
                textPaint.color = Color.White.toArgb()
                textPaint.textSize = 14.sp.toPx()
                points.forEach { point ->


                    canvas.drawCircle(Offset(point.x, point.y), 10f, circlePaint)
                    canvas.nativeCanvas.drawText(point.name, point.x, point.y - 16f, textPaint)

                }
            }

        }


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




