package com.yeji.flowpractice

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

val externalScope = CoroutineScope(Dispatchers.Main)
class MainViewModel : ViewModel() {
    private val _sharedFlow = MutableSharedFlow<String?>(1)
    val sharedFlow = _sharedFlow.asSharedFlow()
    suspend fun setSharedFlow(string: String?) {
        _sharedFlow.emit(string)
    }


    /**
     * comments:
     * [channel에 데이터 방출 시] 같은 coroutinescope를 지정할 수 있도록 externalScope 빼주는 작업 진헹
     * -> withContext(externalScope.coroutineContext)
     */
    private val _channel = Channel<String?>()
    val channel: Channel<String?> get() = _channel
    suspend fun setChannel(string: String?) = withContext(externalScope.coroutineContext) {
        _channel.send(string)
    }

    private val _stateFlow = MutableStateFlow<String?>(null)
    val stateFlow = _stateFlow.asStateFlow()
    fun setStateFlow(string: String?) {
        _stateFlow.value = string
    }

    /**
     * comments:
     * init으로 초기화하면 null로 방출되기에 초기화 역할 X
     */
//    init {
//        viewModelScope.launch {
//            setSharedFlow(null)
//            setChannel(null)
//            setStateFlow(null)
//
//            Log.d("yezzz MainViewModel", "resultStr: ${_channel.receive() ?: ""}")
//        }
//    }
}