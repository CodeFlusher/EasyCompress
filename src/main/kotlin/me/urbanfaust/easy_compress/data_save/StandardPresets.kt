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
        experimental = true
    )

    init {
        PresetRegistry.register(fullHd60fpsLIBX265)
    }

}