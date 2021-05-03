package com.ygorxkharo.airclipboard.home.di

import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.ygorxkharo.airclipboard.home.data.socket.ClipboardConnectionProvider
import com.ygorxkharo.airclipboard.home.data.socket.DefaultSocketManager
import com.ygorxkharo.airclipboard.home.data.socket.SocketManager
import com.ygorxkharo.airclipboard.home.data.socket.WebSocketProviderImpl
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
        bind<SocketManager>() with provider {
            val okHttpClient: OkHttpClient = instance()
            DefaultSocketManager(lifecycleRegistry, okHttpClient)
        }

        bind<ClipboardConnectionProvider>() with provider {
            val socketManager: SocketManager = instance()
            WebSocketProviderImpl(socketManager)
        }
    }
}