package io.wasin.omo.ui

/**
 * Created by haxpor on 6/13/17.
 */
open class Box(x: Float, y: Float, width: Float, height: Float) {

    var x: Float = x
        protected set
    var y: Float = y
        protected set
    var width: Float = width
        protected set
    var height: Float = height
        protected set

    fun contains(x: Float, y: Float): Boolean {
        return x > this.x - width / 2 &&
                x < this.x + width / 2 &&
                y > this.y - height / 2 &&
                y < this.y + height / 2;
    }
}