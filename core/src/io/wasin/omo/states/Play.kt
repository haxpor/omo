package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import io.wasin.omo.Game
import io.wasin.omo.handlers.GameStateManager
import io.wasin.omo.ui.*

/**
 * Created by haxpor on 6/13/17.
 */
class Play(gsm: GameStateManager, difficulty: Difficulty): GameState(gsm) {

    companion object {
        const val SHOW_TIMER: Float = 4.0f
        const val LEVEL_TIMER: Float = 5.0f
        const val RIGHT_MULTIPLY: Int = 10
        const val WRONG_ABS_DEDUCT: Float = 5f
        const val TIME_WAIT_UNTIL_GOTO_SCORE_STATE: Int = 1
    }

    object MultiTouch {
        const val MAX_FINGERS: Int = 2
    }

    enum class Difficulty {
        EASY,
        NORMAL,
        HARD,
        INSANE
    }

    private var scoreTextImage: ScoreTextImage
    private var scoreTimer: Float = LEVEL_TIMER
    private var isPlayedTimeupSfx: Boolean = false

    private var level: Int = 1
    private var maxLevel: Int = 3
    private var difficulty: Difficulty = difficulty

    private var tiles: ArrayList<ArrayList<SizingTile>> = arrayListOf()
    private var tileSize: Float = -1f
    private var boardOffset: Float = -1f
    private var boardHeight: Float = -1f
    private var mouse: Vector3 = Vector3.Zero

    private var selected: Array<Tile>
    private var finished: Array<Tile>
    private var glows: Array<GlowTile>

    private var showing: Boolean = true
    private var showTimer: Float = 0.0f
    private var showPreviousOneSecAwayTime: Float = 0.0f

    private var prevPosTouched: kotlin.Array<Pair<Int, Int>> = kotlin.Array(MultiTouch.MAX_FINGERS, { _ -> Pair(-1, -1)})

    private var light: TextureRegion
    private var dark: TextureRegion

    // handle to wait after game is done then go to score state
    private var isNeedToWaitBeforeTreatAsDone: Boolean = false
    private var doneTimer: Float = 0.0f

    // back button
    private var backButton: TextImage

    init {

        scoreTextImage = ScoreTextImage(Game.V_WIDTH/2, Game.V_HEIGHT - 70)
        backButton = TextImage("back", Game.V_WIDTH/2, 70f)

        val texture = Game.res.getAtlas("pack")!!
        light = texture.findRegion("light")
        dark = texture.findRegion("dark")

        // initially create empty array for selected, and finished first
        selected = Array()
        finished = Array()
        glows = Array()

        val args = getArgs(difficulty)

        // create board
        createBoard(args[0], args[1])
        createFinished(args[2])
    }

