package io.wasin.omo.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.wasin.omo.Game

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = (Game.V_WIDTH).toInt()
        config.height = (Game.V_HEIGHT).toInt()
        config.title = Game.TITLE
        LwjglApplication(Game(), config)
    }
}
