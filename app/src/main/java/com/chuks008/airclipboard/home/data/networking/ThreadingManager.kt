package com.chuks008.airclipboard.home.data.networking

import com.chuks008.airclipboard.home.data.threading.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Manage work on different processes. Let's take it as a replacement for an Android AsyncTask
 */
object ThreadingManager {

    /**
     * Perform Tasks on a background process and report the result on the main application process
     * @param workOnIo Used to perform background task
     * @param workOnMain Used to perform a task on the main application process
     * @param contextProvider Provides various types of coroutine contexts to perform a task on the
     * proper scope
     * @return s cancellable [Job] in the case the background task has to be interrupted
     */
    fun <T : Any> ioToMain(
        workOnIo: suspend (() -> T?),
        workOnMain: ((T?) -> Unit),
        contextProvider: CoroutineContextProvider
    ) =  CoroutineScope(contextProvider.main).launch {
            val backgroundTaskResult = withContext(contextProvider.io) { workOnIo() }
            workOnMain(backgroundTaskResult)
        }
}