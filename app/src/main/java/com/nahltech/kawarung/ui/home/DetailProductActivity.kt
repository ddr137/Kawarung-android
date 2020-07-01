package com.nahltech.kawarung.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.nahltech.kawarung.R
import kotlinx.android.synthetic.main.activity_detail_product.*
import java.text.DecimalFormat
import java.util.*

class DetailProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        toolbarUI()
        fillDetailProduct()
        btn_but_product_detail.setOnClickListener {
            getCustomLayoutDialog(R.layout.dialog_buy_product, R.color.colorPrimary)
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
            val positiveButton = dialog.findViewById<TextView>(R.id.unit_popup)

            positiveButton.text = "/ " + intent.getStringExtra("unit")
            //linearLayout.setBackgroundColor(ContextCompat.getColor(this,colorId))

            dialog.show()
            dialog.window?.attributes = lp
        }
    }

}