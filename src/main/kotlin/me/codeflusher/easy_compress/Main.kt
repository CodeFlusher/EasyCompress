package me.codeflusher.easy_compress

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import me.codeflusher.easy_compress.data_save.SettingsManager
import me.codeflusher.easy_compress.data_save.StandardPresets
import me.codeflusher.easy_compress.util.Logger
import me.codeflusher.easy_compress.util.Utils
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.*
import kotlin.properties.Delegates


class MainApp : Application() {


    fun initializeSetup(stage: Stage) {
        initialize(stage, "installation_dialog.fxml")
    }

    private fun initializeApplication(stage: Stage) {
        StandardPresets()
        initialize(stage, "main.fxml")
    }

    private fun initialize(stage: Stage, path: String) {

        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource(path))
        val xml = fxmlLoader.load<Parent>()
        val scene = Scene(xml, 600.0, 400.0)
        initializeApplicationData(stage, scene)
    }

    private fun initializeApplicationData(stage: Stage, scene: Scene) {
        stage.isResizable = false
        stage.title = "FFMpeg Tool"
        stage.scene = scene
    }

    override fun start(stage: Stage) {
        if (skipInstallation)
            initializeApplication(stage)
        else
            initializeSetup(stage)
        stage.show()
    }

    companion object {
        @JvmStatic
        val currentJavaFile: String =
            Paths.get(Companion::class.java.protectionDomain.codeSource.location.path.drop(1)).absolutePathString()
                .replace("%20", " ")

        @JvmStatic
        val currentFolder: String =
            Paths.get(Companion::class.java.protectionDomain.codeSource.location.path.drop(1)).parent.absolutePathString()
                .replace("%20", " ")

        @JvmStatic
        val javaPath: String = System.getProperty("java.home").plus(File.separator).plus("bin")

        @JvmStatic
        var skipInstallation: Boolean by Delegates.notNull<Boolean>()

        @JvmStatic
        fun isLoadingFromInstallationFolder(): Boolean {
            return Utils.getLocalFile("installationFolder.info").exists()
        }
    }


}

@OptIn(ExperimentalPathApi::class)
fun main(args: Array<String>) {

    if (args.contains("iconInstallation")) {
        val command = "\"" + MainApp.javaPath.plus(File.separator)
            .plus("jar.exe") + "\"" + " -xf " + "\"" + MainApp.currentJavaFile + "\"" + " me/codeflusher/easy_compress/icon.ico"

        println(command)
        val process = Runtime.getRuntime().exec(command)

        while (process.isAlive) {
            try {
                Thread.sleep(100)
            } catch (_: Exception) {

            }
        }

        val iconFile = Paths.get(MainApp.currentFolder, "me", "codeflusher", "easy_compress", "icon.ico")
        val generatedFolder = Paths.get(MainApp.currentFolder, "me")

        iconFile.copyTo(Paths.get(MainApp.currentFolder, "icon.ico"))
        generatedFolder.deleteRecursively()

        return
    }

    MainApp.skipInstallation = args.contains("noInstallation") or MainApp.isLoadingFromInstallationFolder()
    Logger.getInstance(SettingsManager().readSettings().debug)
    Logger.message("Initialization", "Init, Debug:", SettingsManager().readSettings())
    Logger.debugLog("Current File", MainApp.currentJavaFile, MainApp.currentFolder)

    Application.launch(MainApp::class.java)
}