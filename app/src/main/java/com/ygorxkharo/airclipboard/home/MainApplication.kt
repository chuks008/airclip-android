package com.ygorxkharo.airclipboard.home

import android.app.Application
import com.ygorxkharo.airclipboard.home.di.ClipboardModule
import com.ygorxkharo.airclipboard.home.di.NetworkingModule
import org.kodein.di.DIAware
import org.kodein.di.DI

open class MainApplication: Application(), DIAware {
    override val di: DI by DI.lazy {
        importAll(
            NetworkingModule().initialize(),
            ClipboardModule().initialize()
        )
    }
}
