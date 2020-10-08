package com.data.model

import androidx.room.*

@Dao
interface AppDao {
    @Query("SELECT * FROM albums")
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM tracks where collectionId=:collectionId")
    fun getTracks(collectionId: String): List<Track>

    @Transaction
    fun insertData(album: Album, tracks: List<Track>) {
        insertAlbum(album)
        insertTracks(tracks)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbum(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTracks(tracks: List<Track>)

    @Transaction
    fun clearData() {
        deleteAlbums()
        deleteTracks()
    }

    @Query("delete from albums")
    fun deleteAlbums()

    @Query("delete from tracks")
    fun deleteTracks()


}