package me.codeflusher.easy_compress.view

import com.google.gson.Gson
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.input.DragEvent
import javafx.stage.DirectoryChooser
import me.codeflusher.easy_compress.MainApp
import me.codeflusher.easy_compress.data_save.Settings
import me.codeflusher.easy_compress.util.Logger
import me.codeflusher.easy_compress.util.ShortcutFactory
import me.codeflusher.easy_compress.util.Utils
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.*
import kotlin.system.exitProcess

class InstallationDialog : Initializable {


    @FXML
    private lateinit var localInstallationPath: TextField

    @FXML
    private lateinit var setupInstallationPath: TextField

    @FXML
    private lateinit var localFFmpegPath: TextField

    @FXML
    private lateinit var setupFFmpegPath: TextField

    @FXML
    private lateinit var setupCreateShortcut: CheckBox

    @FXML
    private lateinit var setupCreateFolder: CheckBox

    @FXML
    private lateinit var setupUseHWAcceleration: CheckBox

    @FXML
    private lateinit var localCreateShortcut: CheckBox

    @FXML
    private lateinit var localCreateFolder: CheckBox
    @FXML
    private lateinit var localUseHWAcceleration: CheckBox

    override fun initialize(p0: URL?, p1: ResourceBundle?) {

        initializeLocal()

    }

    /*
    * Local Initialization
    * */

    private fun initializeLocal(){
        localInstallationPath = setupInstallationPath
        localInstallationPath.text = System.getenv("ProgramFiles").plus(File.separator+"CodeFlusher"+File.separator+"EasyCompress");
        localCreateFolder = setupCreateFolder
        localCreateShortcut = setupCreateShortcut
        localUseHWAcceleration = setupUseHWAcceleration
        localFFmpegPath = setupFFmpegPath
    }

    /*
    * EVENTS
    * */

