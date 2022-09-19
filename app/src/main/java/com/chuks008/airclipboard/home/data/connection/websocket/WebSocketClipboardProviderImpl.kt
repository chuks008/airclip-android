package com.chuks008.airclipboard.home.data.connection.websocket

import android.content.Context
import android.util.Log
import com.chuks008.airclipboard.BuildConfig
import com.chuks008.airclipboard.home.data.clipboard.ClipboardConnection
import com.chuks008.airclipboard.home.data.connection.ClipboardConnectionManager
import com.chuks008.airclipboard.home.data.connection.ClipboardConnectionProvider
import com.chuks008.airclipboard.home.data.networking.NetworkScanner
import com.chuks008.airclipboard.home.data.threading.CoroutineContextProviderImpl
import com.chuks008.airclipboard.home.data.threading.ThreadingManager
import com.tinder.scarlet.lifecycle.LifecycleRegistry

/**
 * Default implementation for the Websocket provider
 */
class WebSocketClipboardProviderImpl(
    private val socketManager: ClipboardConnectionManager<LifecycleRegistry>,
    private val appContext: Context
): ClipboardConnectionProvider {

    private val AIR_CLIP_PORT = BuildConfig.AIRCLIP_PORT
    private val socketUrlTemplate = "ws://%s"

    override fun getClipboardConnection(onDiscovered: (ClipboardConnection?) -> Unit) {

        val hotspotIpAddress = NetworkScanner.getHotspotIPAddress(appContext)
        Log.e("WebSocketClipboard", "IP Address for hotspot: $hotspotIpAddress")

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