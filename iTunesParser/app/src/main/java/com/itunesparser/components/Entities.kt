package com.itunesparser.components

data class Resource<T>(
    val data: T?,
    val exception: Exception?
)

enum class Status {
    LOADING,
    COMPLETED
}
