package me.codeflusher.easy_compress

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.stage.DirectoryChooser
import me.codeflusher.easy_compress.data.*
import me.codeflusher.easy_compress.data_save.Preset
import me.codeflusher.easy_compress.data_save.PresetRegistry
import me.codeflusher.easy_compress.data_save.SettingsManager
import me.codeflusher.easy_compress.util.Logger
import me.codeflusher.easy_compress.util.Utils
import me.codeflusher.easy_compress.view.PresetDialog
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.bramp.ffmpeg.job.FFmpegJob
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.net.URL
import java.util.*


class MainController : Initializable {

    private lateinit var manager: SettingsManager

    /*--TEXT FIELDS---*/
    @FXML
    private lateinit var encodingFilePathTextField: TextField
    @FXML
    private lateinit var encodingPathToFolderTextField: TextField
    @FXML
    private lateinit var encodingFileNameTextField: TextField
    @FXML
    private lateinit var encodingFpsTextField: TextField
    @FXML
    private lateinit var encodingResolutionXTextField: TextField
    @FXML
    private lateinit var encodingResolutionYTextField: TextField
    @FXML
    private lateinit var encodingVideoBitRateTextField: TextField
    @FXML
    private lateinit var settingsFFmpegPathTextField: TextField
    @FXML
    private lateinit var saveDialogFileName: TextField
    @FXML
    private lateinit var saveDialogPresetName: TextField

    /*--CHOISE BOXES---*/
    @FXML
    private lateinit var encodingChooseBoxMethod: ChoiceBox<String>
    @FXML
    private lateinit var encodingChooseBoxFileFormat: ChoiceBox<VideoFileTypes>
    @FXML
    private lateinit var encodingAudioCodecChooser: ChoiceBox<AudioCodecTypes>
    @FXML
    private lateinit var encodingSampleRateChooser: ChoiceBox<SampleRates>
    @FXML
    private lateinit var encodingBitRateChooser: ChoiceBox<BitRates>
    @FXML
    private lateinit var encodingVideoCodecChooser: ChoiceBox<VideoCodecs>

    /*--BUTTONS---*/
    @FXML
    private lateinit var encodingButtonChooseFile: Button
    @FXML
    private lateinit var encodingButtonChooseFolder: Button
    @FXML
    private lateinit var encodingStartButton: Button
    @FXML
    private lateinit var onFindManualyFFmpegButton: Button
    @FXML
    private lateinit var saveDialogSavePreset: Button
    @FXML
    private lateinit var encodingSavePresetButton: Button

    /*--CHECK BOXES---*/
    @FXML
    private lateinit var encodingCheckBoxStereo: CheckBox
    @FXML
    private lateinit var encodingOverrideFileCheckBox: CheckBox
    @FXML
    private lateinit var encodingSubtitlesCheckBox: CheckBox
    @FXML
    private lateinit var encodingExperimentalCheckBox: CheckBox
    @FXML
    private lateinit var encodingDoubleEncodeCheckBox: CheckBox
    @FXML
    private lateinit var optionalHWAccelerationCheckbox: CheckBox

    /*--TEXT FIELDS---*/
    @FXML
    private lateinit var localFilePathTextField: TextField
    @FXML
    private lateinit var localPathToFolderTextField: TextField
    @FXML
    private lateinit var localFileNameTextField: TextField
    @FXML
    private lateinit var localFpsTextField: TextField
    @FXML
    private lateinit var localResolutionXTextField: TextField
    @FXML
    private lateinit var localResolutionYTextField: TextField
    @FXML
    private lateinit var localVideoBitrateTextField: TextField
    @FXML
    private lateinit var settingsFFmpegPathTextFieldLocal: TextField
    @FXML
    private lateinit var saveDialogFileNameLocal: TextField
    @FXML
    private lateinit var saveDialogPresetNameLocal: TextField

