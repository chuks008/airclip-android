package com.ygorxkharo.airclipboard.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ygorxkharo.airclipboard.home.data.clipboard.ClipBoardRepository
import com.ygorxkharo.airclipboard.home.data.socket.ClipboardConnectionProvider

class ClipboardViewModelImpl(
    private val clipBoardSocketProvider: ClipboardConnectionProvider,
    private val clipBoardRepository: ClipBoardRepository
): ViewModel(), ClipBoardViewModel {

    private var _eventListener = MutableLiveData<ClipboardState>()
    override val eventListener: LiveData<ClipboardState> = _eventListener

    override fun startClipboardDiscovery() {
        clipBoardSocketProvider.getClipboardSocket { socketInstance ->
            socketInstance?.let {
                _eventListener.postValue(ClipboardDiscoverState(true))
                clipBoardRepository.clipBoardApiInstance = it
            } ?: _eventListener.postValue(ClipboardDiscoverState(false))
        }
    }

    override fun connectToClipBoard() {
        clipBoardSocketProvider.startSocket()
        clipBoardRepository.connectToServer()
        clipBoardRepository.messageObserver.subscribe { socketPayload ->
            if(!socketPayload.status) {
                _eventListener.postValue(ClipboardConnectError())
            } else {
                when(socketPayload.message) {
                    "Connected successfully" -> _eventListener.postValue(ClipboardConnectSuccess())
                    "Message successfully received" -> _eventListener.postValue(ClipboardTextSent(socketPayload.status))
                    "Connection closed successfully" -> _eventListener.postValue(ClipboardConnectClosed())
                }
            }
        }
    }

    override fun sendTextToClipboard(text: String) {
        clipBoardRepository.sendTextToClipboard(text)
    }

    override fun disconnectFromClipboard() {
        clipBoardRepository.disconnectFromServer()
        clipBoardSocketProvider.killSocket()
    }
}