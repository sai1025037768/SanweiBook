package cn.zs.book.sanwei.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup
import kotlin.math.max

/**
 * @author Administrator
 * @name [FlowRadioGroup]
 * @time 2021/11/4 13:58
 * @describe
 */
internal class FlowRadioGroup : RadioGroup {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //测量子View
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        //最大行宽
        var maxWidth = 0
        //累计行高
        var totalHeight = 0

        //当前行的累计宽度
        var lineWidth = 0
        //当前行的高度
        var lineHeight = 0
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            //处理Margin
            val params = child.layoutParams as MarginLayoutParams
            val marginHorizontal = params.leftMargin + params.rightMargin
            val marginVertical = params.topMargin + params.bottomMargin


            //判断添加child后，是否要换行
            if (lineWidth + childWidth + marginHorizontal + paddingLeft + paddingRight> widthSize) {
                //要换行，lineWidth, lineHeight 重置, 高度增加
                // 更新maxWidth
                maxWidth = Math.max(maxWidth, lineWidth)

                //换行时，新行宽默认为新行第一View的宽度 + View的水平方向margin
                lineWidth = childWidth + marginHorizontal

                // 总高度累加，不包含新一行的高度
                totalHeight += lineHeight

                //换行时，新行高默认为新行第一View的高度 + View的竖直方向margin
                lineHeight = childHeight + marginVertical

            } else {
                //不换行，lineWidth增加
                lineWidth += childWidth + marginHorizontal
                // 更新当前行最大高度
                lineHeight = Math.max(lineHeight, childHeight + marginVertical)
            }

            //前面没有加上下一行的高度，处理最后一行的高度，宽度
            if (i == childCount - 1) {
                totalHeight += lineHeight
                maxWidth = Math.max(maxWidth, lineWidth)
            }
        }

        setMeasuredDimension(
            if (widthMode == MeasureSpec.EXACTLY) widthSize else
                maxWidth + paddingLeft + paddingRight, totalHeight + paddingTop + paddingBottom
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        super.onLayout(changed, l, t, r, b)

        // child 的布局位置
        var preLeft = paddingLeft
        var preTop = paddingTop

        var maxHeight = 0;

        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)

            //处理Margin
            val params = child.layoutParams as MarginLayoutParams
            val marginHorizontal = params.leftMargin + params.rightMargin
            val marginVertical = params.topMargin + params.bottomMargin

            //是否需要换行
            if (preLeft + child.measuredWidth + marginHorizontal + paddingRight > (r - l)) {
                //需要换行，preLeft重置, preTop需要增加
                preLeft = paddingLeft
                preTop += maxHeight
                maxHeight = child.measuredHeight + marginVertical

            }else{
                maxHeight = Math.max(maxHeight, child.measuredHeight + marginVertical)
            }

            child.layout(
                preLeft + params.leftMargin,
                preTop + params.topMargin,
                preLeft + params.leftMargin + child.measuredWidth,
                preTop + params.topMargin + child.measuredHeight
            )

            preLeft += child.measuredWidth + marginHorizontal


        }
    }
}