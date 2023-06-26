package me.urbanfaust.easy_compress.data_save

import me.urbanfaust.easy_compress.data.*

data class Preset(
    val name: String,
    val resolutionX : Int,
    val resolutionY : Int,
    val fileType: VideoFileTypes,
    val stereo: Boolean,
    val audioCodec: AudioCodecTypes,
    val sampleRate: SampleRates,
    val bitRate: BitRates,
    val videoCodec: VideoCodecs,
    val fps: Int,
    val videoBitRate: Long,
    val overwrite: Boolean,
    val subtitles: Boolean,
    val experimental: Boolean,
    val doubleEncoding: Boolean
)
