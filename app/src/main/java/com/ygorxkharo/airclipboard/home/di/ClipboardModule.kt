package com.ygorxkharo.airclipboard.home.di

import com.ygorxkharo.airclipboard.home.data.clipboard.ClipBoardRepository
import com.ygorxkharo.airclipboard.home.data.clipboard.ClipBoardRepositoryImpl
import com.ygorxkharo.airclipboard.home.data.socket.ClipboardConnectionProvider
import com.ygorxkharo.airclipboard.home.domain.ClipboardMessagePayload
import com.ygorxkharo.airclipboard.home.presentation.viewmodel.ClipBoardViewModel
import com.ygorxkharo.airclipboard.home.presentation.viewmodel.ClipboardViewModelImpl
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

/**
 * Dependency container for loading clipboard application dependencies
 *
 */
class ClipboardModule: KodeinModule{

    override val TAG = "clipboard_module"

    /**
     * Initialize clipboard dependencies
     * @return a module containing bindings for the clipboard UI layer
     */
    override fun initialize() = DI.Module(TAG) {

        val messageObserver: Subject<ClipboardMessagePayload> = PublishSubject.create()

        bind<ClipBoardRepository>() with provider {
            ClipBoardRepositoryImpl(messageObserver)
        }

        bind<ClipBoardViewModel>() with provider {
            val clipBoardSocketProvider: ClipboardConnectionProvider = instance()
            val clipboardRepository: ClipBoardRepository = instance()
            ClipboardViewModelImpl(clipBoardSocketProvider, clipboardRepository)
        }
    }
}
