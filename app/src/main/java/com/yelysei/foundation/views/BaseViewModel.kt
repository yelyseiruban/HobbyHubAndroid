package com.yelysei.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yelysei.foundation.model.ErrorResult
import com.yelysei.foundation.model.PendingResult
import com.yelysei.foundation.model.Result
import com.yelysei.foundation.model.SuccessResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>

open class BaseViewModel: ViewModel() {

    private val coroutineContext = SupervisorJob() +
            Dispatchers.Main.immediate
    protected val viewModelScope: CoroutineScope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        clearViewModelScope()
    }

    open fun onResult(result: Any) {

    }

    open fun onBackPressed(): Boolean {
        clearViewModelScope()
        return false
    }

    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        liveResult.value = PendingResult()
        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResult(block()))
            } catch (e: Exception) {
                liveResult.postValue(ErrorResult(e))
            }
        }
    }

    private fun clearViewModelScope() {
        viewModelScope.cancel()
    }
}