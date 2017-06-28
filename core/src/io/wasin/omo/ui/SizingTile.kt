package io.wasin.omo.ui

/**
 * Created by haxpor on 6/29/17.
 */
class SizingTile(x: Float, y: Float, width: Float, height: Float): Tile(x, y, 0f, 0f)  {

    companion object {
        const val EFFECT_DURATION: Float = 0.5f
    }

    protected var maxWidth: Float = width - 8
    protected var maxHeight: Float = height - 8

    var timer: Float = 0f

    override fun update(dt: Float) {
        if (timer < EFFECT_DURATION) {
            timer += dt
            width = (timer / EFFECT_DURATION) * maxWidth
            height = (timer / EFFECT_DURATION) * maxHeight

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