package com.hussein.nytimes.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Topic(
    val id: Long,
    val url: String,
    val source: String,
    @Json(name = "published_date") val publishedDate: String,
    val type: String,
    val title: String,
    @Json(name = "abstract") val details: String,
    val media: List<Media>,
) {
    val imagesUrlsWithItsCaption =
        media.filter { it.type == "image" }.map {
            it.caption to it.metaData.map { it.url }
        }
}

@JsonClass(generateAdapter = true)
data class Media(
    val type: String,
    val caption: String,
    @Json(name = "media-metadata") val metaData: List<MediaMetadata>
)

@JsonClass(generateAdapter = true)
data class MediaMetadata(
    val url: String,
)
