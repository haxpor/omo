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
		const val V_WIDTH = 320f
		const val V_HEIGHT = 240f
		const val SCALE = 2

		var res: Content = Content()
			private set
	}

	override fun create() {

		Gdx.input.inputProcessor = BBInputProcessor()

		sb = SpriteBatch()

		gsm = GameStateManager(this)

		// create player's savefile manager with pre-set of savefile's path
		playerSaveFileManager = PlayerSaveFileManager(Settings.PLAYER_SAVEFILE_RELATIVE_PATH)

		/*res.loadTexture("images/bunny.png", "bunny")
		res.loadTexture("images/crystal.png", "crystal")
		res.loadTexture("images/hud.png", "hud")
		res.loadTexture("images/bgs.png", "bgs")
		res.loadTexture("images/menu.png", "menu")
		res.loadTexture("images/spikes.png", "spikes")
		res.loadTexture("images/misc.png", "misc")

		res.loadMusic("music/bbsong.mp3", "bbsong")
		res.loadSound("sfx/changeblock.wav", "changeblock")
		res.loadSound("sfx/crystal.wav", "crystal")
		res.loadSound("sfx/hit.wav", "hit")
		res.loadSound("sfx/jump.wav", "jump")
		res.loadSound("sfx/levelselect.wav", "levelselect")*/

		// set to play background music endlessly now
		/*val bgMusic = res.getMusic("bbsong")!!
		bgMusic.play()
		bgMusic.isLooping = true*/

		// set to begin with Play state
		gsm.pushState(GameStateManager.MAINMENU)
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
