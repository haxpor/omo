package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.ui.TextImage
import io.wasin.omo.ui.TransitionState

/**
 * Created by haxpor on 6/29/17.
 */
class Score(gsm: GameStateManager, score: Int): GameState(gsm) {

    private var scoreText: TextImage = TextImage("score", Game.V_WIDTH/2, Game.V_HEIGHT/2 + 90)
    private var image: TextImage = TextImage(score.toString(), Game.V_WIDTH/2, Game.V_HEIGHT/2)

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.setState(TransitionState(gsm, this, Mainmenu(gsm), TransitionState.Type.EXPAND))
        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.projectionMatrix = hudCam.combined
        hudViewport.apply(true)

        sb.begin()
        scoreText.render(sb)
        image.render(sb)
        sb.end()
    }

    override fun resize_user(width: Int, height: Int) {

    }

    override fun dispose() {

    }
}