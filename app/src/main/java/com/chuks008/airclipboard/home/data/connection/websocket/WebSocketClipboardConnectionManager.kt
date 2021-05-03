package com.chuks008.airclipboard.home.data.connection.websocket

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.chuks008.airclipboard.home.data.clipboard.ClipboardConnection
import com.chuks008.airclipboard.home.data.connection.ClipboardConnectionManager
import okhttp3.OkHttpClient

/**
 * Default implementation of the clipboard connection manager
 * @property clipboardLifecycleInstance LifecycleRegistry
 * @property okHttpClient OkHttpClient
 * @constructor
 */
class WebSocketClipboardConnectionManager(
    override val clipboardLifecycleInstance: LifecycleRegistry,
    private val okHttpClient: OkHttpClient
): ClipboardConnectionManager<LifecycleRegistry> {

    override fun buildConnectionInstance(clipboardLocation: String): ClipboardConnection {
        return Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(clipboardLocation))
            .lifecycle(clipboardLifecycleInstance)
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()
            .create()
    }

    override fun startConnection() {
        clipboardLifecycleInstance.onNext(Lifecycle.State.Started)
    }

    override fun killConnection() {
        clipboardLifecycleInstance.onNext(Lifecycle.State.Stopped.WithReason(ShutdownReason.GRACEFUL))
    }
}