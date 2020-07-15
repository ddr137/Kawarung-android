package com.nahltech.kawarung.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.CartProductAdapter
import com.nahltech.kawarung.data.models.cart.Data
import com.nahltech.kawarung.ui.checkout.CheckoutActivity
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.activity_cart.*
import java.text.DecimalFormat
import java.util.*

class CartActivity : AppCompatActivity() {
    private lateinit var cartViewModel: CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbarUI()
        setupRecycler()
        setupViewModel()

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

        cartViewModel.getSubTotalCart().observe(this, Observer {
            fill(it)
        })

        cartViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    private fun fill(data: Data) {
        val localeID = Locale("in", "ID")
        val formatRupiah = DecimalFormat.getCurrencyInstance(localeID)
        sub_total_cart.text = formatRupiah.format(data.subTotal.toString().toDouble())

        btn_buy_cart.setOnClickListener {
            val moveIntent = Intent(this, CheckoutActivity::class.java).apply {
                putExtra("qty", data.qty.toString())
                putExtra("total_discount", data.total_discount.toString())
                putExtra("sub_total", data.subTotal.toString())
                putExtra("order_id", data.id.toString())
            }
            startActivity(moveIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.fetchProductCart(Constants.getIdUser(this), Constants.getToken(this))
        cartViewModel.fetchSubTotalCart(Constants.getIdUser(this), Constants.getToken(this))
    }

    private fun handleUIState(it: CartState) {
        when (it) {
            is CartState.IsLoading -> isLoading(it.state)
            is CartState.Error -> {
                state_empty_cart.visibility = View.VISIBLE
                //toast(it.err)
                isLoading(false)
            }
            is CartState.IsSuccess -> {
                //toast()
            }
            is CartState.IsEmpty -> {
                state_empty_cart.visibility = View.VISIBLE
            }
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            rv_cart.visibility = View.GONE
            rl_cart.visibility = View.GONE
            sh_cart.visibility = View.VISIBLE
            sh_cart.startShimmerAnimation()
        } else {
            rv_cart.visibility = View.VISIBLE
            rl_cart.visibility = View.VISIBLE
            sh_cart.visibility = View.GONE
            sh_cart.stopShimmerAnimation()
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