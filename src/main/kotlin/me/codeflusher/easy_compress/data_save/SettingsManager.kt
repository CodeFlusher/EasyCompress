package me.codeflusher.easy_compress.data_save

import com.google.gson.Gson
import me.codeflusher.easy_compress.util.Logger
import me.codeflusher.easy_compress.util.Utils
import java.io.File
import java.nio.file.Path
import kotlin.io.path.*

class SettingsManager {

    private fun getDefaultSettings(): Settings {
        return Settings("", debug = false, hardwareAcceleration = false)
    }

    fun readSettings(): Settings {
        val file = Utils.getLocalFile("settings.json")
        if (Logger.isInitialized()) {
            Logger.message("Settings manager", "Do file exist: ", file.exists())
            Logger.message("Settings Manager", "Absolute Path:", file.absolutePathString())
        }

        if (!file.exists()) {
            writeSettings(getDefaultSettings())
            return getDefaultSettings()
        }

        try {
            return parseSettings(file.readText())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return getDefaultSettings()
    }

    private fun writeSettings(settings: Settings) {
        val file = Utils.getLocalFile("settings.json")
        try {
            file.deleteIfExists()
            file.createFile()
            file.writeText(serializeObject(settings))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveFFMpeg(path: String) {
        val settings = readSettings()
        writeSettings(Settings(path, settings.debug, settings.hardwareAcceleration))
    }

    fun saveHWAcceleration(useAcceleration: Boolean) {
        val settings = readSettings()
        writeSettings(Settings(settings.ffmpegPath, settings.debug, useAcceleration))
    }

    fun savePreset(preset: Preset, name: String) {
        val file = Utils.getLocalFile("user_presets/$name.json")
        file.createParentDirectories()
        file.deleteIfExists()
        file.createFile()
        file.writeText(serializeObject(preset))
    }

    private fun parseSettings(data: String): Settings {
        return Gson().fromJson(data, Settings::class.java)
    }

    private fun parsePreset(data: String): Preset {
        return Gson().fromJson(data, Preset::class.java)
    }

    private fun serializeObject(o: Any): String {
        val gson = Gson()
        return gson.toJson(o)
    }

    private fun loadPreset(name: String): Preset {
        val file = Path.of(Utils.getLocalFile("user_presets").absolutePathString(), "$name.json")
        try {
            return parsePreset(file.readText())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return StandardPresets().fullHd60fpsLIBX265
    }

    @OptIn(ExperimentalPathApi::class)
    fun loadPresetsFromFolder() {
        try {
            Utils.getLocalFile("user_presets" + File.separator).walk().forEach {
                PresetRegistry.register(loadPreset(it.name.dropLast(5)))
            }
        } catch (e: Exception) {
            Logger.exception("Preset Initialization", e)
        }
    }
}