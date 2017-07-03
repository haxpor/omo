package io.wasin.omo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.SerializationException
import io.wasin.omo.handlers.*
import io.wasin.omo.states.Mainmenu
import io.wasin.omo.states.Play

class Game : ApplicationAdapter() {

	lateinit var sb: SpriteBatch
		private set
	lateinit var gsm: GameStateManager
		private set
    lateinit var saveFileManager: PlayerSaveFileManager
        private set

	companion object {
		const val TITLE = "OMO"
		const val V_WIDTH = 480f
		const val V_HEIGHT = 800f

		var res: Content = Content()
			private set
	}

	override fun create() {

		Gdx.input.inputProcessor = BBInputProcessor()

		sb = SpriteBatch()
		gsm = GameStateManager(this)
        // create player's savefile manager with pre-set of savefile's path
        saveFileManager = PlayerSaveFileManager(Settings.PLAYER_SAVEFILE_RELATIVE_PATH)

        // load resource
		res.loadAtlas("pack.pack", "pack")
		res.loadMusic("sound/bg.wav", "bg")

        // read player's savefile
        // this will read it into cache, thus it will be maintained and used throughout the life cycle of the game
        try {
            Gdx.app.log("Mainmenu", "read save file")
            saveFileManager.readSaveFile()
        }
        catch(e: GameRuntimeException) {
            if (e.code == GameRuntimeException.SAVE_FILE_NOT_FOUND ||
                    e.code == GameRuntimeException.SAVE_FILE_EMPTY_CONTENT) {

                // write a new fresh save file to resolve the issue
                Gdx.app.log("Game", "write a fresh save file")
                saveFileManager.writeFreshSaveFile()
            }
        }
        catch(e: SerializationException) {
            Gdx.app.log("Game", "save file is corrupted, rewrite a fresh one : ${e.message}")

            saveFileManager.writeFreshSaveFile()
        }

        // set to play bg music endlessly now
        res.getMusic("bg")?.let {
            it.play()
            it.volume = 0.2f
            it.isLooping = true
        }

		// begin with Easy difficulty of Play state
		gsm.pushState(Mainmenu(gsm))
	}

	override fun render() {
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.framesPerSecond)
		gsm.update(Gdx.graphics.deltaTime)
		gsm.render(sb)
		BBInput.update()
	}

	override fun dispose() {
	}

	override fun resize(width: Int, height: Int) {
		gsm.resize(width, height)
	}
}
