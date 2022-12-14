package com.yeji.flowpractice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewViewModel : ViewModel() {
    private val _sharedFlow = MutableSharedFlow<String?>()
    val sharedFlow = _sharedFlow.asSharedFlow()
    suspend fun setSharedFlow(string: String?) { _sharedFlow.emit(string)}

    /**
     * comments:
     * [channel을 flow로 변환하여 데이터 방출 시] 다른 coroutinescope를 사용해도 괜찮음
     */
    private val _channel = Channel<String?>()
    val channel = _channel.receiveAsFlow() // channel을 flow로 변환하여 사용
    suspend fun setChannel(string: String?) { _channel.send(string) }

    private val _stateFlow = MutableStateFlow<String?>(null)
    val stateFlow = _stateFlow.asStateFlow()
    fun setStateFlow(string: String?) { _stateFlow.value = string }


    fun setNewValues(str: String?){
        viewModelScope.launch {
            // delay(1000) // 시간을 좀 주면 흘려보내는 타이밍을 늦출 수 있다
            setSharedFlow(str)
            setChannel(str)
            setStateFlow(str)
        }
    }
}