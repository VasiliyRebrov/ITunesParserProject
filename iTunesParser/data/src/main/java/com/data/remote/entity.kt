package com.data.remote

import com.data.model.Album
import com.data.model.Track

data class BaseRemoteAlbums(val resultCount: Int, val results: List<Album>)
data class BaseRemoteTracks(val resultCount: Int, val results: List<Track>)