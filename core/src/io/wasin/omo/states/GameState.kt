package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager

/**
 * Created by haxpor on 5/14/17.
 */
abstract class GameState(gsm: GameStateManager) {
    protected var gsm: GameStateManager = gsm

    lateinit protected var camViewport: Viewport
    lateinit protected var hudViewport: Viewport

    // for convenient in reference and use in derived class
    protected val game: Game = gsm.game
    lateinit protected var cam: OrthographicCamera
    lateinit protected var hudCam: OrthographicCamera

    init {
        setupCamera(Game.V_WIDTH, Game.V_HEIGHT)
        setupViewport(cam, hudCam, Game.V_WIDTH, Game.V_HEIGHT)
        // always update viewport
        camViewport.update(Gdx.graphics.width, Gdx.graphics.height)
        hudViewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
    }

    abstract fun handleInput()
    abstract fun update(dt: Float)
    abstract fun render(sb: SpriteBatch)
    abstract fun dispose()
    abstract fun resize_user(width: Int, height: Int)

    fun resize(width: Int, height: Int) {
        camViewport.update(width, height)
        hudViewport.update(width, height, true)

        resize_user(width, height)
    }

    /**
     * Set up cam, and hudCam.
     * If needed to create a different type of camera and viewport, then override and implement it in
     * GameState class.
     */
    open protected fun setupCamera(viewportWidth: Float, viewportHeight: Float) {
        // set up cam
        cam = OrthographicCamera()
        cam.setToOrtho(false, viewportWidth, viewportHeight)

        // set up hud-cam
        hudCam = OrthographicCamera()
        hudCam.setToOrtho(false, viewportWidth, viewportHeight)
    }

    /**
     * Set up camViewport, and hudViewport.
     * If needed to create a different type of viewport, then override and implement it in GameState class.
     */
    open protected fun setupViewport(cam: OrthographicCamera, hudCam: OrthographicCamera, viewportWidth: Float, viewportHeight: Float) {
        camViewport = FitViewport(viewportWidth, viewportHeight, cam)
        hudViewport = FitViewport(viewportWidth, viewportHeight, hudCam)
    }
}