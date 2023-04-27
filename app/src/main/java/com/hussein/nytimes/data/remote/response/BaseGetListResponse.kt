package com.hussein.nytimes.data.remote.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BaseGetListResponse<T>(val status: String, val results: List<T>)