package io.wasin.omo.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Created by haxpor on 6/29/17.
 *
 * ExpandingTile has ability to expand, then contract whenever need it. Expanding, or contracting uses
 * duration of effectDuration to do its stuff.
 */
class ExpandingTile(x: Float, y: Float, width: Float, height: Float): SizingTile(x, y, width, height) {

    private var expanding: Boolean = true
    private var contracting: Boolean = false

    var isDoneExpanding: Boolean = false
        get() = (maxWidth - width < 0.0001f && maxHeight - height < 0.0001f)

    init {
        maxWidth = width
        maxHeight = height
    }

    override fun update(dt: Float) {
        if (expanding) {
            super.update(dt)
        }
        else if (contracting) {
            if (timer < effectDuration) {
                timer += dt
                width = maxWidth - (timer/effectDuration) * maxWidth
                height = maxHeight - (timer/effectDuration) * maxHeight

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

    override fun render(sb: SpriteBatch) {
        // get original color to set it back later
        val color = sb.color

        sb.setColor(0f, 0f, 0f, 1f)
        sb.draw(dark, x - width / 2, y - height / 2, width, height)

        sb.color = color
    }

    fun setToContract() {
        expanding = false
        contracting = true
        timer = 0f
    }
}