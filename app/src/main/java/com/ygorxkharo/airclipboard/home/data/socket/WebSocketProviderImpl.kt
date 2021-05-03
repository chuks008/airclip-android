package com.ygorxkharo.airclipboard.home.data.socket

import com.ygorxkharo.airclipboard.BuildConfig
import com.ygorxkharo.airclipboard.home.data.clipboard.ClipboardApi
import com.ygorxkharo.airclipboard.home.data.threading.CoroutineContextProviderImpl
import com.ygorxkharo.airclipboard.home.data.networking.NetworkScanner
import com.ygorxkharo.airclipboard.home.data.networking.ThreadingManager

/**
 * Default implementation for the Websocket provider
 */
class WebSocketProviderImpl(private val socketManager: SocketManager): ClipboardConnectionProvider {
    private val AIR_CLIP_PORT = BuildConfig.AIRCLIP_PORT
    private val socketUrlTemplate = "ws://%s"

    override fun getClipboardSocket(onDiscovered: (ClipboardApi?) -> Unit) {
        val localConnectionAddress = NetworkScanner.getIpv4HostAddress()
        localConnectionAddress?.let { ipv4String ->
            ThreadingManager.ioToMain(
                workOnIo = {
                    NetworkScanner.confirmSocketWithListeningPort(ipv4String, AIR_CLIP_PORT)
                },
                workOnMain = { serverUrl ->
                    serverUrl?.let { ipAddress ->
                        val socketInstance = socketManager.buildSocketInstance(
                            String.format(socketUrlTemplate, ipAddress)
                        )
                        onDiscovered(socketInstance)
                    } ?: onDiscovered(null)
                },
                CoroutineContextProviderImpl()
            )
        } ?: onDiscovered(null)
    }

    override fun startSocket() {
        socketManager.startConnection()
    }

    override fun killSocket() {
        socketManager.killConnection()
    }
}