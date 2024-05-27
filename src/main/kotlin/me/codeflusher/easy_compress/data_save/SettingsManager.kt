package me.codeflusher.easy_compress.data_save

import com.google.gson.Gson
import me.codeflusher.easy_compress.MainApp
import me.codeflusher.easy_compress.util.Logger
import me.codeflusher.easy_compress.util.Utils
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.charset.Charset

class SettingsManager {

    fun getDefaultSettings() : Settings{
        return Settings("", debug = false, hardwareAcceleration = false)
    }

    fun readSettings() : Settings{
        val file = File(Utils.getLocalFile("settings.json"))
        val gson = Gson()
        println("Reading settings, file exists: " + file.exists() + " path:" + file.absolutePath)
        if(Logger.isInitialized()){
            Logger.message("Settings manager", "Do file exist: ", file.exists())
            Logger.message("Settings Manager", "Absolute Path:", file.absolutePath)
        }

        if (!file.exists()){
            writeSettings(getDefaultSettings())
        }
        try {
            val reader = FileReader(file.absolutePath.replace("%20", " "), Charsets.UTF_8)
            val read = reader.readText()
            if (Logger.isInitialized())
                Logger.message("Settings manager","Current config:", read)
            return gson.fromJson(read, Settings::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return getDefaultSettings()
    }

    fun writeSettings(settings: Settings){
//        println("Writing Settings")
        val gson = Gson()
        val json = gson.toJson(settings)
        try {
            val file = File(Utils.getLocalFile("settings.json"))
            if (!file.exists())
                file.createNewFile()
            val fileWriter = FileWriter(file.absolutePath.replace("%20", " "), Charsets.UTF_8)
            fileWriter.flush()
            fileWriter.append(json)
            fileWriter.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun saveFFMpeg(path: String){
        val settings = readSettings()
        writeSettings(Settings(path, settings.debug, settings.hardwareAcceleration))
    }

    fun saveHWAcceleration(useAcceleration: Boolean){
        val settings = readSettings()
        writeSettings(Settings(settings.ffmpegPath, settings.debug, useAcceleration))
    }

    fun savePreset(preset: Preset, name: String){
        val gson = Gson()
        val json = gson.toJson(preset)
        try {
            Logger.message("File IO", "Trying to work with file: user_presets/$name.json")
            val dir = File(Utils.getLocalFile("user_presets")).mkdirs()
            val file = File(Utils.getLocalFile("user_presets") + "\\$name.json")
            if (!file.exists()) {
                file.createNewFile()
            }
            val fileWriter = FileWriter(file)
            fileWriter.flush()
            fileWriter.append(json)
            fileWriter.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun loadPreset(name:String) : Preset{
        val file = File(Utils.getLocalFile("user_presets") + File.separator +"$name.json")
        val gson = Gson()
        try {
            val reader = FileReader(file.absolutePath.replace("%20", " "), Charsets.UTF_8)
            val read = reader.readText()
            Logger.debugLog("Loading Presets", read)
            return gson.fromJson(read, Preset::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return StandardPresets().fullHd60fpsLIBX265
    }

    fun loadPresetsFromFolder(){
        val gson = Gson()
        try {
            File(Utils.getLocalFile("user_presets") + File.separator).walk().forEach {
                PresetRegistry.register(loadPreset(it.name.dropLast(5)))
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}