package com.ygorxkharo.airclipboard.home.presentation.viewmodel

import androidx.lifecycle.LiveData

interface ClipBoardViewModel {
    val eventListener: LiveData<ClipboardState>
    fun startClipboardDiscovery()
    fun connectToClipBoard()
    fun disconnectFromClipboard()
    fun sendTextToClipboard(text: String)
}