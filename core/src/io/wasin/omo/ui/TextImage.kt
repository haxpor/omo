package io.wasin.omo.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.wasin.omo.Game

/**
 * Created by haxpor on 6/28/17.
 */
class TextImage(text: String, x: Float, y: Float): Box(x, y, 50.0f * text.length, 50.0f) {

    private var fontSheets: Array<Array<TextureRegion>>
    private var text: String = text.toLowerCase()   // only support for lowrer case as bitmap font will render the same thing

    companion object {
        const val SIZE = 50
    }

    init {
        // process TextureRegion
        val sheet = Game.res.getAtlas("pack")!!.findRegion("fontsheet")
        val numCols = sheet.regionWidth / SIZE
        val numRows = sheet.regionHeight / SIZE

        fontSheets = Array(numRows, {
            row -> Array(numCols, { col -> TextureRegion(sheet, SIZE*col, SIZE*row, SIZE, SIZE) })
        })
    }

    fun render(sb: SpriteBatch) {
        for (i in 0..text.length-1) {
            val cInt = text[i] - 'a'
            val row = cInt / fontSheets[0].size
            val col = cInt % fontSheets[0].size
            sb.draw(fontSheets[row][col],
                    x - width/2 + SIZE*i,
                    y - height/2)
        }
    }
}