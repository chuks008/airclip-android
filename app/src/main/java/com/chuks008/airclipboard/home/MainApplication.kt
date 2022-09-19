package com.chuks008.airclipboard.home

import android.app.Application
import com.chuks008.airclipboard.home.di.ClipboardModule
import com.chuks008.airclipboard.home.di.NetworkingModule
import org.kodein.di.DIAware
import org.kodein.di.DI

open class MainApplication: Application(), DIAware {
    override val di: DI by DI.lazy {
        importAll(
            NetworkingModule(applicationContext).initialize(),
            ClipboardModule().initialize()
        )
    }
}
