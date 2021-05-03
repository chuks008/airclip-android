package com.ygorxkharo.airclipboard.home.domain

data class ClipboardMessagePayload(
    val status: Boolean,
    val message: String? = null
)