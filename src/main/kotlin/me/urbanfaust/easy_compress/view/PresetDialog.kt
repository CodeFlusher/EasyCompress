package me.urbanfaust.easy_compress.view

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import me.urbanfaust.easy_compress.MainApp
import me.urbanfaust.easy_compress.MainController
import me.urbanfaust.easy_compress.data_save.PresetLink
import me.urbanfaust.easy_compress.util.Logger
import net.bramp.ffmpeg.io.LoggingFilterReader


class PresetDialog(owner: Window) : Dialog<PresetLink>() {

    @FXML
    private lateinit var saveDialogPresetName :TextField
    @FXML
    private lateinit var saveDialogFileName : TextField
    @FXML
    private lateinit var saveDialogSavePreset :Button
    @FXML
    private lateinit var saveDialogPresetNameLocal :TextField
    @FXML
    private lateinit var saveDialogFileNameLocal : TextField
    @FXML
    private lateinit var saveDialogSavePresetLocal :Button

    private val connection: ObjectProperty<PresetLink?> = SimpleObjectProperty(null)

    fun initLocals(){
        saveDialogPresetNameLocal= saveDialogPresetName
        saveDialogFileNameLocal= saveDialogFileName
        saveDialogSavePresetLocal = saveDialogSavePreset

    }
    @FXML
    fun onSavePreset(){
        Logger.message("Preset Dialog", "Caught save dialog button click")
        Logger.message("Preset Dialog", "SaveDialogPresetNameLocal.text = " +saveDialogPresetNameLocal.text + "\n" + "SaveDialogFileNameLocal.text = " +saveDialogFileNameLocal.text)
        if (saveDialogPresetNameLocal.text == "" || saveDialogFileNameLocal.text == ""){
            Logger.message("Preset Dialog", "Couldn't save the preset: invalid params")
            val alert = Alert(AlertType.INFORMATION)
            alert.title = "Error"
            alert.headerText = "Invalid Params"
            alert.contentText = "All text fields must be filled"

            alert.showAndWait()
        }else{
            Logger.message("Preset Dialog", "Preset successfully saved")
            (owner.userData as MainController).onSavePreset(saveDialogPresetNameLocal.text, saveDialogFileNameLocal.text, this)
        }

    }

    init {

        Logger.message("Preset Init", "Initializing the preset")
        val loader = FXMLLoader(MainApp::class.java.getResource("save_preset_dialog.fxml"))
        loader.setController(this)

        val dialogPane = loader.load<DialogPane>()

        dialogPane.buttonTypes.add(ButtonType.CLOSE)

        initModality(Modality.NONE)
        initOwner(owner)
        initLocals()

        owner.setOnCloseRequest {
            Logger.message("Dialog Panel", "On close Request")
            hide()
        }

        Logger.message("Preset Init", "Adding event handler")
        saveDialogSavePresetLocal.addEventHandler(ActionEvent.ANY){
            onSavePreset()
        }

        Logger.message("Preset Init", "Final steps")
        title = "Save Preset"
        isResizable = false

        Logger.message("Preset Init", "Showing dialog pane")
        setDialogPane(dialogPane)
        setResultConverter {
            connection.value
        }
    }
}