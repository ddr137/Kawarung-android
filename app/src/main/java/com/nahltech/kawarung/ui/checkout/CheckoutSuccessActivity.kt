package com.nahltech.kawarung.ui.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nahltech.kawarung.R
import kotlinx.android.synthetic.main.activity_checkout_success.*

class CheckoutSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)
        toolbarUI()
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_checkout_success)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_checkout_success.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_checkout_success.setNavigationOnClickListener { finish() }
    }
}