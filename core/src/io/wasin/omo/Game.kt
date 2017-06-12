package io.wasin.omo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.wasin.omo.handlers.*

class Game : ApplicationAdapter() {

	lateinit var sb: SpriteBatch
		private set
	lateinit var gsm: GameStateManager
		private set
	lateinit var playerSaveFileManager: PlayerSaveFileManager
		private set

	companion object {
		const val TITLE = "OMO"
		const val V_WIDTH = 480f
		const val V_HEIGHT = 800f
		const val SCALE = 1

		var res: Content = Content()
			private set
	}

	override fun create() {

		Gdx.input.inputProcessor = BBInputProcessor()

		sb = SpriteBatch()

		gsm = GameStateManager(this)

		// create player's savefile manager with pre-set of savefile's path
		playerSaveFileManager = PlayerSaveFileManager(Settings.PLAYER_SAVEFILE_RELATIVE_PATH)

		res.loadAtlas("pack.atlas", "pack")

		// set to begin with Play state
		gsm.pushState(GameStateManager.PLAY)
	}

	override fun render() {
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.framesPerSecond)
		gsm.update(Gdx.graphics.deltaTime)
		gsm.render()
		BBInput.update()
	}

	override fun dispose() {
	}

	override fun resize(width: Int, height: Int) {
		gsm.resize(width, height)
	}
}
