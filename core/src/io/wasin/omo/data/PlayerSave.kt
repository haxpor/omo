package io.wasin.omo.data

import io.wasin.omo.states.Play

/**
 * Created by haxpor on 6/1/17.
 */

/**
 * Warning, don't change field name for each data class as it will affect resulting written
 * JSON file at the end.
 *
 */

data class PlayerSave(var results: HashMap<Play.Difficulty, Int> = hashMapOf())