package com.ygorxkharo.airclipboard.home.data.clipboard

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

/**
 * Used to connect to the server hosting the socket for the clipboard
 */
interface ClipboardApi {

    /**
     * OBserve events coming from the web socket
     *
     * @return A [Flowable] which will constantly receive events each time the server send one
     */
    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    /**
     * Send a payload via the websocket
     *
     * @param copiedText Object representing payload sent to websocket
     */
    @Send
    fun sendCopiedText(copiedText: CopyAction)
}