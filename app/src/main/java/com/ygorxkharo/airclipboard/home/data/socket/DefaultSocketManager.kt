package com.ygorxkharo.airclipboard.home.data.socket

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.ygorxkharo.airclipboard.home.data.clipboard.ClipboardApi
import okhttp3.OkHttpClient

class DefaultSocketManager(
    override val socketLifecycleInstance: LifecycleRegistry,
    private val okHttpClient: OkHttpClient
): SocketManager {

    override fun buildSocketInstance(socketUrl: String): ClipboardApi {
        return Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(socketUrl))
            .lifecycle(socketLifecycleInstance)
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()
            .create()
    }

    override fun startConnection() {
        socketLifecycleInstance.onNext(Lifecycle.State.Started)
    }

    override fun killConnection() {
        socketLifecycleInstance.onNext(Lifecycle.State.Stopped.WithReason(ShutdownReason.GRACEFUL))
    }
}