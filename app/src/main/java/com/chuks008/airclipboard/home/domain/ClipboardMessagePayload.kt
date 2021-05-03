package com.chuks008.airclipboard.home.domain

/**
 * Represents a payload a clip board source
 *
 * @property status Provides status information on payload
 * @property message Content of the payload
 */
data class ClipboardMessagePayload(
    val status: Boolean,
    val message: String? = null
)