package me.urbanfaust.easy_compress

import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import me.urbanfaust.easy_compress.data.*
import me.urbanfaust.easy_compress.data_save.Preset
import me.urbanfaust.easy_compress.data_save.PresetRegistry
import me.urbanfaust.easy_compress.data_save.SettingsManager
import me.urbanfaust.easy_compress.data_save.StandardPresets
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import java.lang.reflect.Executable
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

    /*--CHECK BOXES---*/
    @FXML
    private lateinit var encodingCheckBoxStereo: CheckBox
    @FXML
    private lateinit var encodingOverrideFileCheckBox: CheckBox
    @FXML
    private lateinit var encodingSubtitlesCheckBox: CheckBox
    @FXML
    private lateinit var encodingExperimentalCheckBox: CheckBox

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
    private lateinit var localByteSizeTextField: TextField
    @FXML
    private lateinit var settingsFFmpegPathTextFieldLocal: TextField

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
    private fun onFileDragNDropped(){
        println("Javafx currently dolbitsa v sraku")
    }
    @FXML
    private fun onFolderFileDragNDropped(){
        println("Javafx currently dolbitsa v sraku")
    }
    @FXML
    private fun onChooseFileAction(){
        println("choose file")
        val fileChooser = FileChooser()
        val files = fileChooser.showOpenDialog(localFileNameTextField.scene.window)
        if (files != null) {
            localFilePathTextField.text = files.absolutePath
            println(files.absolutePath)
        }

    }
    @FXML
    private fun onChooseFolderAction(){
        println("choose file")
        val fileChooser = DirectoryChooser()
        val files = fileChooser.showDialog(localFileNameTextField.scene.window)
        if (files != null) {
            localPathToFolderTextField.text = files.absolutePath
            println(files.absolutePath)
        }
    }

    @FXML
    private fun onPathChanged(){
        manager.saveFFMpeg(settingsFFmpegPathTextFieldLocal.text)
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

    @FXML
    private fun onEncodingStart(){
        println(settingsFFmpegPathTextFieldLocal.text + "/ffmpeg.exe")
        println(settingsFFmpegPathTextFieldLocal.text + "/ffprobe.exe")

        val ffmpeg = FFmpeg(settingsFFmpegPathTextFieldLocal.text + "/ffmpeg.exe")
        val ffprobe = FFprobe(settingsFFmpegPathTextFieldLocal.text + "/ffprobe.exe")

        val builder = FFmpegBuilder()
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
            builder.disableSubtitle()
        }

        val finalBuilder = builder.done()

        val executor = FFmpegExecutor(ffmpeg, ffprobe)

        val thread = Thread{
            executor.createTwoPassJob(finalBuilder).run()
        }
        thread.start()

    }

    fun initializeLocalChoiseBoxes(){
        localChooseBoxEncodingMethod = encodingChooseBoxMethod
        localEncodingChooseBoxFileFormat = encodingChooseBoxFileFormat
        localAudioCodecChooser = encodingAudioCodecChooser
        localSampleRateChooser = encodingSampleRateChooser
        localBitRateChooser = encodingBitRateChooser
        localVideoCodecChooser = encodingVideoCodecChooser

    }

    fun initializeLocalTextFields(){
        localFilePathTextField = encodingFilePathTextField
        localPathToFolderTextField = encodingPathToFolderTextField
        localFileNameTextField = encodingFileNameTextField
        localByteSizeTextField = encodingVideoBitRateTextField
        localFpsTextField = encodingFpsTextField
        localResolutionXTextField = encodingResolutionXTextField
        localResolutionYTextField = encodingResolutionYTextField
        settingsFFmpegPathTextFieldLocal = settingsFFmpegPathTextField
    }

    fun initializeLocalButtons(){
        localButtonChooseFolder = encodingButtonChooseFolder
        localButtonChooseFile = encodingButtonChooseFile
        localStartButton = encodingStartButton
    }

    fun initializeLocalCheckBoxes(){
        localCheckBoxStereo = encodingCheckBoxStereo
        localOverrideFileCheckBox = encodingOverrideFileCheckBox
        localSubtitlesCheckBox = encodingSubtitlesCheckBox
        localExperimentalCheckBox = encodingExperimentalCheckBox
    }

    fun registerEvents(){
        settingsFFmpegPathTextFieldLocal.textProperty().addListener { observable, oldValue, newValue -> manager.saveFFMpeg(newValue) }
        localChooseBoxEncodingMethod.selectionModel.selectedIndexProperty().addListener{observable, old, new ->
            run {
                try {
                    val preset = PresetRegistry.findInRegistry(localChooseBoxEncodingMethod.items.get(new as Int))
                    applyPreset(preset)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun registerChoiseBoxItems(){
        println(PresetRegistry.getRegisteredPresetsNames())
        localChooseBoxEncodingMethod.items.addAll(PresetRegistry.getRegisteredPresetsNames())
        localEncodingChooseBoxFileFormat.items.addAll(VideoFileTypes.values())
        localAudioCodecChooser.items.addAll(AudioCodecTypes.values())
        localSampleRateChooser.items.addAll(SampleRates.values())
        localBitRateChooser.items.addAll(BitRates.values())
        localVideoCodecChooser.items.addAll(VideoCodecs.values())
    }

    fun initializeData(){
        manager = SettingsManager()
        settingsFFmpegPathTextFieldLocal.text = manager.readSettings().ffmpegPath
    }

    fun applyPreset(preset: Preset){
        localResolutionXTextField.text = preset.resolutionX.toString()
        localResolutionYTextField.text = preset.resolutionY.toString()
        localEncodingChooseBoxFileFormat.value = preset.fileType
        localCheckBoxStereo.isSelected = preset.stereo
        localAudioCodecChooser.value = preset.audioCodec
        localSampleRateChooser.value = preset.sampleRate
        localBitRateChooser.value = preset.bitRate
        localFpsTextField.text = preset.fps.toString()
        localByteSizeTextField.text = preset.videoBitRate.toString()
        localOverrideFileCheckBox.isSelected = preset.overwrite
        localSubtitlesCheckBox.isSelected = preset.subtitles
        localExperimentalCheckBox.isSelected = preset.experimental
        localVideoCodecChooser.value = preset.videoCodec
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initializeLocalChoiseBoxes()
        initializeLocalTextFields()
        initializeLocalButtons()
        initializeLocalCheckBoxes()

        registerEvents()

        registerChoiseBoxItems()

        initializeData()
    }
}
