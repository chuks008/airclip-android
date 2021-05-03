package com.ygorxkharo.airclipboard.home.data.connection

import com.ygorxkharo.airclipboard.home.data.clipboard.ClipboardConnection

/**
 * Used to manage a connection to a clipboard
 *
 */
interface ClipboardConnectionManager<T: Any> {

    /**
     * Build an instance of a [ClipboardConnection] to start sending/receiving messages
     *
     * @param clipboardLocation Path to the clipboard. For example, in the case the clipboard location
     * is on an HTTP server, this could be the IP address pointing to the server hosting the clipboard
     * @return a [ClipboardConnection] instance
     */
    fun buildConnectionInstance(clipboardLocation: String): ClipboardConnection

    /**
     * Connect to the clipboard source
     */
    fun startConnection()

    /**
     * Kill the connection to the clipboard source
     */
    fun killConnection()

    /**
     * @property clipboardLifecycleInstance Controls the lifecycle of the clip board connection of
     * generic type [T]
     */
    val clipboardLifecycleInstance: T
}