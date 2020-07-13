package com.nahltech.kawarung.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nahltech.kawarung.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        toolbarUI()
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_search)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_search.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_search.setNavigationOnClickListener { finish() }
    }
}