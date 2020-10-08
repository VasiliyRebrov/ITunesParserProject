package com.itunesparser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.data.common.SingleLiveEvent
import com.data.model.Album
import com.data.model.Track
import com.data.repository.AlbumRepository
import com.itunesparser.components.checkInternetAccess

class AlbumViewModel(application: Application) : BaseViewModel<List<Track>?>(application) {
    private val repository = AlbumRepository(application)

    private val _externalEventResponse = SingleLiveEvent<String>()
    val externalEventResponse: LiveData<String> = _externalEventResponse

    fun externalEvent(url: String) {
        _externalEventResponse.value =
            if (getApplication<Application>().checkInternetAccess()) url
            else ""
    }

    //use case
    fun loadSongs(album: Album) {
        runCoroutine {
            repository.loadTracksByAlbum(
                album,
                getApplication<Application>().checkInternetAccess()
            )
        }
    }
}