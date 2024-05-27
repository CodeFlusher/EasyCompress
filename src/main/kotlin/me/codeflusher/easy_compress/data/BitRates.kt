package me.codeflusher.easy_compress.data

enum class BitRates(val rate: Long) {
    RATE_32K(32768),
    RATE_96K(98304),
    RATE_128K(131072),
    RATE_160K(163840),
    RATE_256K(262144),
    RATE_320K(327680)
}
