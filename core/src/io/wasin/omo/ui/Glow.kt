package io.wasin.omo.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Created by haxpor on 6/29/17.
 */
class Glow(x: Float, y: Float, width: Float, height: Float): Tile(x, y, width, height) {

    companion object {
        const val EFFECT_DURATION: Float = 0.5f
    }

    private var alpha: Float = 1.0f

    var shouldBeRemoved: Boolean = false
    var timer: Float = 0f

    override fun update(dt: Float) {
        timer += dt
        width += dt * 200f
        height += dt * 200f

        if (timer >= EFFECT_DURATION) {
            shouldBeRemoved = true
        }
    }

    override fun render(sb: SpriteBatch) {
        alpha = 1 - timer / EFFECT_DURATION

        // get current color from spritebatch
        val color = sb.color

        sb.setColor(1f, 1f, 1f, alpha)
        sb.draw(light, x - width/2, y - height/2, width, height)
        sb.color = color
    }
}