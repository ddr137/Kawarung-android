package com.nahltech.kawarung.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nahltech.kawarung.R
import kotlinx.android.synthetic.main.activity_detail_history_purchase.*

class DetailHistoryPurchaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history_purchase)
        toolbarUI()
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_history_detail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_history_detail.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_history_detail.setNavigationOnClickListener { finish() }
    }
}