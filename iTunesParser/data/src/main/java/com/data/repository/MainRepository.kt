package com.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.data.common.ResponseStatusEnum
import com.data.common.NoNetworkException
import com.data.common.ResponseStatusEnum.*
import com.data.model.Album
import com.data.model.AppDataBase
import com.data.remote.apiServices.SingletonRetrofit
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

class MainRepository(application: Application) {
    private val dao = AppDataBase.getInstance(application).dao()

    private val _albumsResult = MutableLiveData<List<Album>>()
    val albumsResult: LiveData<List<Album>> = _albumsResult

    private val _responseStatus = MutableLiveData<ResponseStatusEnum>()
    val responseStatus: LiveData<ResponseStatusEnum> = _responseStatus

    init {
        runBlocking { withContext(Dispatchers.IO) { loadLocalAlbums() } }
    }

    suspend fun loadAlbums(inputText: String, isConnected: Boolean) {
        if (inputText.isNotBlank()) {
            if (isConnected) loadRemoteAlbums(inputText)
            else {
                loadLocalAlbums()
                throw NoNetworkException()
            }
        } else loadLocalAlbums()
    }

    private suspend fun loadRemoteAlbums(inputText: String) {
        try {
            delay(100)
            val albums = SingletonRetrofit.loadAlbums(inputText).results
            delay(100)
            setValues(albums, REMOTE_LIST)
        } catch (exc: Exception) {
            loadLocalAlbums()
            throw exc
        }
    }

    private fun loadLocalAlbums() {
        val albums = dao.getAlbums()
        val status = if (albums.isNotEmpty()) LOCAL_LIST else EMPTY
        setValues(albums, status)
    }

    fun clearData() {
        dao.clearData()
        if (responseStatus.value == LOCAL_LIST) setValues(listOf(), EMPTY)
    }

    private fun setValues(albums: List<Album>, status: ResponseStatusEnum) {
        _albumsResult.postValue(albums.sortedBy { it.collectionName.toLowerCase(Locale.ROOT) })
        _responseStatus.postValue(status)
    }
}
