package io.wasin.omo.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.SerializationException
import io.wasin.omo.handlers.*

/**
 * Created by haxpor on 5/30/17.
 */
class Mainmenu(gsm: GameStateManager): GameState(gsm) {

    init {

        // read player's savefile
        // this will read it into cache, thus it will be maintained and used throughout the life
        // cycle of the game
        try {
            Gdx.app.log("Mainmenu", "read save file")
            game.playerSaveFileManager.readSaveFile()
        }
        catch(e: GameRuntimeException) {
            if (e.code == GameRuntimeException.SAVE_FILE_NOT_FOUND ||
                    e.code == GameRuntimeException.SAVE_FILE_EMPTY_CONTENT) {
                // write a new fresh save file to resolve the issue
                Gdx.app.log("Mainmenu", "write a fresh save file")
                game.playerSaveFileManager.writeFreshSaveFile(Settings.TOTAL_LEVELS)
            }
        }
        catch(e: SerializationException) {
            Gdx.app.log("Mainmenu", "save file is corrupted, rewrite a fresh one : ${e.message}")

            game.playerSaveFileManager.writeFreshSaveFile(Settings.TOTAL_LEVELS)
        }
    }

    override fun handleInput() {
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render() {
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun dispose() {

    }

    override fun resize_user(width: Int, height: Int) {

    }
}