package com.nahltech.kawarung.ui.cart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.CartProductAdapter
import com.nahltech.kawarung.ui.checkout.CheckoutActivity
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {
    private lateinit var cartViewModel: CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbarUI()
        setupRecycler()
        setupViewModel()
        btn_buy_cart.setOnClickListener {
            val moveIntent = Intent(this, CheckoutActivity::class.java)
            startActivity(moveIntent)
        }
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_cart)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_cart.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_cart.setNavigationOnClickListener { finish() }
    }

    private fun setupViewModel() {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.getProductCart().observe(this, Observer {
            rv_cart.adapter?.let { adapter ->
                if (adapter is CartProductAdapter) {
                    adapter.setListProductCart(it)
                }
            }
        })

        cartViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.fetchProductCart(Constants.getIdUser(this), Constants.getToken(this))
    }

    private fun handleUIState(it: CartState) {
        when (it) {
            is CartState.IsLoading -> isLoading(it.state)
            is CartState.Error -> {
                toast(it.err)
                isLoading(false)
            }
            is CartState.IsSuccess -> {
                //toast()
            }
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {

        } else {

        }
    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun setupRecycler() {
        rv_cart.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = CartProductAdapter(mutableListOf(), context)
        }
    }
}