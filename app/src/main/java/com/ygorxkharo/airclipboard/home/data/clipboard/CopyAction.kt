package com.ygorxkharo.airclipboard.home.data.clipboard

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Object representing payload sent to websocket
 *
 * @property text text payload to send through the websocket
 * @property message Message type used by the server to know how o handle the payload
 */
@JsonClass(generateAdapter = true)
data class CopyAction(
    @Json(name = "text") val text: String,
    @Json(name = "copy") val message: String = "copy",
)