package com.ygorxkharo.airclipboard.home.data.clipboard

import com.tinder.scarlet.WebSocket
import com.ygorxkharo.airclipboard.home.domain.ClipboardMessagePayload
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

/**
 * Default implementation of the clipboard repository
 */
class ClipBoardRepositoryImpl(
    override val messageObserver: Subject<ClipboardMessagePayload>
): ClipBoardRepository {

    override lateinit var clipBoardApiInstance: ClipboardConnection

    /**
     * @property disposables used to dispose of any observables when they are no longer needed
     */
    private val disposables = CompositeDisposable()

    /**
     * @property connectionDisposable Used to initialize a request to connect to the clipboard
     */
    private lateinit var connectionDisposable: Disposable

    override fun connectToServer() {
        println("Connecting to socket...")
        connectionDisposable = clipBoardApiInstance.observeWebSocketEvent()
            .subscribe(
                { socketEvent -> handleSocketEvents(socketEvent) }, //onNext
                { error -> ClipboardMessagePayload(
                    false,
                    message = "Error occured: ${error.message}"
                ) }
            )

        disposables.add(connectionDisposable)
    }

    override fun sendTextToClipboard(text: String) = clipBoardApiInstance.sendCopiedText(CopyAction(text))

    override fun disconnectFromServer() {
        disposables.clear()
        messageObserver.onNext(
            ClipboardMessagePayload(
                true,
                message = "Connection closed successfully"
            )
        )
    }

    private fun handleSocketEvents(socketEvent: WebSocket.Event) {
        when (socketEvent) {
            is WebSocket.Event.OnConnectionOpened<*> -> {
                messageObserver.onNext(ClipboardMessagePayload(
                    true,
                    message = "Connected successfully"
                ))
            }

            is WebSocket.Event.OnMessageReceived -> {
                messageObserver.onNext(
                    ClipboardMessagePayload(
                        true,
                        message = socketEvent.message.toString()
                    )
                )
            }

            is WebSocket.Event.OnConnectionFailed-> messageObserver.onNext(ClipboardMessagePayload(false))
            else -> {
                println("Other event: $socketEvent")
            }
        }
    }
}