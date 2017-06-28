package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.SerializationException
import io.wasin.omo.Game
import io.wasin.omo.handlers.*
import io.wasin.omo.ui.Graphic
import io.wasin.omo.ui.TextImage

/**
 * Created by haxpor on 5/30/17.
 */
class Mainmenu(gsm: GameStateManager): GameState(gsm) {

    private var title: Graphic = Graphic(Game.res.getAtlas("pack")!!.findRegion("omo"), Game.V_WIDTH/2, Game.V_HEIGHT/2 + 100)
    private var play: TextImage = TextImage("play", Game.V_WIDTH/2, Game.V_HEIGHT/2-50)

    override fun handleInput() {
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render() {
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.projectionMatrix = hudCam.combined
        sb.begin()
        title.render(sb)
        play.render(sb)
        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {

    }
}