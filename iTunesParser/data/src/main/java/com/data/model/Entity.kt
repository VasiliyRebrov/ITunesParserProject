package com.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey
    val trackId: String,
    val collectionId: String,
    val trackName: String,
    val trackNumber: Int,
    val discNumber: Int,
    val trackTimeMillis: Int,
    val wrapperType: String
)

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey
    val collectionId: String,
    val collectionName: String,
    val collectionExplicitness: String,
    val collectionPrice: Double,
    val currency: String,
    val collectionViewUrl: String,
    val artworkUrl100: String,
    val trackCount: String,
    val primaryGenreName: String,
    val artistName: String,
    val artistViewUrl: String,
    val releaseDate: String
)
