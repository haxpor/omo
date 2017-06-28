package io.wasin.omo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.wasin.omo.handlers.*
import io.wasin.omo.states.Mainmenu
import io.wasin.omo.states.Play

class Game : ApplicationAdapter() {

	lateinit var sb: SpriteBatch
		private set
	lateinit var gsm: GameStateManager
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

		res.loadAtlas("pack.pack", "pack")

		// begin with Easy difficulty of Play state
		gsm.pushState(Mainmenu(gsm))
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
