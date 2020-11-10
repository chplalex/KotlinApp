package com.chplalex.notesgb.ui.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel<S> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Default + Job()
    }

    @VisibleForTesting
    public override fun onCleared() {
        dataChannel.close()
        errorChannel.close()
        coroutineContext.cancel()
        super.onCleared()
    }

    private val dataChannel = BroadcastChannel<S>(Channel.CONFLATED)

    private val errorChannel = Channel<Throwable>()

    fun getDataChannel(): ReceiveChannel<S> = dataChannel.openSubscription()

    fun getErrorChannel(): ReceiveChannel<Throwable> = errorChannel

    protected fun setError(error: Throwable) = launch {
        errorChannel.send(error)
    }

    protected fun setData(data: S) = launch {
        dataChannel.send(data)
    }
}