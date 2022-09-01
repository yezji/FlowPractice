package com.yeji.flowpractice

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class NewViewModel : ViewModel() {
    private val _sharedFlow = MutableSharedFlow<String?>(1)
    val sharedFlow = _sharedFlow.asSharedFlow()
    suspend fun setSharedFlow(string: String?) { _sharedFlow.emit(string)}

    /**
     * comments:
     * [channel을 flow로 변환하여 데이터 방출 시] 다른 coroutinescope를 사용해도 괜찮음
     */
    private val _channel = Channel<String?>(10)
    val channel = _channel.receiveAsFlow() // channel을 flow로 변환하여 사용
    suspend fun setChannel(string: String?) { _channel.send(string) }

    private val _stateFlow = MutableStateFlow<String?>(null)
    val stateFlow = _stateFlow.asStateFlow()
    fun setStateFlow(string: String?) { _stateFlow.value = string }

}