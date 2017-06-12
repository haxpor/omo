package io.wasin.omo.ui

/**
 * Created by haxpor on 6/13/17.
 */
open class Box(x: Float, y: Float, width: Float, height: Float) {

    protected var x: Float = x
    protected var y: Float = y
    protected var width: Float = width
    protected var height: Float = height

    fun contains(x: Float, y: Float): Boolean {
        return x > this.x - width / 2 &&
                x < this.x + width / 2 &&
                y > this.y - height / 2 &&
                y < this.y + height / 2;
    }
}