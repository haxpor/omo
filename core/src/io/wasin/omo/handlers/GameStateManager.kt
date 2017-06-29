package io.wasin.omo.handlers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.wasin.omo.Game
import io.wasin.omo.states.*

import java.util.Stack

/**
 * Created by haxpor on 5/14/17.
 */

class GameStateManager(game: Game){
    var game: Game
        private set
    private var gameStates: Stack<GameState>


    init {
        this.game = game
        this.gameStates = Stack<GameState>()
    }

    fun update(dt: Float) {
        this.gameStates.peek().update(dt)
    }

    fun resize(width: Int, height: Int) {
        this.gameStates.peek().resize(width, height)
    }

    fun render(sb: SpriteBatch) {
        this.gameStates.peek().render(sb)
    }

    fun setState(gameState: GameState) {
        this.gameStates.clear()
        this.gameStates.push(gameState)
    }

    fun pushState(gameState: GameState) {
        this.gameStates.push(gameState)
    }

    fun popState() {
        val g = this.gameStates.pop()
        g.dispose()
    }
}