    /*--LOCAL CHOISE BOXES---*/
    @FXML
    lateinit var localChooseBoxEncodingMethod: ChoiceBox<String>
    @FXML
    lateinit var localEncodingChooseBoxFileFormat: ChoiceBox<VideoFileTypes>
    @FXML
    lateinit var localAudioCodecChooser: ChoiceBox<AudioCodecTypes>
    @FXML
    lateinit var localSampleRateChooser: ChoiceBox<SampleRates>
    @FXML
    lateinit var localBitRateChooser: ChoiceBox<BitRates>
    @FXML
    lateinit var localVideoCodecChooser: ChoiceBox<VideoCodecs>

    /*--LOCAL BUTTONS---*/
    @FXML
    private lateinit var localButtonChooseFile: Button
    @FXML
    private lateinit var localButtonChooseFolder: Button
    @FXML
    private lateinit var localStartButton: Button
    @FXML
    private lateinit var localOnFindManualyFFmpegButton: Button
    @FXML
    private lateinit var saveDialogSavePresetLocal: Button
    @FXML
    private lateinit var localSavePresetButton: Button

    /*--LOCAL CHECK BOXES---*/
    @FXML
    private lateinit var localCheckBoxStereo: CheckBox
    @FXML
    private lateinit var localOverrideFileCheckBox: CheckBox
    @FXML
    private lateinit var localSubtitlesCheckBox: CheckBox
    @FXML
    private lateinit var localExperimentalCheckBox: CheckBox
    @FXML
    private lateinit var localDoubleEncodeCheckBox: CheckBox
    @FXML
    private lateinit var localHWAccelerationCheckBox: CheckBox

    @FXML
    private fun onFileDragNDropped(){
        println("Javafx currently dolbitsa v sraku")
    }
    @FXML
    private fun onFolderFileDragNDropped(){
        println("Javafx currently dolbitsa v sraku")
    }
    @FXML
    private fun onChooseFileAction(){
        val files = Utils.openDialogForFile(localButtonChooseFile.scene.window)
        if (files != null) {
            localFilePathTextField.text = files.absolutePath
            Logger.debugLog("FXML Action", "Absolute path:",files.absolutePath)
        }
    }



    @FXML
    private fun onDownloadForWindows(){
        Desktop.getDesktop().browse(URI("https://www.gyan.dev/ffmpeg/builds/packages/ffmpeg-2023-06-21-git-1bcb8a7338-full_build.7z"));
    }

    @FXML
    private fun onChooseFolderAction(){
        val files = Utils.openDialogForDirectory(localButtonChooseFile.scene.window)
        if (files != null) {
            localPathToFolderTextField.text = files.absolutePath
            Logger.debugLog("FXML Action", "Absolute folder path:",files.absolutePath)
        }
    }

    @FXML
    private fun onPathChanged(){
        manager.saveFFMpeg(settingsFFmpegPathTextFieldLocal.text)
    }
    @FXML
    private fun onHardwareAccelerationPropertyChanged(){
        manager.saveHWAcceleration(localHWAccelerationCheckBox.isSelected)
    }

    private fun verifyFields() : Boolean{

        val fileInputPath = localFilePathTextField.text
        if (!File(fileInputPath).isAbsolute){
            Utils.showUserDialog("File path is invalid.", AlertType.WARNING)
            return false
        }
        if (localEncodingChooseBoxFileFormat.value == null){
            Utils.showUserDialog("File type must be chosen.", AlertType.WARNING)
            return false
        }
        if (localAudioCodecChooser.value == null){
            Utils.showUserDialog("Audio Codec must be chosen.", AlertType.WARNING)
            return false
        }
        if (localVideoCodecChooser.value == null){
            Utils.showUserDialog("Video Codec must be chosen.", AlertType.WARNING)
            return false
        }
        if (localSampleRateChooser.value == null){
            Utils.showUserDialog("Sample rate must be chosen.", AlertType.WARNING)
            return false
        }
        if (localBitRateChooser.value == null){
            Utils.showUserDialog("Bit rate must be chosen.", AlertType.WARNING)
            return false
        }


        return true
    }

