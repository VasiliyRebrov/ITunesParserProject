package com.itunesparser.viewmodel

import android.app.Application
import androidx.lifecycle.Observer
import com.data.common.SingleLiveEvent
import com.data.repository.MainRepository
import com.itunesparser.components.checkInternetAccess
import java.util.*

class MainViewModel(application: Application) : BaseViewModel<Unit>(application) {
    private val repository = MainRepository(application)
    val albumsResult = repository.albumsResult
    val titleStatus = repository.responseStatus
    val inputField = SingleLiveEvent<String>()
    private val observer = Observer<String> { loadAlbums(it) }


    init {
        inputField.observeForever(observer)
    }

    //use case
    private fun loadAlbums(inputText: String) {
        runCoroutine {
            repository.loadAlbums(inputText, getApplication<Application>().checkInternetAccess())
        }
    }

    //use case
    fun clearData() {
        runCoroutine { repository.clearData() }
    }

    override fun onCleared() {
        inputField.removeObserver(observer)
        super.onCleared()
    }
}