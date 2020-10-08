package com.itunesparser.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Html
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.data.model.Album
import com.google.android.material.snackbar.Snackbar
import com.itunesparser.R
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_album.view.*
import kotlinx.coroutines.CancellationException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun Context.checkInternetAccess(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun Exception.convertError(): Exception? = if (this is CancellationException) null else this

fun toHyperText(str: String) = Html.fromHtml("<u>$str</u>") ?: str

//snackbar
inline fun View.snack(
    message: String, length: Int = Snackbar.LENGTH_SHORT, f: Snackbar.() -> Unit = {}
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

//time
fun convertTime(sourceDate: String): String {
    val sourcePattern = SimpleDateFormat(SOURCE_DATE_PATTERN, Locale.ENGLISH)
    val requiredPattern = SimpleDateFormat(REQUIRED_DATE_PATTERN, Locale.ENGLISH)
    val unix = sourcePattern.parse(sourceDate)?.time ?: return DEFAULT_PATTERN
    return requiredPattern.format(Date(unix))
}

fun convertTime(unix: Int): String {
    val requiredPattern = SimpleDateFormat(REQUIRED_TIME_PATTERN, Locale.ENGLISH)
    val date = Date(unix.toLong())
    return requiredPattern.format(date)
}

//dto converter
fun Album.toRegexList(): List<String> {
    fun Album.toRegex() =
        collectionId + REGEX_SEPARATOR + collectionName + REGEX_SEPARATOR + collectionExplicitness +
                REGEX_SEPARATOR + collectionPrice + REGEX_SEPARATOR + currency + REGEX_SEPARATOR +
                collectionViewUrl + REGEX_SEPARATOR + artworkUrl100 + REGEX_SEPARATOR + trackCount +
                REGEX_SEPARATOR + primaryGenreName + REGEX_SEPARATOR + artistName +
                REGEX_SEPARATOR + artistViewUrl + REGEX_SEPARATOR + releaseDate
    return this.toRegex().split(REGEX_SEPARATOR)
}

fun List<String>.createAlbum() = Album(
    this[0], this[1], this[2], this[3].toDouble(), this[4], this[5], this[6],
    this[7], this[8], this[9], this[10], this[11]
)

//picasso
fun loadImage(url: String, view: ImageView) {
    Picasso.get().load(url)
        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
        .placeholder(R.drawable.queue_music)
        .error(R.drawable.queue_music)
        .into(view)
}

