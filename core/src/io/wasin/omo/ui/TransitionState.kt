package io.wasin.omo.ui

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
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
    private var maxTime: Float = 1f
    private var alpha: Float = 1f

    // expand
    lateinit private var expands: Array<Array<ExpandingTile>>
    private var doneExpanding: Boolean = false
    private var doneContracting: Boolean = false
    private var delayTimerBetweenTile: Float = 0.025f

    init {
        if (type == Type.BLACK_FADE) {
            maxTime = 1f
        }
        else if (type == Type.EXPAND) {
            val size = 80f
            // determine number of tiles (row & col) to fill the screen
            val numRow = MathUtils.ceil(hudViewport.screenHeight / size)
            val numCol = MathUtils.ceil(hudViewport.screenWidth / size)

            expands = Array(numRow, { row -> Array(numCol,
                    {
                        col -> ExpandingTile(
                            col * size + size/2,
                            row * size + size/2,
                            size,
                            size).also {
                                it.timer = (-(numRow - row) - col) * delayTimerBetweenTile
                            }
                    })
            })
        }
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {
        timer += dt

        // duration of transition depends on maxTime
        if (type == Type.BLACK_FADE) {
            if (timer >= maxTime) {
                gsm.setState(next)
            }
        }
        // duration of transition depends on 2 * effectDuration of ExpandingTile
        else if (type == Type.EXPAND) {
            var isAllDoneExpanding = true
            var isAllDoneContracting = true
            expands.forEach {
                it.forEach {
                    it.update(dt)

                    // if at least one is not done expanding yet, then reset flag
                    if (!it.isDoneExpanding && !doneExpanding) {
                        isAllDoneExpanding = false
                    }
                    // otherwise: if at least one is not done contracting yet, then reset flag
                    else if (!it.isDoneContracting && !doneContracting) {
                        isAllDoneContracting = false
                    }
                }
            }

            // if in expanding phase, and need to switch to contracting phase
            if (isAllDoneExpanding && !doneExpanding && !doneContracting) {
                doneExpanding = true
                expands.forEachIndexed { row, array ->
                    array.forEachIndexed { col, tile ->
                        tile.setToContract()
                        tile.timer = (-(expands.size + row) + col) * delayTimerBetweenTile
                    }
                }
            }

            // if in contracting phase, and need to set state
            if (isAllDoneContracting && doneExpanding && !doneContracting) {
                doneContracting = true
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
            // draw dark on the entire screen
            sb.draw(dark, 0f, 0f, hudViewport.screenWidth.toFloat(), hudViewport.screenHeight.toFloat())
            sb.end()

            // set color back to batch
            sb.color = color
        }
        else if (type == Type.EXPAND) {

            if (!doneExpanding) {
                prev.render(sb)
            }
            else {
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