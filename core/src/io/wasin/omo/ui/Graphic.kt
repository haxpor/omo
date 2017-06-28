package io.wasin.omo.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * Created by haxpor on 6/28/17.
 */
class Graphic(image: TextureRegion, x: Float, y: Float): Box(x, y, image.regionWidth.toFloat(), image.regionHeight.toFloat()) {
    private var image: TextureRegion = image

    fun render(sb: SpriteBatch) {
        sb.draw(image, x - width/2, y - height/2)
    }
}