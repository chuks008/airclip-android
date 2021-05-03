package com.ygorxkharo.airclipboard.home.data.socket

import com.ygorxkharo.airclipboard.home.data.clipboard.ClipboardApi

interface ClipboardConnectionProvider {
    fun getClipboardSocket(onDiscovered: (ClipboardApi?) -> Unit)
    fun startSocket()
    fun killSocket()
}