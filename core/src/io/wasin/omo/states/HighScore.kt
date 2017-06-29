package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.ui.TextImage
import io.wasin.omo.ui.TransitionState

/**
 * Created by haxpor on 6/30/17.
 */
class HighScore(gsm: GameStateManager): GameState(gsm) {

    private var touchPos: Vector3 = Vector3.Zero
    private var diffTitles: Array<TextImage>
    private var backButton: TextImage

    init {
        val texts = arrayOf("easy", "normal", "hard", "insane")
        diffTitles = Array(texts.size, { i -> TextImage(texts[i], Game.V_WIDTH/2, Game.V_HEIGHT/2 + 100 - 70*i) })
        backButton = TextImage("back", Game.V_WIDTH/2, 70f)
    }

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.x = Gdx.input.x.toFloat()
            touchPos.y = Gdx.input.y.toFloat()
            cam.unproject(touchPos, hudViewport.screenX.toFloat(), hudViewport.screenY.toFloat(),
                    hudViewport.screenWidth.toFloat(), hudViewport.screenHeight.toFloat())

            // all difficulty buttons
            for (i in 0..diffTitles.size-1) {
                if (diffTitles[i].contains(touchPos.x, touchPos.y)) {
                    gsm.setState(TransitionState(gsm, this, Play(gsm, Play.Difficulty.values()[i]), TransitionState.Type.EXPAND))
                }
            }

            if (backButton.contains(touchPos.x, touchPos.y)) {
                gsm.setState(TransitionState(gsm, this, Mainmenu(gsm), TransitionState.Type.EXPAND))
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
        hudViewport.apply(true)

        sb.begin()
        for (b in diffTitles) {
            b.render(sb)
        }
        backButton.render(sb)
        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {

    }
}