    @OptIn(ExperimentalPathApi::class)
    fun onInstall() {

//        val file = File(localInstallationPath.text)

        val file = Path.of(localInstallationPath.text)

        file.createParentDirectories()

        if (file.isRegularFile() or !file.isAbsolute){
            Logger.warnLog("Installation Path", "Invalid Path")
            Logger.warnLog("Installation Path", localInstallationPath.text)
            Utils.showUserDialog("Invalid Path", Alert.AlertType.ERROR)
            return
        }

        if(!file.all { it.name != "EasyCompress.jar" }){
            Logger.warnLog("Installation Path", "Folder Contains Files")
            Utils.showUserDialog("Folder must be empty", Alert.AlertType.ERROR)
            return
        }

//        if (file.contains(" ")){
//            Utils.showUserDialog("Path should not contain spaces", Alert.AlertType.ERROR)
//            return
//        }

        val currentFile = Path.of(MainApp.currentJavaFile)

//        val newFile = File(localInstallationPath.text.plus(File.separator+"EasyCompress.jar"))
        val newFile = Path.of(localInstallationPath.text,"EasyCompress.jar")
        newFile.deleteIfExists()
        newFile.createFile()

//        val startFile = File(localInstallationPath.text.plus(File.separator+"start.bat"))
        val startFile = Path.of(localInstallationPath.text, "start.bat")
//        val iconFile = File(localInstallationPath.text.plus(File.separator+"me/codeflusher/easy_compress/icon.ico"))
        val iconFile = Path.of(localInstallationPath.text, "icon.ico")
//        val generatedIconFile = File(Utils.createPath(MainApp.currentFolder, "me", "codeflusher","easy_compress","icon.ico"))
        val generatedIconFile = Path.of(MainApp.currentFolder, "me","codeflusher","easy_compress","icon.ico")
//        val generatedFolder = File(Utils.createPath(MainApp.currentFolder, "me"))
        val generatedFolder = Path.of(MainApp.currentFolder,"me")

//        val settingsFile = File(Paths.get(localInstallationPath.text, "settings.json"))
        val settingsFile = Path.of(localInstallationPath.text, "settings.json")
        settingsFile.deleteIfExists()
        settingsFile.createFile()

        currentFile.copyTo(newFile, overwrite = true)

//        val installationFile = File(localInstallationPath.text.plus(File.separator+"installationFolder.info"))
        val installationFile = Path.of(localInstallationPath.text, "installationFolder.info")

        installationFile.deleteIfExists()
        installationFile.createFile()

        startFile.deleteIfExists()
        startFile.createFile()

        startFile.writeText("java -Dfile.encoding=UTF-8 -jar EasyCompress.jar")

        Logger.message("Installing", "Running icon unpacker:", "java -jar " + newFile.absolutePathString() + " iconInstallation")
        val process = Runtime.getRuntime().exec("java -jar " +"\""+ newFile.absolute()+"\"" + " iconInstallation")

        while (process.isAlive){
            try{
                Thread.sleep(100)
            } catch (_: Exception){

            }

        }

        iconFile.deleteIfExists()

        generatedIconFile.copyTo(iconFile)
        generatedFolder.deleteRecursively()

        val gson = Gson()
        settingsFile.writeText(gson.toJson(
            (Settings(localFFmpegPath.text, debug = false, hardwareAcceleration = localUseHWAcceleration.isSelected))
        ))

        if (localCreateShortcut.isSelected){
            Logger.log("Installation", "Creating Desktop")
            try{
                ShortcutFactory.createDesktopShortcut(startFile.absolutePathString(), "EasyCompress.lnk", iconFile.absolutePathString())
            }catch (e:Exception){
                Logger.exception("Shortcut", e)
            }

        }

        if (localCreateFolder.isSelected){
            Logger.log("Installation", "Creating start up folder link")

            val startUpPath = Utils.createPath("AppData","Roaming","Microsoft","Windows","Start Menu","Programs")
            val startUpFile = File(System.getProperty("user.home") + startUpPath +File.separator+"EasyCompress.lnk")

            if(startUpFile.exists()){
                startUpFile.delete()
            }

            startUpFile.createNewFile()
            try{
                ShortcutFactory.createShortcut(startFile.absolutePathString(), startUpFile.absolutePath, iconFile.absolutePathString())
            }catch (e:Exception){
                Logger.exception("Shortcut", e)
            }
        }


        //Finishing
        if (Utils.askChooseDialog("Installation Finished","Successfully installed. Open now?", ButtonType.YES, ButtonType.CLOSE) == ButtonType.YES){
            Runtime.getRuntime().exec("java -jar \"" + newFile.absolutePathString()+"\"")
        }


        exitProcess(0)
    }

    fun onSkip(){
        Logger.log("Skipping Installation", MainApp.currentJavaFile)
        Logger.log("Skipping Installation", "java -jar " + MainApp.currentJavaFile.drop(1) + " noInstallation")
        Runtime.getRuntime().exec("java -jar " + MainApp.currentJavaFile.drop(1) + " noInstallation")

        exitProcess(0)
    }

    fun onChooseFolderAction() {
        val file = Utils.openDialogForDirectory(localInstallationPath.scene.window) ?: return
        localInstallationPath.text = file.absolutePath
    }

    fun onFolderDragNDropped(dragEvent: DragEvent) {
        val dragBoard = dragEvent.dragboard
        if(!dragBoard.hasFiles())
            return
        val files = dragBoard.files
        val firstDir = files.find { it.isDirectory }
        if (firstDir == null){
            return
        }
        localInstallationPath.text = firstDir.absolutePath
    }

    fun onFildManuallyFFmpeg() {
        Logger.debugLog("File Chooser", "Choose FFMpeg directory action")
        val fileChooser = DirectoryChooser()
        val file = fileChooser.showDialog(setupFFmpegPath.scene.window)
        if (file != null) {
            setupFFmpegPath.text = file.absolutePath
            Logger.log("File Chooser", "Chosen Path:", file.absolutePath)
        }
    }

}


