package com.ygorxkharo.airclipboard.home.data.connection.websocket

import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.ygorxkharo.airclipboard.BuildConfig
import com.ygorxkharo.airclipboard.home.data.clipboard.ClipboardConnection
import com.ygorxkharo.airclipboard.home.data.connection.ClipboardConnectionManager
import com.ygorxkharo.airclipboard.home.data.connection.ClipboardConnectionProvider
import com.ygorxkharo.airclipboard.home.data.threading.CoroutineContextProviderImpl
import com.ygorxkharo.airclipboard.home.data.networking.NetworkScanner
import com.ygorxkharo.airclipboard.home.data.networking.ThreadingManager

/**
 * Default implementation for the Websocket provider
 */
class WebSocketClipboardProviderImpl(
    private val socketManager: ClipboardConnectionManager<LifecycleRegistry>
): ClipboardConnectionProvider {

    private val AIR_CLIP_PORT = BuildConfig.AIRCLIP_PORT
    private val socketUrlTemplate = "ws://%s"

    override fun getClipboardConnection(onDiscovered: (ClipboardConnection?) -> Unit) {
        val localConnectionAddress = NetworkScanner.getIpv4HostAddress()
        localConnectionAddress?.let { ipv4String ->
            ThreadingManager.ioToMain(
                workOnIo = {
                    NetworkScanner.confirmSocketWithListeningPort(ipv4String, AIR_CLIP_PORT)
                },
                workOnMain = { serverUrl ->
                    serverUrl?.let { ipAddress ->
                        val socketInstance = socketManager.buildConnectionInstance(
                            String.format(socketUrlTemplate, ipAddress)
                        )
                        onDiscovered(socketInstance)
                    } ?: onDiscovered(null)
                },
                CoroutineContextProviderImpl()
            )
        } ?: onDiscovered(null)
    }

    override fun startConnection() {
        socketManager.startConnection()
    }

    override fun destroyConnection() {
        socketManager.killConnection()
    }
}