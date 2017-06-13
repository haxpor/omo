package io.wasin.omo.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.wasin.omo.Game

/**
 * Created by haxpor on 6/13/17.
 */
class Tile(x: Float, y: Float, width: Float, height: Float): Box(x, y, 0f, 0f) {
    private val light: TextureRegion
    private val dark: TextureRegion
    private var timer: Float = 0f
    private var maxTime: Float = 0.5f
    private var totalWidth: Float = width - 8
    private var totalHeight: Float = height - 8

    var selected: Boolean = false

    init {
        val atlas = Game.res.getAtlas("pack")!!
        light = atlas.findRegion("light")
        dark = atlas.findRegion("dark")
    }

    fun update(dt: Float) {
        if (timer < maxTime) {
            timer += dt
            width = (timer / maxTime) * totalWidth
            height = (timer / maxTime) * totalHeight

            if (width > totalWidth) {
                width = totalWidth
            }
            if (width < 0) {
                width = 0f
            }
            if (height > totalHeight) {
                height = totalHeight
            }
            if (height < 0) {
                height = 0f
            }
        }
    }

    fun render(sb: SpriteBatch) {
        if (selected) {
            sb.draw(light, x - width / 2, y - height / 2, width, height)
        }
        else {
            sb.draw(dark, x - width / 2, y - height / 2, width, height)
        }
    }

    fun setTimer(timer: Float) {
        this.timer = timer
    }
}