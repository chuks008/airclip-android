package com.ygorxkharo.airclipboard.home.di

import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.ygorxkharo.airclipboard.home.data.connection.ClipboardConnectionProvider
import com.ygorxkharo.airclipboard.home.data.connection.websocket.WebSocketClipboardConnectionManager
import com.ygorxkharo.airclipboard.home.data.connection.ClipboardConnectionManager
import com.ygorxkharo.airclipboard.home.data.connection.websocket.WebSocketClipboardProviderImpl
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import java.util.concurrent.TimeUnit

/**
 * Dependency container for loading networking dependencies
 *
 */
class NetworkingModule: KodeinModule{

    override val TAG = "network_module"
    private val TIMEOUT_LIMIT_SECS = 10L
    private val SOCKET_THROTTLE_LIMIT_SECS = 0L

    /**
     * Initialize networking dependencies
     * @return a module containing bindings for networking
     */
    override fun initialize() = DI.Module(TAG) {

        val lifecycleRegistry = LifecycleRegistry(SOCKET_THROTTLE_LIMIT_SECS)
        bind<OkHttpClient>() with provider {
            OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_LIMIT_SECS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_LIMIT_SECS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_LIMIT_SECS, TimeUnit.SECONDS)
                .build()
        }
        bind<ClipboardConnectionManager<LifecycleRegistry>>() with provider {
            val okHttpClient: OkHttpClient = instance()
            WebSocketClipboardConnectionManager(lifecycleRegistry, okHttpClient)
        }

        bind<ClipboardConnectionProvider>() with provider {
            val socketManager: ClipboardConnectionManager<LifecycleRegistry> = instance()
            WebSocketClipboardProviderImpl(socketManager)
        }
    }
}