    fun onSavePreset(presetName: String, fileName:String, dialog: PresetDialog) {
        Logger.message("Save Preset", "Preset save started")
        dialog.close()
        val name: String = presetName
        val resolutionX : Int = localResolutionXTextField.text.toInt()
        val resolutionY : Int = localResolutionYTextField.text.toInt()
        val fileType: VideoFileTypes = localEncodingChooseBoxFileFormat.value
        val stereo: Boolean = localCheckBoxStereo.isSelected
        val audioCodec: AudioCodecTypes = localAudioCodecChooser.value
        val sampleRate: SampleRates = localSampleRateChooser.value
        val bitRate: BitRates = localBitRateChooser.value
        val videoCodec: VideoCodecs = localVideoCodecChooser.value
        val fps: Int = localFpsTextField.text.toInt()
        val videoBitRate: Long = localVideoBitrateTextField.text.toLong()
        val overwrite: Boolean = localOverrideFileCheckBox.isSelected
        val subtitles: Boolean = localSubtitlesCheckBox.isSelected
        val experimental: Boolean = localExperimentalCheckBox.isSelected
        val doubleEncoding: Boolean = localDoubleEncodeCheckBox.isSelected
        val preset = Preset(
            name, resolutionX, resolutionY, fileType, stereo, audioCodec, sampleRate, bitRate, videoCodec, fps, videoBitRate, overwrite, subtitles, experimental, doubleEncoding
        )
        manager.savePreset(preset, fileName)
        PresetRegistry.register(preset)
        registerPresets()
    }

    @FXML
    fun onSavePresetButton(){
        val window = localSavePresetButton.scene.window
        window.userData = this
        val dialog = PresetDialog(window)

        dialog.showAndWait()
    }

    @FXML
    private fun onFildManuallyFFmpeg(){
        println("choose file")
        val fileChooser = DirectoryChooser()
        val files = fileChooser.showDialog(localFileNameTextField.scene.window)
        if (files != null) {
            settingsFFmpegPathTextFieldLocal.text = files.absolutePath
            println(files.absolutePath)
        }
    }

    private fun startProcessing(){
        localStartButton.text = "Processing"
        localStartButton.isDisable = true
    }
    private fun stopProcessing(message: String, alertType: AlertType){
        localStartButton.text = "Start"
        localStartButton.isDisable = false
        try {
            Logger.message("Processing", "Showing Alert")
            Alert(alertType, message).show()
            Logger.message("Processing", "Alert created")
        }catch (e: Exception){
            Logger.exception("Processing Exception",e);
        }

    }

