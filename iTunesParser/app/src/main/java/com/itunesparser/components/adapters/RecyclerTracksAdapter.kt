package com.itunesparser.components.adapters

import com.data.model.Track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.itunesparser.R
import com.itunesparser.components.convertTime
import kotlinx.android.synthetic.main.item_song.view.*

class RecyclerTracksAdapter : RecyclerView.Adapter<RecyclerTracksAdapter.ViewHolder>() {
    val tracks = mutableListOf<Track>()

    class ViewHolder(val cont: ConstraintLayout) : RecyclerView.ViewHolder(cont)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false) as ConstraintLayout
        return ViewHolder(cardView)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cont = holder.cont
        val track = tracks[position]
        cont.text_song_item_number.text = track.trackNumber.toString()
        cont.text_song_item_name.text = track.trackName
        cont.text_song_item_duration.text = convertTime(track.trackTimeMillis)
    }
}
