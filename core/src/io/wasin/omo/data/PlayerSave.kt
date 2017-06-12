package io.wasin.omo.data

/**
 * Created by haxpor on 6/1/17.
 */

/**
 * Warning, don't change field name for each data class as it will affect resulting written
 * JSON file at the end.
 *
 * Thus this will have major effect in production environment
 */

data class LevelResult(var clear: Boolean = false, var collectedCrystal: Int = 0)
data class PlayerSave(var levelResults: Array<LevelResult> = emptyArray())