    @FXML
    private fun onEncodingStart(){

        if (!verifyFields()){
            return
        }

        val ffmpegPath = settingsFFmpegPathTextFieldLocal.text + "/ffmpeg.exe"
        val ffmprobePath = settingsFFmpegPathTextFieldLocal.text + "/ffprobe.exe"

        println(ffmpegPath)
        println(ffmprobePath)

        if (!File(ffmpegPath).exists()){
            Utils.showUserDialog("FFmpeg Path is Invalid", AlertType.ERROR)
            return
        }
        if (!File(ffmprobePath).exists()){
            Utils.showUserDialog("FFmprobe Path is Invalid", AlertType.ERROR)
            return
        }

        val ffmpeg = FFmpeg(ffmpegPath)
        val ffprobe = FFprobe(ffmprobePath)

        if (File(localPathToFolderTextField.text + "/" + localFileNameTextField.text + "." + localEncodingChooseBoxFileFormat.value.type).exists() and localOverrideFileCheckBox.isSelected){
            val result = Utils.askYesNoDialog("Overwriting File","Output file already exists, encoding will overwrite it and file will be gone. Are you sure to start encoding?")
            if (!result){
                return
            }
        }

        val builder: FFmpegBuilder = FFmpegBuilder()
        if (localHWAccelerationCheckBox.isSelected)
            builder.addExtraArgs("-hwaccel", "cuda")
        val outputBuilder = builder
            .setInput(localFilePathTextField.text) // Filename, or a FFmpegProbeResult
            .overrideOutputFiles(localOverrideFileCheckBox.isSelected) // Override the output if it exists
            .addOutput(localPathToFolderTextField.text + "/" + localFileNameTextField.text + "." + localEncodingChooseBoxFileFormat.value.type) // Filename for the destination
            .setFormat(localEncodingChooseBoxFileFormat.value.type) // Format is inferred from filename, or can be set
            .setAudioChannels(if(localCheckBoxStereo.isSelected) 2 else 1) // Mono audio
            .setAudioCodec(localAudioCodecChooser.value.type) // using the aac codec
            .setAudioSampleRate(localSampleRateChooser.value.rate) // at 48KHz
            .setAudioBitRate(localBitRateChooser.value.rate) // at 32 kbit/s
            .setVideoCodec(localVideoCodecChooser.value.codec) // Video using x264
            .setVideoFrameRate(localFpsTextField.text.toInt(), 1) // at 24 frames per second
            .setVideoResolution(localResolutionXTextField.text.toInt(), localResolutionYTextField.text.toInt()) // at 640x480 resolution
            .setVideoBitRate(encodingVideoBitRateTextField.text.toLong())
            .setStrict(if (localExperimentalCheckBox.isSelected) FFmpegBuilder.Strict.EXPERIMENTAL else FFmpegBuilder.Strict.STRICT)

        if (!localSubtitlesCheckBox.isSelected){
            outputBuilder.disableSubtitle()
        }

        val finalBuilder = outputBuilder.done()

        Logger.debugLog("Command Line promt", ProcessBuilder(ffmpeg.path(finalBuilder.build())).command().toString())

        val executor = FFmpegExecutor(ffmpeg, ffprobe)

        var job: FFmpegJob? = null

        val thread = Thread{

            job = if (localDoubleEncodeCheckBox.isSelected)
                executor.createTwoPassJob(finalBuilder)
            else
                executor.createJob(finalBuilder)
            job!!.run()

        }

        thread.setUncaughtExceptionHandler { _, exception ->
            run {
                exception.stackTrace.forEach {
                    Logger.message("Error", it)
                }
            }
        }

        val seekerThread = Thread{
            Logger.message("Processing", "Check Started Processing")
            while(true) {
                Thread.sleep(100)
                if (job == null){
                    continue
                }
                Logger.debugLog("Processing", job!!.state)
                when(job!!.state){
                    FFmpegJob.State.WAITING -> {
                        startProcessing()
                    }
                    FFmpegJob.State.RUNNING -> {
                        startProcessing()
                    }
                    FFmpegJob.State.FINISHED -> {
                        Logger.message("Processing res", job!!.state)
                        this.stopProcessing("Successfully finished working", AlertType.INFORMATION)
//                        this.throwUserDialog(, AlertType.INFORMATION)
                        break
                    }
                    FFmpegJob.State.FAILED -> {
                        Logger.message("Processing res", job!!.state)
                        this.stopProcessing("Video Encoding Failed", AlertType.ERROR)
//                        Utils.showUserDialog("Video Encoding Failed", AlertType.ERROR)
                        break
                    }
                    null->{
                    }
                }
            }
        }



        Logger.message("Processing", "Starting Processing")

        thread.start()

        Logger.message("Processing", "Seeker Processing")

        seekerThread.setUncaughtExceptionHandler { _, _ ->
                run {

                }
            }
        Platform.runLater(seekerThread)
//        var data = seekerThread.start()

    }

    private fun initializeLocalChoiseBoxes(){
        localChooseBoxEncodingMethod = encodingChooseBoxMethod
        localEncodingChooseBoxFileFormat = encodingChooseBoxFileFormat
        localAudioCodecChooser = encodingAudioCodecChooser
        localSampleRateChooser = encodingSampleRateChooser
        localBitRateChooser = encodingBitRateChooser
        localVideoCodecChooser = encodingVideoCodecChooser

    }

