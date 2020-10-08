package com.itunesparser.components.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.data.model.Album
import com.itunesparser.R
import com.itunesparser.components.*
import kotlinx.android.synthetic.main.item_album.view.*

class RecyclerAlbumsAdapter : RecyclerView.Adapter<RecyclerAlbumsAdapter.ViewHolder>() {
    private val albums = mutableListOf<Album>()
    var listener: Listener? = null

    interface Listener {
        fun onItemClick(album: Album)
    }

    class ViewHolder(val cont: ConstraintLayout) : RecyclerView.ViewHolder(cont)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false) as ConstraintLayout
        return ViewHolder(cardView)
    }

    override fun getItemCount() = albums.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cont = holder.cont
        val album = albums[position]
        loadImage(album.artworkUrl100, cont.image_album_item_artwork)
        cont.text_album_item_name.text = album.collectionName
        cont.text_album_item_artist_name.text = album.artistName
        cont.text_album_item_description.text = cont.resources.getString(
            R.string.item_album_description, convertTime(album.releaseDate),
            TEXT_SEPARATOR, album.trackCount
        )
        cont.image_item_explicit_status.visibility =
            if (album.collectionExplicitness == EXPLICIT_STATUS) View.VISIBLE else View.INVISIBLE
        listener?.let { listener -> cont.setSafeOnClickListener { listener.onItemClick(album) } }
    }

    fun updateList(newList: List<Album>) {
        if (newList != albums) {
            this.albums.clear()
            this.albums.addAll(newList)
            notifyDataSetChanged()
        }
    }
}
