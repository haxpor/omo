package io.wasin.omo.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.wasin.omo.Game

/**
 * Created by haxpor on 6/28/17.
 */
open class TextImage(text: String, x: Float, y: Float): Box(x, y, 50.0f * text.length, 50.0f) {

    private var fontSheets: Array<Array<TextureRegion>>
    var text: String = text.toLowerCase()   // only support for lowrer case as bitmap font will render the same thing
        set(value) {
            // calculate new width, and let height intact as it's the same
            width = 50.0f * value.length
            field = value
        }

    companion object {
        const val SIZE = 50
    }

    init {
        // process TextureRegion
        val sheet = Game.res.getAtlas("pack")!!.findRegion("fontsheet")
        fontSheets = sheet.split(SIZE, SIZE)
    }

    fun render(sb: SpriteBatch) {
        for (i in 0..text.length-1) {
            var cInt: Int = 'a'.toInt()
            when (text[i]) {
                in 'a'..'z' -> cInt = text[i] - 'a'
                in '0'..'9' -> cInt = text[i] - '0' + 27
                ' ' -> cInt = 26    // hard-code as per sheet is designed that way
                '+' -> cInt = 37   // hard-code as per sheet is designed that way, 26 chars + 10 digits + 1 space
                '-' -> cInt = 38   // hard-code as per sheet is designed that way, 26 chars + 10 digits + 1 space + 1 (+ symbol)
            }
            val row = cInt / fontSheets[0].size
            val col = cInt % fontSheets[0].size
            sb.draw(fontSheets[row][col],
                    x - width/2 + SIZE*i,
                    y - height/2)
        }
    }
}