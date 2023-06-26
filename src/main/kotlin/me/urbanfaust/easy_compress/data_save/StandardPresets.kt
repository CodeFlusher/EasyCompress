package me.urbanfaust.easy_compress.data_save

import me.urbanfaust.easy_compress.data.*

class StandardPresets {
    val fullHd60fpsLIBX265 = Preset(
        "Full HD Compressed 2M bitrate",
        1920,
        1080,
        VideoFileTypes.MP4,
        true,
        AudioCodecTypes.AAC,
        SampleRates.RATE_48000,
        BitRates.RATE_160K,
        VideoCodecs.LIBX265,
        60,
        2000000L,
        overwrite = true,
        subtitles = false,
        experimental = true,
        doubleEncoding = true
    )
    val fourK60fpsLIBX265 = Preset(
        "4K Compressed 6M bitrate",
        3840,
        2160,
        VideoFileTypes.MP4,
        true,
        AudioCodecTypes.AAC,
        SampleRates.RATE_48000,
        BitRates.RATE_160K,
        VideoCodecs.LIBX265,
        60,
        6000000L,
        overwrite = true,
        subtitles = false,
        experimental = true,
        doubleEncoding = true
    )
    val fullHd120fpsLIBX265 = Preset(
        "Full HD 120fps Compressed 4M bitrate",
        1920,
        1080,
        VideoFileTypes.MP4,
        true,
        AudioCodecTypes.AAC,
        SampleRates.RATE_48000,
        BitRates.RATE_160K,
        VideoCodecs.LIBX265,
        120,
        4000000L,
        overwrite = true,
        subtitles = false,
        experimental = true,
        doubleEncoding = true
    )
    val fourK120fpsLIBX265 = Preset(
        "4K Compressed 10M bitrate",
        3840,
        2160,
        VideoFileTypes.MP4,
        true,
        AudioCodecTypes.AAC,
        SampleRates.RATE_48000,
        BitRates.RATE_160K,
        VideoCodecs.LIBX265,
        120,
        10000000L,
        overwrite = true,
        subtitles = false,
        experimental = true,
        doubleEncoding = true
    )

    init {
        PresetRegistry.register(fullHd60fpsLIBX265)
        PresetRegistry.register(fullHd120fpsLIBX265)
        PresetRegistry.register(fourK60fpsLIBX265)
        PresetRegistry.register(fourK120fpsLIBX265)
    }

}