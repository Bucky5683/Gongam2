package com.cono.gongam.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopWatchViewModel : ViewModel() {
    private val _sumSeconds = MutableLiveData(0)
    private val _isStopped = MutableLiveData(true)

    val sumSeconds: LiveData<Int> get() = _sumSeconds
    val isStopped: LiveData<Boolean> get() = _isStopped

    fun setIsStopped(stopped: Boolean) {
        _isStopped.value = stopped
    }

    fun startCounting() {
        if (_isStopped.value == false) {
            viewModelScope.launch {
                while (_isStopped.value == false) {
                    delay(1000)
                    _sumSeconds.value = _sumSeconds.value?.plus(1)
                }
            }
        }
    }
}