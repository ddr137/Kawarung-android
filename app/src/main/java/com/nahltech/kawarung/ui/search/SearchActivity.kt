package com.nahltech.kawarung.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.nahltech.kawarung.R
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbarUI()

        et_search.requestFocus()

        et_search.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!et_search.getText().toString().equals("")) {
                    val intent = Intent(this, SearchResultActivity::class.java)
                    intent.putExtra("term", et_search.text.toString())
                    startActivity(intent)
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_search)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_search.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_search.setNavigationOnClickListener { finish() }
    }
}