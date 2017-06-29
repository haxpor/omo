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
        BLACK_FADE,
        EXPAND
    }

    private var prev: GameState = prev
    private var next: GameState = next
    private var type: Type = type

    private var dark: TextureRegion = Game.res.getAtlas("pack")!!.findRegion("dark")

    // black fade
    private var timer: Float = 0f
    var maxTime: Float = 1f
    private var alpha: Float = 1f

    // expand
    lateinit private var expands: Array<Array<ExpandingTile>>
    private var doneExpanding: Boolean = false
    private var doneContracting: Boolean = false

    init {
        if (type == Type.BLACK_FADE) {
            maxTime = 1f
        }
        else if (type == Type.EXPAND) {
            val size = 80f
            expands = Array(10, { row -> Array(6, {col -> ExpandingTile(
                    col * size + size/2,
                    row * size + size/2,
                    size,
                    size)}) })
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
        else if (type == Type.EXPAND) {
            var isNeedToSetDoneExpanding = false
            expands.forEach {
                it.forEach {
                    it.update(dt)

                    // check if tile expands to final state
                    // thus we need to start contract
                    if (it.isDoneExpanding && !isNeedToSetDoneExpanding) {
                        isNeedToSetDoneExpanding = true
                    }
                }
            }

            // if we need to set doneExpanding and it never been set before
            if (isNeedToSetDoneExpanding && !doneExpanding) {
                doneExpanding = true
                expands.forEach {
                    it.forEach {
                        it.setToContract()
                    }
                }
            }

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
        else if (type == Type.EXPAND) {

            if (timer < maxTime/2f) {
                alpha = timer / (maxTime / 2f)
                prev.render(sb)
            }
            else {
                alpha = 1f - (timer/(maxTime/2f))
                next.render(sb)
            }

            sb.projectionMatrix = hudCam.combined
            sb.begin()
            expands.forEach { it.forEach { it.render(sb) } }
            sb.end()
        }
    }

    override fun resize_user(width: Int, height: Int) {

    }

    override fun dispose() {

    }
}