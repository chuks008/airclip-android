package com.ygorxkharo.airclipboard.home.presentation.viewmodel

import androidx.lifecycle.LiveData

/**
 * State management for the clip board screen
 *
 * @property eventListener LiveData<ClipboardState>
 */
interface ClipBoardViewModel {
    val eventListener: LiveData<ClipboardState>

    /**
     * Discover clip boards to connect to
     */
    fun startClipboardDiscovery()

    /**
     * Connect to a specific clip board
     */
    fun connectToClipBoard()

    /**
     * Disconnect from the currently connect clipboard
     */
    fun disconnectFromClipboard()

    /**
     * Send a text snippet to the clip board
     *
     * @param text Text content to send out
     */
    fun sendTextToClipboard(text: String)
}