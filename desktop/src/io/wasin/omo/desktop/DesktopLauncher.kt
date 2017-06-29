package io.wasin.omo.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.wasin.omo.Game

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = (Game.V_WIDTH / 2).toInt()
        config.height = (Game.V_HEIGHT / 2).toInt()
        config.title = Game.TITLE
        config.fullscreen = true
        LwjglApplication(Game(), config)
    }
}
