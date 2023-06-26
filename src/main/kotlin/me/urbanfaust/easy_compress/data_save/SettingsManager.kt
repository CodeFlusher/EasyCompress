package me.urbanfaust.easy_compress.data_save

import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class SettingsManager {

    fun saveFFMpeg(path: String){
        val gson = Gson()
        val json = gson.toJson(Settings(path))
        try {
            val file = File("settings.json")
            if (!file.exists())
                file.createNewFile()
            val fileWriter = FileWriter(file)
            fileWriter.flush()
            fileWriter.append(json)
            fileWriter.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun readSettings() : Settings{
        val file = File("settings.json")
        val gson = Gson()
        println(file.exists())
        if (!file.exists()){
            saveFFMpeg("")
            return readSettings()
        }
        try {
            val reader = FileReader(file)
            val read = reader.readText()
            println(read)
            return gson.fromJson(read, Settings::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Settings("")
    }

    fun savePreset(preset: Preset, name: String){
        val gson = Gson()
        val json = gson.toJson(preset)
        try {
            val file = File("user_presets/$name.json")
            if (!file.exists())
                file.createNewFile()
            val fileWriter = FileWriter(file)
            fileWriter.flush()
            fileWriter.append(json)
            fileWriter.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun loadPreset(name:String) : Preset{
        val file = File("user_presets/$name.json")
        val gson = Gson()
        try {
            val reader = FileReader(file)
            val read = reader.readText()
            println(read)
            return gson.fromJson(read, Preset::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return StandardPresets().fullHd60fpsLIBX265
    }
}