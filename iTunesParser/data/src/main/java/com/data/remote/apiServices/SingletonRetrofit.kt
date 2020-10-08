package com.data.remote.apiServices

import com.data.common.EXC_NOT_FOUND_MSG
import com.data.common.EXC_NO_DATA_AVAILABLE_MSG
import com.data.common.NotFoundException
import com.data.remote.BaseRemoteAlbums
import com.data.remote.BaseRemoteTracks

object SingletonRetrofit {
    private val api = Api.create()

    fun loadAlbums(term: String): BaseRemoteAlbums {
        val call = api.loadAlbums(term = term)
        val response = call.execute()
        // Хотя бы один элемент - валидный результат
        if (response.isSuccessful) response.body()?.let { if (it.resultCount > 0) return it }
        throw NotFoundException()
    }

    fun loadTracksByAlbum(collectionId: String): BaseRemoteTracks {
        val call = api.loadTracksByAlbum(id = collectionId)
        val response = call.execute()
        // Треков в источнике может не быть, даже если есть альбом
        // Первым элементом в списке треков всегда приходит альбом. Даже если треки не найдены.
        if (response.isSuccessful) response.body()?.let { if (it.resultCount > 1) return it }
        throw NotFoundException(EXC_NO_DATA_AVAILABLE_MSG)
    }
}
