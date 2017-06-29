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

/** you should not deal with class directly
 * String type for key is actually String value from enum Play.Difficulty.
 * It will allow underlying system to be able to serialize/deserialize data from JSON file without problem.
 *
 * Yes, libgdx can serialize HashMap.
 * It saves enum as key to JSON file as String, when it goes back from JSON, we need key to be String too not enum.
 *
 */
data class PlayerSave(var results: HashMap<String, Int> = hashMapOf())