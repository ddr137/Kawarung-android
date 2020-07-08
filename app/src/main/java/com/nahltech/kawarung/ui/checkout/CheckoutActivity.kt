package com.nahltech.kawarung.ui.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nahltech.kawarung.R
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        toolbarUI()
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_checkout)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_checkout.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_checkout.setNavigationOnClickListener { finish() }
    }
}