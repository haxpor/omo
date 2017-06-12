package io.wasin.omo.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.wasin.omo.Game

/**
 * Created by haxpor on 6/13/17.
 */
class Tile(x: Float, y: Float, width: Float, height: Float): Box(x, y, width - 8, height - 8) {
    private val light: TextureRegion
    private val dark: TextureRegion

    private var selected: Boolean = false

    init {
        val atlas = Game.res.getAtlas("pack")!!
        light = atlas.findRegion("light")
        dark = atlas.findRegion("dark")
    }

    fun update(dt: Float) {

    }

    fun render(sb: SpriteBatch) {
        sb.draw(light, x - width / 2, y - height / 2, width, height)
    }
}