    private fun initializeLocalTextFields(){
        localFilePathTextField = encodingFilePathTextField
        localPathToFolderTextField = encodingPathToFolderTextField
        localFileNameTextField = encodingFileNameTextField
        localVideoBitrateTextField = encodingVideoBitRateTextField
        localFpsTextField = encodingFpsTextField
        localResolutionXTextField = encodingResolutionXTextField
        localResolutionYTextField = encodingResolutionYTextField
        settingsFFmpegPathTextFieldLocal = settingsFFmpegPathTextField
    }

    private fun initializeDialog(){
        saveDialogFileNameLocal = saveDialogFileName
        saveDialogPresetNameLocal = saveDialogPresetName
        saveDialogSavePresetLocal = saveDialogSavePreset
    }

    private fun initializeLocalButtons(){
        localButtonChooseFolder = encodingButtonChooseFolder
        localButtonChooseFile = encodingButtonChooseFile
        localStartButton = encodingStartButton
        localSavePresetButton =encodingSavePresetButton
    }

    private fun initializeLocalCheckBoxes(){
        localCheckBoxStereo = encodingCheckBoxStereo
        localOverrideFileCheckBox = encodingOverrideFileCheckBox
        localSubtitlesCheckBox = encodingSubtitlesCheckBox
        localExperimentalCheckBox = encodingExperimentalCheckBox
        localDoubleEncodeCheckBox = encodingDoubleEncodeCheckBox
        localHWAccelerationCheckBox = optionalHWAccelerationCheckbox
    }

    private fun registerEvents(){
        settingsFFmpegPathTextFieldLocal.textProperty().addListener { _, _, newValue -> manager.saveFFMpeg(newValue) }
        localHWAccelerationCheckBox.selectedProperty().addListener{ _, _, newValue -> manager.saveHWAcceleration(newValue)}
        localChooseBoxEncodingMethod.selectionModel.selectedIndexProperty().addListener{ _, _, new ->
            run {
                try {
                    val preset = PresetRegistry.findInRegistry(localChooseBoxEncodingMethod.items[new as Int])
                    applyPreset(preset)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun registerChooseBoxItems(){
        localEncodingChooseBoxFileFormat.items.addAll(VideoFileTypes.entries)
        localAudioCodecChooser.items.addAll(AudioCodecTypes.entries)
        localSampleRateChooser.items.addAll(SampleRates.entries)
        localBitRateChooser.items.addAll(BitRates.entries)
        localVideoCodecChooser.items.addAll(VideoCodecs.entries)
    }

    private fun registerPresets(){
        localChooseBoxEncodingMethod.items.clear()
        localChooseBoxEncodingMethod.items.addAll(PresetRegistry.getRegisteredPresetsNames())
    }

    private fun initializeData(){
        manager = SettingsManager()
        manager.loadPresetsFromFolder()
        val settings = manager.readSettings()
        settingsFFmpegPathTextFieldLocal.text = settings.ffmpegPath
        localHWAccelerationCheckBox.isSelected = settings.hardwareAcceleration
        registerPresets()
    }

    private fun applyPreset(preset: Preset){
        localResolutionXTextField.text = preset.resolutionX.toString()
        localResolutionYTextField.text = preset.resolutionY.toString()
        localEncodingChooseBoxFileFormat.value = preset.fileType
        localCheckBoxStereo.isSelected = preset.stereo
        localAudioCodecChooser.value = preset.audioCodec
        localSampleRateChooser.value = preset.sampleRate
        localBitRateChooser.value = preset.bitRate
        localFpsTextField.text = preset.fps.toString()
        localVideoBitrateTextField.text = preset.videoBitRate.toString()
        localOverrideFileCheckBox.isSelected = preset.overwrite
        localSubtitlesCheckBox.isSelected = preset.subtitles
        localExperimentalCheckBox.isSelected = preset.experimental
        localVideoCodecChooser.value = preset.videoCodec
        localDoubleEncodeCheckBox.isSelected = preset.doubleEncoding
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        initializeLocalChoiseBoxes()
        initializeLocalTextFields()
        initializeLocalButtons()
        initializeLocalCheckBoxes()

        registerEvents()

        registerChooseBoxItems()

        initializeData()
    }
}
