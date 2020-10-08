package com.data.remote.apiServices

import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class ApiTest {
    private val api = Api.create()
    private val albums = listOf(
        "Burzum",
        "Led Zeppelin",
        "Led Zep",
        "Blizzard of Ozz",
        "From Elvis in Memphis",
        "Владимир Высоцкий",
        "Deepest Purple",
        "Thulean Mysteries",
        "1184",
        "warheads on foreheads"
    )
    private val ids = listOf("1138094621", "286930912")

    @Test
    fun loadAlbums() {
        try {
            albums.forEach {
                println("**********\n$it")
                val call = api.loadAlbums(term = it)
                val response = call.execute()
                //check response is successful
                Assert.assertTrue(response.isSuccessful)
                //check body is exist
                Assert.assertTrue(response.body() != null)
                val body = response.body()
                println(response.raw().request().url())
                //check result is valid
                body?.let {
                    Assert.assertTrue(body.resultCount > 0)
                    println("count: ${body.resultCount}\n")
                    body.results.forEach { item -> println("$item\n") }
                }
            }
        } catch (exc: Exception) {
            println("ERROR_TEST: $exc | ${exc.message}")
        }
    }

    @Test
    fun loadSongs() {
        try {
            ids.forEach {
                println("**********\n$it")
                val call = api.loadTracksByAlbum(id = it)
                val response = call.execute()
                //check response is successful
                Assert.assertTrue(response.isSuccessful)
                //check body is exist
                Assert.assertTrue(response.body() != null)
                val body = response.body()
                //check result is valid
                body?.let {
                    Assert.assertTrue(body.resultCount > 0)
                    println("count: ${body.resultCount}\n")
                    body.results.forEach { item -> println("$item\n") }
                }
            }
        } catch (exc: Exception) {
            println("ERROR_TEST: $exc | ${exc.message}")
        }
    }
}