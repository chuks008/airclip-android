package com.ygorxkharo.airclipboard.home.data.threading

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Default implementation of the [CoroutineContextProvider]
 *
 * @property main CoroutineContext
 * @property io CoroutineContext
 */
class CoroutineContextProviderImpl: CoroutineContextProvider {
    override val main: CoroutineContext by lazy { Dispatchers.Main }
    override val io: CoroutineContext by lazy { Dispatchers.IO }
}