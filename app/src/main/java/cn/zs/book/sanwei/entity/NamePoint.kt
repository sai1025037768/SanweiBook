package cn.zs.book.sanwei.entity

import android.graphics.PointF

/**
 * @name [NamePoint]
 * @author Administrator
 * @time 2021/11/8 17:09
 * @describe
 */
class NamePoint {

    var x = 0f
    var y = 0f
    var name = ""

    constructor()
    constructor(name: String) {
        this.name = name
    }

    constructor(name: String, x: Float, y: Float) {
        this.name = name;
        this.x = x
        this.y = y
    }

    constructor(name: String, pointF: PointF) {
        this.name = name;
        this.x = pointF.x
        this.y = pointF.y
    }

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    constructor(pointF: PointF) {
        this.x = pointF.x
        this.y = pointF.y
    }

    fun copyFrom(pointF: PointF) {
        this.x = pointF.x
        this.y = pointF.y
    }

}

