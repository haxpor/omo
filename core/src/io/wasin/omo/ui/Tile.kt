package io.wasin.omo.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.wasin.omo.Game

/**
 * Created by haxpor on 6/13/17.
 */
open class Tile(x: Float, y: Float, width: Float, height: Float): Box(x, y, width, height) {

    protected val light: TextureRegion
    protected val dark: TextureRegion

    var selected: Boolean = false

    init {
        val atlas = Game.res.getAtlas("pack")!!
        light = atlas.findRegion("light")
        dark = atlas.findRegion("dark")
    }

    open fun update(dt: Float) {
    }

    open fun render(sb: SpriteBatch) {
        if (selected) {
            sb.draw(light, x - width / 2, y - height / 2, width, height)
        }
        else {
            sb.draw(dark, x - width / 2, y - height / 2, width, height)
        }
    }
}