package com.ygorxkharo.airclipboard.home.data.clipboard

import com.ygorxkharo.airclipboard.home.domain.ClipboardMessagePayload
import io.reactivex.subjects.Subject

/**
 * Interacts with the clipboard resource to send/receive messages
 *
 *
 * @property clipBoardApiInstance Application Access Interface to connect to the clipboard resource
 */
interface ClipBoardRepository {

    /**
     * @property messageObserver Observer to send/receive messages to the clipboard resource
     */
    val messageObserver: Subject<ClipboardMessagePayload>

    var clipBoardApiInstance: ClipboardConnection
    fun connectToServer()
    fun sendTextToClipboard(text: String)
    fun disconnectFromServer()
}