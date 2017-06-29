package io.wasin.omo.ui

/**
 * Created by haxpor on 6/29/17.
 */
open class SizingTile(x: Float, y: Float, width: Float, height: Float): Tile(x, y, 0f, 0f)  {

    protected var maxWidth: Float = width - 8
    protected var maxHeight: Float = height - 8

    var timer: Float = 0f
    var effectDuration: Float = 0.5f

    override fun update(dt: Float) {
        if (timer < effectDuration) {
            timer += dt
            width = (timer / effectDuration) * maxWidth
            height = (timer / effectDuration) * maxHeight

            if (width > maxWidth) {
                width = maxWidth
            }
            if (width < 0) {
                width = 0f
            }
            if (height > maxHeight) {
                height = maxHeight
            }
            if (height < 0) {
                height = 0f
            }
        }
    }
}