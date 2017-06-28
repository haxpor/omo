package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.ui.TextImage

/**
 * Created by haxpor on 6/28/17.
 */
class Difficulty(gsm: GameStateManager): GameState(gsm) {

    private var touchPos: Vector3 = Vector3.Zero
    private var buttons: Array<TextImage>

    init {
        val texts = arrayOf("easy", "normal", "hard", "insane")
        buttons = Array(texts.size, { i -> TextImage(texts[i], Game.V_WIDTH/2, Game.V_HEIGHT/2 + 100 - 70*i) })
    }

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.x = Gdx.input.x.toFloat()
            touchPos.y = Gdx.input.y.toFloat()
            cam.unproject(touchPos)

            for (i in 0..buttons.size-1) {
                if (buttons[i].contains(touchPos.x, touchPos.y)) {
                    gsm.setState(Play(gsm, Play.Difficulty.values()[i]))
                }
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.projectionMatrix = hudCam.combined

        sb.begin()
        for (b in buttons) {
            b.render(sb)
        }
        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {

    }
}