package com.nahltech.kawarung.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.nahltech.kawarung.R
import com.nahltech.kawarung.auth.LoginActivity
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.dialog_buy_product.*
import java.text.DecimalFormat
import java.util.*

class DetailProductActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        toolbarUI()
        fillDetailProduct()
        btn_but_product_detail.setOnClickListener {
            if (Constants.getToken(this@DetailProductActivity) == "undefined"
                && Constants.getIdUser(this@DetailProductActivity) == "undefined"
            ) {
                Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                })
            } else {
                getCustomLayoutDialog(R.layout.dialog_buy_product, R.color.colorPrimary)
            }
        }
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_detail_product)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_detail_product.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_detail_product.setNavigationOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    private fun fillDetailProduct() {
        val tv: ExpandableTextView = findViewById(R.id.expand_text_view)
        val localeID = Locale("in", "ID")
        val formatRupiah = DecimalFormat.getCurrencyInstance(localeID)

        if (intent.getStringExtra("discount") == "0") {
            discount_product_detail.visibility = View.GONE
            discount_price_strike_detail.visibility = View.GONE
            category_product_detail.text = intent.getStringExtra("category")
            name_product_detail.text = intent.getStringExtra("name")
            min_buy_product_detail.text =
                "Minimal Beli ${intent.getStringExtra("min")} ${intent.getStringExtra("unit")} "
            stock_product_detail.text =
                "${intent.getStringExtra("stock")} ${intent.getStringExtra("unit")} "
            viewer_product_detail.text = intent.getStringExtra("viewer")
            create_product_detail.text = intent.getStringExtra("created")
            update_product_detail.text = intent.getStringExtra("update")
            tv.text = intent.getStringExtra("description")
            price_product_detail.text = formatRupiah.format(
                intent.getStringExtra("price")!!.toDouble()
            ) + "/ ${intent.getStringExtra("unit")}"
        } else {
            discount_product_detail.visibility = View.VISIBLE
            discount_price_strike_detail.visibility = View.VISIBLE
            discount_product_detail.text = "${intent.getStringExtra("discount")}%"
            discount_price_strike_detail.text =
                formatRupiah.format(intent.getStringExtra("price")!!.toDouble())
            price_product_detail.text = formatRupiah.format(
                intent.getStringExtra("discount_price")!!.toDouble()
            ) + "/ ${intent.getStringExtra("unit")}"
            category_product_detail.text = intent.getStringExtra("category")
            name_product_detail.text = intent.getStringExtra("name")
            min_buy_product_detail.text =
                "Minimal Beli ${intent.getStringExtra("min")} ${intent.getStringExtra("unit")} "
            stock_product_detail.text =
                "${intent.getStringExtra("stock")} ${intent.getStringExtra("unit")} "
            viewer_product_detail.text = intent.getStringExtra("viewer")
            create_product_detail.text = intent.getStringExtra("created")
            update_product_detail.text = intent.getStringExtra("update")
            tv.text = intent.getStringExtra("description")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCustomLayoutDialog(layoutId: Int, colorId: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutId)

        val lp = WindowManager.LayoutParams()
        if (dialog.window != null) {

            lp.copyFrom(dialog.window?.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            val unitProduct = dialog.findViewById<TextView>(R.id.unit_popup)
            val buttonAddToCart = dialog.findViewById<Button>(R.id.add_to_cart)
            val edtQty = dialog.findViewById<EditText>(R.id.edt_qty_popup)

            unitProduct.text = "/ " + intent.getStringExtra("unit")
            setupViewModel()
            buttonAddToCart.setOnClickListener {
                val id = Constants.getIdUser(this)
                val token = Constants.getToken(this)
                val productId = intent.getStringExtra("product_id").toString().trim()
                val qty = edtQty.text.toString().trim()
                if (homeViewModel.validateBuyProduct(productId, qty)) {
                    homeViewModel.buyProduct(id, token, productId, qty)
                }
            }
            dialog.show()
            dialog.window?.attributes = lp
        }
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    private fun handleUIState(it: HomeState) {
        when (it) {
            is HomeState.Error -> {
                isLoading(false)
                toast(it.err)
            }

            is HomeState.ShowToast -> toast(it.message)
            is HomeState.ValidateBuyProduct -> {
                it.qty?.let {
                    setPasswordError(it)
                }
            }
            is HomeState.IsSuccess -> {
                toast("Berhasil dimasukan ke keranjang")
            }
            is HomeState.IsLoading -> isLoading(it.state)
        }
    }

    private fun setPasswordError(err: String?) {
        layout_input_qty_buy_product.error = err
    }

    private fun isLoading(state: Boolean) {
        if (state) {

        } else {

        }
    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()


}