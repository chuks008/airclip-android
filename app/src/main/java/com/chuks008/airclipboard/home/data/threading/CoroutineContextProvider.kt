package com.chuks008.airclipboard.home.data.threading

import kotlin.coroutines.CoroutineContext

/**
 * Provides a [CoroutineContext] to be used for different processes when executing a task
 *
 * @property main Used to perform tasks on the main application process
 * @property io CoroutineContext Used to perform tasks on the background application process
 */
interface CoroutineContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}