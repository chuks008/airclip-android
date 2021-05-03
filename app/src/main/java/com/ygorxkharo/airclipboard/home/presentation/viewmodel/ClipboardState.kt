package com.ygorxkharo.airclipboard.home.presentation.viewmodel

interface ClipboardState

class ClipboardDiscoverState(val isFound: Boolean): ClipboardState
class ClipboardConnectSuccess: ClipboardState
class ClipboardConnectError: ClipboardState
class ClipboardTextSent(val isSent: Boolean): ClipboardState
class ClipboardConnectClosed: ClipboardState