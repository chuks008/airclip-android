package com.ygorxkharo.airclipboard.home.data.socket

import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.ygorxkharo.airclipboard.home.data.clipboard.ClipboardApi

interface SocketManager {
    val socketLifecycleInstance: LifecycleRegistry
    fun buildSocketInstance(socketUrl: String): ClipboardApi
    fun startConnection()
    fun killConnection()
}