package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.ui.TextImage

/**
 * Created by haxpor on 6/29/17.
 */
class Score(gsm: GameStateManager, score: Int): GameState(gsm) {

    private var image: TextImage = TextImage(score.toString(), Game.V_WIDTH/2, Game.V_HEIGHT/2)

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.setState(Mainmenu(gsm))
        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render() {
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.projectionMatrix = hudCam.combined
        sb.begin()
        image.render(sb)
        sb.end()
    }

    override fun resize_user(width: Int, height: Int) {

    }

    override fun dispose() {

    }
}