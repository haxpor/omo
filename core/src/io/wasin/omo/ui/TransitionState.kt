package io.wasin.omo.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.states.GameState

/**
 * Created by haxpor on 6/29/17.
 */
class TransitionState(gsm: GameStateManager, prev: GameState, next: GameState, type: Type): GameState(gsm) {

    enum class Type {
        BLACK_FADE
    }

    private var prev: GameState = prev
    private var next: GameState = next
    private var type: Type = type

    // black fade
    private var dark: TextureRegion = Game.res.getAtlas("pack")!!.findRegion("dark")
    private var timer: Float = 0f
    var maxTime: Float = 1f
    private var alpha: Float = 1f

    init {
        if (type == Type.BLACK_FADE) {
            maxTime = 1f
        }
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {
        timer += dt

        if (type == Type.BLACK_FADE) {
            if (timer >= maxTime) {
                gsm.setState(next)
            }
        }
    }

    override fun render(sb: SpriteBatch) {

        if (type == Type.BLACK_FADE) {
            if (timer < maxTime/2f) {
                alpha = timer / (maxTime / 2f)
                prev.render(sb)
            }
            else {
                alpha = 1f - (timer/(maxTime/2f))
                next.render(sb)
            }

            // get current color from batch
            val color = sb.color

            sb.setColor(0f, 0f, 0f, alpha)
            sb.projectionMatrix = hudCam.combined
            sb.begin()
            sb.draw(dark, 0f, 0f, Game.V_WIDTH, Game.V_HEIGHT)
            sb.end()

            // set color back to batch
            sb.color = color
        }
    }

    override fun resize_user(width: Int, height: Int) {

    }

    override fun dispose() {

    }
}