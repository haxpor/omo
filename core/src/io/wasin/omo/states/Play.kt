package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.ui.Tile

/**
 * Created by haxpor on 6/13/17.
 */
class Play(gsm: GameStateManager): GameState(gsm) {

    private var tiles: ArrayList<ArrayList<Tile>> = arrayListOf()
    private var tileSize: Float = -1f
    private var boardOffset: Float = -1f
    private var boardHeight: Float = -1f
    private var mouse: Vector3 = Vector3.Zero
    lateinit private var finished: Array<Tile>

    private var showing: Boolean = true
    private var showTimer: Float = 0.0f

    object MultiTouch {
        const val MAX_FINGERS: Int = 2
    }

    init {

        // create board
        createBoard(3, 3)
        createFinished()
    }

    override fun handleInput() {
        for (i in 0..MultiTouch.MAX_FINGERS-1) {
            if (!showing && Gdx.input.isTouched(i)) {
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

        if (showing) {
            showTimer += dt
            if (showTimer > 5f) {
                showing = false

                // go through everything and unselected each tile
                for (selectedTile in finished) {
                    selectedTile.selected = false
                }
            }
        }

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

    private fun createBoard(numRow: Int, numCol: Int) {
        // create tiles based on fixed setting of GRID
        tileSize = Game.V_WIDTH / ( if (numCol >= numRow) numCol else numRow )
        boardHeight = tileSize * numRow
        boardOffset = (Game.V_HEIGHT - boardHeight)/2

        for (row in 0..numRow-1) {
            tiles.add(row, arrayListOf())

            for (col in 0..numCol-1) {
                tiles[row].add(col,
                        Tile(
                                col * tileSize + tileSize/2,
                                row * tileSize + boardOffset + tileSize/2,
                                tileSize,
                                tileSize
                        )
                )
                tiles[row][col].setTimer(-((numRow - row) * 0.02f) - col * 0.05f)
            }
        }
    }

    private fun createFinished() {
        finished = Array()

        showing = true
        showTimer = 0f

        var numTilesToLight = 4
        for (i in 0..numTilesToLight-1) {
            var row = 0
            var col = 0
            do {
                row = MathUtils.random(tiles.count() - 1)
                col = MathUtils.random(tiles[row].count() - 1)
            } while(finished.contains(tiles[row][col], true))
            finished.add(tiles[row][col])
            tiles[row][col].selected = true
        }
    }
}