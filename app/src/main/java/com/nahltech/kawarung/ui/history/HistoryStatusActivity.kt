package com.nahltech.kawarung.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nahltech.kawarung.R
import kotlinx.android.synthetic.main.activity_history_status.*

class HistoryStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_status)
        toolbarUI()
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_history_status)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_history_status.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_history_status.setNavigationOnClickListener { finish() }
    }
}