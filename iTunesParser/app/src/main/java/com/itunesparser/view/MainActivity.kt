package com.itunesparser.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.model.Album
import com.itunesparser.R
import com.itunesparser.components.adapters.RecyclerAlbumsAdapter
import com.itunesparser.components.toRegexList
import com.itunesparser.databinding.ActivityMainBinding
import com.itunesparser.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var lastToastEvent: String? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                switchInputFieldFocus()
                true
            }
            R.id.action_clear_history -> {
                viewModel.clearData()
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
        initObservers()
        initBar()
        initInputField()
        initRecycler()
    }

    private fun initBinding() {
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
    }

    private fun initObservers() {
        viewModel.correspondence.observe(this, Observer {
            it.data?.let { lastToastEvent = null; return@Observer }
            it.exception?.message?.let { msg -> doToast(msg) }
        })
    }

    private fun initBar() {
        setSupportActionBar(tb_main)
        (this as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb_main.setNavigationIcon(R.drawable.search_white)
    }

    private fun initInputField() {
        et_main.setOnFocusChangeListener { _, hasFocus -> switchNavIcon(hasFocus) }
        et_main.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                switchInputFieldFocus()
                true
            } else false
        }
    }

    private fun initRecycler() {
        val adapter = RecyclerAlbumsAdapter()
        with(rv_main_albums) {
            this.setHasFixedSize(true)
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@MainActivity)
        }
        adapter.listener = object : RecyclerAlbumsAdapter.Listener {
            override fun onItemClick(album: Album) {
                startActivity(Intent(this@MainActivity, AlbumActivity::class.java).apply {
                    putExtra(AlbumActivity.EXTRA_DATA, album.toRegexList() as Serializable)
                })
            }
        }
    }


    override fun onPause() {
        if (et_main.isFocused) switchInputFieldFocus()
        super.onPause()
    }


    private fun switchInputFieldFocus() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInputFromWindow(
                et_main.applicationWindowToken,
                InputMethodManager.SHOW_IMPLICIT,
                0
            )
        with(et_main) {
            if (isFocused) clearFocus()
            else requestFocus()
        }
    }

    private fun switchNavIcon(hasFocus: Boolean) {
        tb_main.setNavigationIcon(
            if (hasFocus) R.drawable.arrow_back_ios
            else R.drawable.search_white
        )
    }

    private fun doToast(str: String) {
        if (str != lastToastEvent) {
            lastToastEvent = str
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
        }
    }
}
