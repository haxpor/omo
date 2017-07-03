package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import io.wasin.omo.Game
import io.wasin.omo.handlers.*
import io.wasin.omo.ui.Graphic
import io.wasin.omo.ui.TextImage
import io.wasin.omo.ui.TransitionState

/**
 * Created by haxpor on 5/30/17.
 */
class Mainmenu(gsm: GameStateManager): GameState(gsm) {

    private var touchPos: Vector3 = Vector3.Zero
    private var title: Graphic = Graphic(Game.res.getAtlas("pack")!!.findRegion("omo"), Game.V_WIDTH/2, Game.V_HEIGHT/2 + 100)
    private var play: TextImage = TextImage("play", Game.V_WIDTH/2, Game.V_HEIGHT/2-50)
    private var highScore: TextImage = TextImage("scores", Game.V_WIDTH/2, Game.V_HEIGHT/2-130)

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.x = Gdx.input.x.toFloat()
            touchPos.y = Gdx.input.y.toFloat()
            hudCam.unproject(touchPos, hudViewport.screenX.toFloat(), hudViewport.screenY.toFloat(),
                    hudViewport.screenWidth.toFloat(), hudViewport.screenHeight.toFloat())

            if (play.contains(touchPos.x, touchPos.y)) {
                Game.res.getSound("tap")?.let {
                    val sfx = it
                    sfx.play().let { sfx.setVolume(it, 0.7f)}
                }

                gsm.setState(TransitionState(gsm, this, Difficulty(gsm), TransitionState.Type.BLACK_FADE))
            }
            else if (highScore.contains(touchPos.x, touchPos.y)) {
                Game.res.getSound("tap")?.let {
                    val sfx = it
                    sfx.play().let { sfx.setVolume(it, 0.7f)}
                }
                gsm.setState(TransitionState(gsm, this, HighScore(gsm), TransitionState.Type.BLACK_FADE))
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
        title.render(sb)
        play.render(sb)
        highScore.render(sb)
        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {

    }
}