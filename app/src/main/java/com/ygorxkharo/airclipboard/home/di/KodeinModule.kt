package com.ygorxkharo.airclipboard.home.di

import org.kodein.di.DI

/**
 * Custom Kodein module providing a tag and [DI.Module]
 * @property TAG Module tag
 */
interface KodeinModule {
    val TAG: String

    fun initialize(): DI.Module
}