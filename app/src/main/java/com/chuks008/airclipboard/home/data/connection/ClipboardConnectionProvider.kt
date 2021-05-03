package com.chuks008.airclipboard.home.data.connection

import com.chuks008.airclipboard.home.data.clipboard.ClipboardConnection

/**
 * Provides an instance of connection to the clipbaord resource
 */
interface ClipboardConnectionProvider {

    /**
     * Callback to provide a connection to a clip board resource if one is available
     *
     * @param onDiscovered Sends an instance of a clip board connection to the caller
     */
    fun getClipboardConnection(onDiscovered: (ClipboardConnection?) -> Unit)

    /**
     * Initialize the components to start a connection to the clip board
     */
    fun startConnection()

    /**
     * Decommission the connection to the clip board
     */
    fun destroyConnection()
}