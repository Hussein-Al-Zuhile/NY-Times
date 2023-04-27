package com.hussein.nytimes.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class ErrorBody(val fault: Fault)

@JsonClass(generateAdapter = true)
data class Fault(@Json(name = "faultstring") val message: String)