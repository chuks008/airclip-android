package com.chuks008.airclipboard.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chuks008.airclipboard.home.data.clipboard.ClipBoardRepository
import com.chuks008.airclipboard.home.data.connection.ClipboardConnectionProvider

/**
 * Default implementation of state management for the clipboard screen
 *
 * @property clipBoardSocketProvider Provides a connection to a clipboard
 * @property clipBoardRepository Provides a means to send/receive messages to the clip board
 */
class DefaultClipboardViewModel(
    private val clipBoardSocketProvider: ClipboardConnectionProvider,
    private val clipBoardRepository: ClipBoardRepository
): ViewModel(), ClipBoardViewModel {

    /**
     * @property _eventListener Sends UI events based on messages received from the clip board
     */
    private var _eventListener = MutableLiveData<ClipboardState>()

    /**
     * @property eventListener LiveData<ClipboardState>
     */
    override val eventListener: LiveData<ClipboardState> = _eventListener

    override fun startClipboardDiscovery() {
        clipBoardSocketProvider.getClipboardConnection { socketInstance ->
            socketInstance?.let {
                _eventListener.postValue(ClipboardDiscoverState(true))
                clipBoardRepository.clipBoardApiInstance = it
            } ?: _eventListener.postValue(ClipboardDiscoverState(false))
        }
    }

    override fun connectToClipBoard() {
        clipBoardSocketProvider.startConnection()
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
        clipBoardSocketProvider.destroyConnection()
    }
}