package com.itunesparser.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.common.EXC_DEFAULT_MSG
import com.data.common.EXC_NO_NETWORK_MSG
import com.data.common.NoNetworkException
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.itunesparser.R
import com.itunesparser.components.*
import com.itunesparser.components.adapters.RecyclerTracksAdapter
import com.itunesparser.databinding.ActivityAlbumBinding
import com.itunesparser.viewmodel.AlbumViewModel
import kotlinx.android.synthetic.main.activity_album.*
import kotlin.math.abs


class AlbumActivity : AppCompatActivity() {
    private val viewModel: AlbumViewModel by viewModels()
    private val album by lazy { (intent.getStringArrayListExtra(EXTRA_DATA)!!.createAlbum()) }
    private var appBarExpanded = true
    private var collapsedMenu: Menu? = null

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (collapsedMenu != null && (!appBarExpanded || collapsedMenu!!.size() != 1)) {
            collapsedMenu!!.add(R.string.external_link_action)
                .setIcon(R.drawable.open_in_new)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        return super.onPrepareOptionsMenu(collapsedMenu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_album, menu)
        collapsedMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId == android.R.id.home -> {
                finish()
                true
            }
            item.title == resources.getString(R.string.external_link_action) -> {
                viewModel.externalEvent(album.artistViewUrl)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initBinding()
        initObservation()
        initHeaderImage()
        initToolbar()
        initAppBar()
        initRecycler()
        setContent()
        viewModel.loadSongs(album)
    }

    private fun initBinding() {
        val binding: ActivityAlbumBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_album)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
    }

    private fun initObservation() {
        viewModel.externalEventResponse.observe(this, Observer {
            if (it.isNotEmpty())
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(album.collectionViewUrl)))
            else
                root_album.snack(EXC_NO_NETWORK_MSG, Snackbar.LENGTH_LONG)
        })
        viewModel.correspondence.observe(this, Observer { it ->
            it.data?.let {
                with((rv_album_tracks.adapter as RecyclerTracksAdapter)) {
                    tracks.addAll(it)
                    notifyDataSetChanged()
                }
            }
            it.exception?.let {
                when (it) {
                    is NoNetworkException -> {
                        root_album.snack(it.message!!, Snackbar.LENGTH_INDEFINITE) {
                            this.action(RETRY_BUTTON, null) { viewModel.loadSongs(album) }
                        }
                    }
                    else -> {
                        root_album.snack(it.message ?: EXC_DEFAULT_MSG, Snackbar.LENGTH_LONG)
                    }
                }
            }
        })
    }

    private fun initHeaderImage() {
        loadImage(album.artworkUrl100, image_album_header)
    }

    private fun initToolbar() {
        setSupportActionBar(tb_album)
        (this as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = album.collectionName
        }
    }

    private fun initAppBar() {
        appbar_album.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            appBarExpanded = abs(verticalOffset) <= 200
            invalidateOptionsMenu()
        })
        fab_album_open_in_new.setSafeOnClickListener { viewModel.externalEvent(album.artistViewUrl) }
    }

    private fun initRecycler() {
        with(rv_album_tracks) {
            this.setHasFixedSize(true)
            this.adapter = RecyclerTracksAdapter()
            this.layoutManager = LinearLayoutManager(this@AlbumActivity)
        }
    }

    private fun setContent() {
        with(but_album_artist_name) {
            text = toHyperText(album.artistName)
            setSafeOnClickListener { viewModel.externalEvent(album.artistViewUrl) }
        }
        image_album_explicit_status.visibility =
            if (album.collectionExplicitness == EXPLICIT_STATUS) View.VISIBLE else View.INVISIBLE

        text_album_description.text = resources.getString(
            R.string.album_description, convertTime(album.releaseDate),
            TEXT_SEPARATOR, album.primaryGenreName
        )
        but_album_price.text = resources.getString(
            R.string.album_price, album.collectionPrice.toString(),
            album.currency
        )
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}