    override fun handleInput() {
        for (i in 0..MultiTouch.MAX_FINGERS-1) {

            // back button
            if (Gdx.input.isTouched(i)) {
                mouse.x = Gdx.input.getX(i).toFloat()
                mouse.y = Gdx.input.getY(i).toFloat()
                hudCam.unproject(mouse, hudViewport.screenX.toFloat(), hudViewport.screenY.toFloat(),
                        hudViewport.screenWidth.toFloat(), hudViewport.screenHeight.toFloat())

                if (backButton.contains(mouse.x, mouse.y)) {
                    Game.res.getSound("tap")?.let {
                        val sfx = it
                        sfx.play().let { sfx.setVolume(it, 0.7f)}
                    }
                    gsm.setState(TransitionState(gsm, this, io.wasin.omo.states.Difficulty(gsm), TransitionState.Type.EXPAND))
                }
            }

            if (!showing && !isNeedToWaitBeforeTreatAsDone && Gdx.input.isTouched(i)) {
                mouse.x = Gdx.input.getX(i).toFloat()
                mouse.y = Gdx.input.getY(i).toFloat()
                hudCam.unproject(mouse, hudViewport.screenX.toFloat(), hudViewport.screenY.toFloat(),
                        hudViewport.screenWidth.toFloat(), hudViewport.screenHeight.toFloat())

                if (mouse.y >= boardOffset && mouse.y <= boardOffset + boardHeight) {

                    val row: Int = ((mouse.y - boardOffset) / tileSize).toInt()
                    val col: Int = (mouse.x / tileSize).toInt()

                    if (row >= 0 && row < tiles.count() &&
                            col >= 0 && col < tiles[row].count() &&
                            (row != prevPosTouched[i].first ||
                            col != prevPosTouched[i].second)) {

                        val tile = tiles[row][col]
                        tile.selected = !tile.selected

                        // add to selected array if it's highlighted
                        if (tile.selected) {
                            selected.add(tile)

                            // check if this selected tile is wrong
                            if (!finished.contains(tile)) {
                                Game.res.getSound("wrong")?.let {
                                    val sfx = it
                                    sfx.play().let { sfx.setVolume(it, 0.45f) }
                                }
                                tile.wrong = true

                                // apply penalty
                                scoreTextImage.addScore(-WRONG_ABS_DEDUCT)
                            }
                            else {
                                Game.res.getSound("correct")?.let {
                                    val sfx = it
                                    sfx.play().let { sfx.setVolume(it, 0.45f) }
                                }
                                tile.wrong = false
                            }

                            // add glow (grow type) effect
                            glows.add(GlowTile(tiles[row][col].x, tiles[row][col].y, tileSize, tileSize))
                        }
                        // removed from selected array if it's deselected
                        else {
                            Game.res.getSound("cancel")?.let {
                                val sfx = it
                                sfx.play().let { sfx.setVolume(it, 0.45f) }
                            }

                            selected.removeValue(tile, true)

                            // add glow (shrink type) effect
                            val glow = GlowTile(tiles[row][col].x, tiles[row][col].y, tileSize, tileSize, GlowTile.Type.SHRINK)
                            if (tile.wrong) {
                                glow.wrong = true
                            }
                            glows.add(glow)
                        }

                        // if finished then restart the board again
                        if (checkIsFinished()) {
                            Game.res.getSound("win")?.let {
                                val sfx = it
                                sfx.play().let { sfx.setVolume(it, 0.6f) }
                            }

                            level++

                            val addScore = scoreTimer * RIGHT_MULTIPLY
                            scoreTextImage.addScore(addScore)

                            // check if it's done
                            if (level > maxLevel) {
                                isNeedToWaitBeforeTreatAsDone = true
                                doneTimer = 0f

                                // check to save as high score
                                // note: use destScore as a comparison as score is still updating
                                game.saveFileManager.getScore(difficulty)?.let {
                                    if (scoreTextImage.destScore > it) {
                                        // save to file only if score is beaten
                                        // need to round first before saving score
                                        game.saveFileManager.updateScore(difficulty, scoreTextImage.destScore.toInt(), true)
                                    }
                                }
                            }
                            else {
                                val args = getArgs(difficulty)
                                createBoard(args[0], args[1])
                                createFinished(args[2])

                                // reset scoreTextImage timer
                                scoreTimer = LEVEL_TIMER
                            }
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
        checkToShowObjective(dt)
        checkToCountdownScoreTimer(dt)

        // update glows
        for (i in glows.count()-1 downTo 0) {
            glows[i].update(dt)

            if (glows[i].shouldBeRemoved) {
                glows.removeIndex(i)
            }
        }

        scoreTextImage.update(dt)

        // tiles
        for (row in 0..tiles.count()-1) {
            for (col in 0..tiles[row].count()-1) {
                tiles[row][col].update(dt)
            }
        }

        if (isNeedToWaitBeforeTreatAsDone) {
            doneTimer += dt

            if (doneTimer >= TIME_WAIT_UNTIL_GOTO_SCORE_STATE) {
                // switch to another state thus no need to check whether code enter inside this statement again
                done()
            }
        }
    }

    override fun render(sb: SpriteBatch) {
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.projectionMatrix = hudCam.combined
        hudViewport.apply(true)
        sb.begin()

        // render score
        scoreTextImage.render(sb)

        // render level indicator
        val allLevelsWidth = (2*maxLevel - 1)*10
        for (i in 0..maxLevel-1) {
            if (i+1 <= level) {
                sb.draw(light, Game.V_WIDTH/2 - allLevelsWidth/2 + 20*i, Game.V_HEIGHT - 125, 10f, 10f)
            }
            else {
                sb.draw(dark, Game.V_WIDTH/2 - allLevelsWidth/2 + 20*i, Game.V_HEIGHT - 125, 10f, 10f)
            }
        }

        // render tiles
        for (row in 0..tiles.count()-1) {
            for (col in 0..tiles[row].count()-1) {
                tiles[row][col].render(sb)
            }
        }

        // render back button
        backButton.render(sb)

        // render glow
        for (g in glows) {
            g.render(sb)
        }

        sb.end()
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {

    }

    private fun checkToCountdownScoreTimer(dt: Float) {
        if (!showing) {
            scoreTimer -= dt

            // play timeup sfx
            if (scoreTimer <= 0.0f && !isPlayedTimeupSfx) {
                Game.res.getSound("timeup")?.let {
                    val sfx = it
                    sfx.play().let { sfx.setVolume(it, 0.5f)}
                }

                isPlayedTimeupSfx = true
            }
        }
    }

    private fun checkToShowObjective(dt: Float) {
        if (showing) {
            showTimer += dt

            if (showTimer > 3f) {
                if (showTimer % 0.15f < 0.07f) {
                    for (f in finished) {
                        f.selected = true
                    }
                    Game.res.getSound("onesec")?.let {
                        val sfx = it
                        sfx.play().let { sfx.setVolume(it, 0.65f)}
                    }
                }
                else {
                    for (f in finished) {
                        f.selected = false
                    }
                }
            }

            if (showTimer > SHOW_TIMER) {
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
                        SizingTile(
                                col * tileSize + tileSize/2,
                                row * tileSize + boardOffset + tileSize/2,
                                tileSize,
                                tileSize
                        )
                )
                tiles[row][col].timer = -((numRow - row) * 0.02f) - col * 0.05f
            }
        }
    }

    private fun createFinished(numTilesToLight: Int) {

        selected.clear()
        finished.clear()

        showing = true
        showTimer = 0f
        isPlayedTimeupSfx = false

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
        gsm.setState(TransitionState(gsm, this, Score(gsm, scoreTextImage.destScore.toInt()), TransitionState.Type.EXPAND))
    }
}