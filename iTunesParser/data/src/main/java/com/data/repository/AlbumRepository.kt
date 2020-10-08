package com.data.repository

import android.app.Application
import com.data.common.NoNetworkException
import com.data.common.VALID_TRACK_WRAPPER_TYPE
import com.data.model.Album
import com.data.model.AppDataBase
import com.data.model.Track
import com.data.remote.apiServices.SingletonRetrofit

class AlbumRepository(application: Application) {
    private val dao = AppDataBase.getInstance(application).dao()

    fun loadTracksByAlbum(album: Album, isConnected: Boolean): List<Track> {
        if (isConnected) {
            val tracks = SingletonRetrofit.loadTracksByAlbum(album.collectionId)
                .results.filter { it.wrapperType == VALID_TRACK_WRAPPER_TYPE }
            dao.insertData(album, tracks)
        }
        return with(dao.getTracks(album.collectionId)) {
            if (isEmpty()) throw NoNetworkException()
            sortedWith(compareBy(Track::discNumber, Track::trackId))
        }
    }
}