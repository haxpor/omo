package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.ui.Tile

/**
 * Created by haxpor on 6/13/17.
 */
class Play(gsm: GameStateManager): GameState(gsm) {

    private var tiles: ArrayList<ArrayList<Tile>> = arrayListOf()
    private var tileSize: Float
    private var boardOffset: Float

    object GRID {
        const val NUM_ROWS: Int = 4
        const val NUM_COLS: Int = 4
    }

    init {

        // create tiles based on fixed setting of GRID
        tileSize = Game.V_WIDTH / ( if (GRID.NUM_COLS >= GRID.NUM_ROWS) GRID.NUM_COLS else GRID.NUM_ROWS )
        boardOffset = (Game.V_HEIGHT - tileSize * GRID.NUM_ROWS)/2

        for (row in 0..GRID.NUM_ROWS-1) {
            tiles.add(row, arrayListOf())

            for (col in 0..GRID.NUM_COLS-1) {
                tiles[row].add(col,
                        Tile(
                            col * tileSize + tileSize/2,
                            row * tileSize + boardOffset + tileSize/2,
                            tileSize,
                            tileSize
                        )
                )
            }
        }
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {

    }

    override fun render() {

        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.projectionMatrix = cam.combined
        sb.begin()

        for (row in 0..tiles.count()-1) {
            for (col in 0..tiles[row].count()-1) {
                tiles[row][col].render(sb)
            }
        }

        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {

    }
}