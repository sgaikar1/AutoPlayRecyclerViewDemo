package com.sgaikar1.autoplayrecyclerview

data class MediaObject(
    var title: String? = null,
    var media_url: String? = null,
    var thumbnail: String? = null,
    var description: String? = null,
    var isPlaying: Boolean = false)
