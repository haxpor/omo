package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.ui.Tile

/**
 * Created by haxpor on 6/13/17.
 */
class Play(gsm: GameStateManager, difficulty: Difficulty): GameState(gsm) {

    object MultiTouch {
        const val MAX_FINGERS: Int = 2
    }

    enum class Difficulty {
        EASY,
        NORMAL,
        HARD,
        INSANE
    }

    private var level: Int = 1
    private var maxLevel: Int = 3
    private var difficulty: Difficulty = difficulty

    private var tiles: ArrayList<ArrayList<Tile>> = arrayListOf()
    private var tileSize: Float = -1f
    private var boardOffset: Float = -1f
    private var boardHeight: Float = -1f
    private var mouse: Vector3 = Vector3.Zero

    private var selected: Array<Tile>
    private var finished: Array<Tile>

    private var showing: Boolean = true
    private var showTimer: Float = 0.0f

    private var prevPosTouched: kotlin.Array<Pair<Int, Int>> = kotlin.Array(MultiTouch.MAX_FINGERS, { _ -> Pair(-1, -1)})

    init {

        // initially create empty array for selected, and finished first
        selected = Array()
        finished = Array()

        val args = getArgs(difficulty)

        // create board
        createBoard(args[0], args[1])
        createFinished(args[2])
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
                            col >= 0 && col < tiles[row].count() &&
                            row != prevPosTouched[i].first ||
                            col != prevPosTouched[i].second) {

                        val tile = tiles[row][col]
                        tile.selected = !tile.selected

                        // add to selected array if it's highlighted
                        if (tile.selected) {
                            selected.add(tile)
                        }
                        // removed from selected array if it's deselected
                        else {
                            selected.removeValue(tile, true)
                        }

                        // if finished then restart the board again
                        if (checkIsFinished()) {
                            level++
                            if (level > maxLevel) {
                                done()
                            }
                            val args = getArgs(difficulty)
                            createBoard(args[0], args[1])
                            createFinished(args[2])
                        }
                    }

                    // save for previous touched position
                    prevPosTouched[i] = Pair(row, col)
                }
            }

            // update previous position if not touch anymore
            if (!Gdx.input.isTouched(i)) {
                prevPosTouched[i] = Pair(-1, -1)
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()

        checkShowing(dt)

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

    private fun checkShowing(dt: Float) {
        if (showing) {
            showTimer += dt

            if (showTimer > 3f) {
                if (showTimer % 0.15f < 0.07f) {
                    for (f in finished) {
                        f.selected = true
                    }
                }
                else {
                    for (f in finished) {
                        f.selected = false
                    }
                }
            }

            if (showTimer > 4f) {
                showing = false

                // go through everything and unselected each tile
                for (selectedTile in finished) {
                    selectedTile.selected = false
                }
            }
        }
    }

    private fun createBoard(numRow: Int, numCol: Int) {
        // create tiles based on fixed setting of GRID
        tileSize = Game.V_WIDTH / ( if (numCol >= numRow) numCol else numRow )
        boardHeight = tileSize * numRow
        boardOffset = (Game.V_HEIGHT - boardHeight)/2

        tiles.clear()

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

    private fun createFinished(numTilesToLight: Int) {

        selected.clear()
        finished.clear()

        showing = true
        showTimer = 0f

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

    private fun checkIsFinished(): Boolean {

        if (finished.count() != selected.count()) return false

        for (f in finished) {
            if (!selected.contains(f, true)) {
                return false
            }
        }

        return true
    }

    private fun getArgs(difficulty: Difficulty): kotlin.Array<Int> {
        // create arguments
        // arguments are as follows
        // => number of rows, number of columns, and num tiles to light
        val args = kotlin.Array(3, { _ -> 0 })

        if (difficulty == Difficulty.EASY) {
            args[0] = 3
            args[1] = 3
            if (level in 1..3) {
                args[2] = 3
            }
            else if (level in 4..5){
                args[2] = 4
            }
            maxLevel = 5
        }
        else if (difficulty == Difficulty.NORMAL) {
            args[0] = 4
            args[1] = 4
            if (level in 1..2) {
                args[2] = 4
            }
            else if (level in 3..4) {
                args[2] = 5
            }
            else if (level in 5..6) {
                args[2] = 6
            }
            maxLevel = 6
        }
        else if (difficulty == Difficulty.HARD) {
            args[0] = 5
            args[1] = 5
            if (level in 1..2) {
                args[2] = 6
            }
            else if (level in 3..4) {
                args[2] = 7
            }
            else if (level in 5..6) {
                args[2] = 8
            }
            else if (level in 7..8) {
                args[2] = 9
            }
            maxLevel = 8
        }
        else if (difficulty == Difficulty.INSANE) {
            args[0] = 6
            args[1] = 6
            if (level in 1..2) {
                args[2] = 8
            }
            else if (level in 3..4) {
                args[2] = 9
            }
            else if (level in 5..6) {
                args[2] = 10
            }
            else if (level in 7..8) {
                args[2] = 11
            }
            else if (level in 9..10) {
                args[2] = 12
            }
            maxLevel = 10
        }

        return args
    }

    private fun done() {

    }
}