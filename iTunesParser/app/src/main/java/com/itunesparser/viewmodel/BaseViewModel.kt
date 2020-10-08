package com.itunesparser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.common.SingleLiveEvent
import com.itunesparser.components.Resource
import com.itunesparser.components.Status
import com.itunesparser.components.convertError
import kotlinx.coroutines.*

abstract class BaseViewModel<T>(application: Application) : AndroidViewModel(application) {
    var job: Job? = null

    private val _correspondence = SingleLiveEvent<Resource<T>>()
    val correspondence: LiveData<Resource<T>> = _correspondence

    private val _progress = MutableLiveData<Status>(Status.COMPLETED)
    val progress: LiveData<Status> = _progress

    //launcher
    protected fun runCoroutine(block: suspend () -> T) {
        viewModelScope.launch(Dispatchers.Main) {
            job?.cancelAndJoin()
            job = launch {
                _progress.value = Status.LOADING
                try {
                    val result = withContext(Dispatchers.IO) { block() }
                    _correspondence.value = Resource(result, null)
                } catch (exc: Exception) {
                    _correspondence.value = Resource(null, exc.convertError())
                } finally {
                    _progress.value = Status.COMPLETED
                }
            }
        }
    }
}