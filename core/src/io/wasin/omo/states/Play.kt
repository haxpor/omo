package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector3
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
    private var boardHeight: Float
    private var mouse: Vector3 = Vector3.Zero

    object GRID {
        const val NUM_ROWS: Int = 6
        const val NUM_COLS: Int = 6
    }

    object MultiTouch {
        const val MAX_FINGERS: Int = 2
    }

    init {

        // create tiles based on fixed setting of GRID
        tileSize = Game.V_WIDTH / ( if (GRID.NUM_COLS >= GRID.NUM_ROWS) GRID.NUM_COLS else GRID.NUM_ROWS )
        boardHeight = tileSize * GRID.NUM_ROWS
        boardOffset = (Game.V_HEIGHT - boardHeight)/2

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
                tiles[row][col].setTimer(-((GRID.NUM_ROWS - row) * 0.02f) - col * 0.05f)
            }
        }
    }

    override fun handleInput() {
        for (i in 0..MultiTouch.MAX_FINGERS-1) {
            if (Gdx.input.isTouched(i)) {
                mouse.x = Gdx.input.getX(i).toFloat()
                mouse.y = Gdx.input.getY(i).toFloat()
                cam.unproject(mouse)

                if (mouse.y >= boardOffset && mouse.y <= boardOffset + boardHeight) {

                    val row: Int = ((mouse.y - boardOffset) / tileSize).toInt()
                    val col: Int = (mouse.x / tileSize).toInt()

                    if (row >= 0 && row < tiles.count() &&
                            col >= 0 && col < tiles[row].count()) {
                        tiles[row][col].selected = true
                    }
                }
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()

        // tiles
        for (row in 0..tiles.count()-1) {
            for (col in 0..tiles[row].count()-1) {
                tiles[row][col].update(dt)
            }
        }
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