package me.urbanfaust.easy_compress

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import me.urbanfaust.easy_compress.data_save.PresetRegistry
import me.urbanfaust.easy_compress.data_save.SettingsManager
import me.urbanfaust.easy_compress.data_save.StandardPresets

class MainApp : Application() {
    override fun start(stage: Stage) {
        StandardPresets()
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("main.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        stage.isResizable = false
        stage.title = "FFMpeg Tool"
        stage.scene = scene
        stage.show()
    }
}

fun main() {

    Application.launch(MainApp::class.java)
}