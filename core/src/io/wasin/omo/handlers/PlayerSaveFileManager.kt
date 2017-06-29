package io.wasin.omo.handlers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.SerializationException
import io.wasin.omo.data.PlayerSave
import io.wasin.omo.data.PlayerSaveCache
import io.wasin.omo.interfaces.ISaveFile
import io.wasin.omo.states.Play

/**
 * Created by haxpor on 5/31/17.
 */
class PlayerSaveFileManager(filePath: String): ISaveFile {
    var saveFilePath: String = filePath
        private set
    var cache: PlayerSaveCache
        private set
    private var json: Json

    init {
        json = Json()
        json.setOutputType(JsonWriter.OutputType.json)
        json.setUsePrototypes(false)

        cache = PlayerSaveCache()
    }

    /**
     * @throws GameRuntimeException if there's any error happening
     */
    @Throws(GameRuntimeException::class)
    fun readSaveFile(): PlayerSave? {
        return readSaveFile(saveFilePath)
    }

    /**
     * @throws GameRuntimeException if there's any error happening
     * @throws SerializationException if file's content cannot be parsed back to Json object, file might be corrupted
     */
    @Throws(GameRuntimeException::class, SerializationException::class)
    override fun readSaveFile(filePath: String): PlayerSave? {
        val handle = Gdx.files.local(filePath)
        if(!handle.exists()) {
            throw GameRuntimeException("Save file doesn't exist at $filePath", GameRuntimeException.SAVE_FILE_NOT_FOUND)
        }

        val jsonString = handle.readString()
        if (jsonString.length == 0) {
            // invalidate internal cached then return null
            cache.data = null
            throw GameRuntimeException("Save file's content is empty. Save file might be corrupted.", GameRuntimeException.SAVE_FILE_EMPTY_CONTENT)
        }

        Gdx.app.log("PlayerSaveFileManager", "save file content")
        Gdx.app.log("PlayerSaveFileManager", jsonString)
        val playerSave = json.fromJson(PlayerSave::class.java, jsonString)

        // syn to internal cache
        cache.data = playerSave

        return playerSave
    }

    /**
     * @throws GameRuntimeException if there's any error haeppning
     */
    fun writeSaveFile(data: PlayerSave) {
        writeSaveFile(data, saveFilePath)
    }

    /**
     * @throws GameRuntimeException if there's any error happening
     */
    @Throws(GameRuntimeException::class)
    override fun writeSaveFile(data: PlayerSave, filePath: String) {
        val handle = Gdx.files.local(filePath)
        val toWriteString = json.prettyPrint(data)

        Gdx.app.log("PlayerSaveFileManager", "Write content")
        Gdx.app.log("PlayerSaveFileManager", toWriteString)

        try {
            handle.writeString(toWriteString, false)
        }
        catch(e: GdxRuntimeException) {
            throw GameRuntimeException("${e.message}", GameRuntimeException.WRITE_FILE_ERROR)
        }
        finally {
            // sync with internal cache
            cache.data = data.copy()

            Gdx.app.log("PlayerSaveFileManager", "synced with internal cache")
        }
    }

    /**
     * Update score for input difficultyLevel with an option to write updated
     * content to the file immediately.
     * @throws GameRuntimeException if cache's data is null
     */
    @Throws(GameRuntimeException::class)
    fun updateScore(difficultyLevel: Play.Difficulty, score: Int, writeImmediately: Boolean) {

        if (cache.data == null) throw GameRuntimeException("Cache's data should not be null prior to calling this method", GameRuntimeException.NULL_ERROR)

        // update to in memory results
        cache.data!!.results[difficultyLevel.toString()] = score

        println("updateScore: " + cache.data!!.toString())

        if (writeImmediately) {
            // write out cached data
            writeSaveFile(cache.data!!)
        }
    }

    /**
     * Write a fresh save file. This is usually used for writing a new save file for a new user.
     * @throws GameRuntimeException if there's any error regarding to I/O operation
     */
    fun writeFreshSaveFile() {
        // initialize score to 0 for all difficulty
        val tmpHashMap = HashMap<String, Int>(Play.Difficulty.values().size)
        for (dl in Play.Difficulty.values()) {
            tmpHashMap[dl.toString()] = 0
        }

        val data = PlayerSave(tmpHashMap)
        writeSaveFile(data)

        // sync to internal cache
        cache.data = data
    }

    /**
     * Get a score from specified difficulty
     * @return Score for specified difficulty, otherwise return null if there's no such cache data yet
     */
    fun getScore(difficulty: Play.Difficulty): Int? {
        if (cache.data == null) return null

        return cache.data!!.results[difficulty.toString()]
    }
}