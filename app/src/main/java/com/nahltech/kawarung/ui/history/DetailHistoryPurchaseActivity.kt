package com.nahltech.kawarung.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.CheckoutProductAdapter
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.activity_detail_history_purchase.*
import java.text.DecimalFormat
import java.util.*

class DetailHistoryPurchaseActivity : AppCompatActivity() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history_purchase)
        toolbarUI()
        setupViewModel()
        setupRecycler()
    }

    private fun setupRecycler() {
        rv_detail_history_purchase.apply {
            layoutManager = LinearLayoutManager(this@DetailHistoryPurchaseActivity)
            adapter = CheckoutProductAdapter(mutableListOf(), context)
        }
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.fetchProductPurchase(
            Constants.getToken(this),
            Constants.getIdUser(this),
            intent.getStringExtra("id_purchase")!!.toString()
        )
        historyViewModel.fetchDetailHistoryPurchase(
            Constants.getToken(this),
            Constants.getIdUser(this),
            intent.getStringExtra("id_purchase")!!.toString()
        )
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_history_detail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_history_detail.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_history_detail.setNavigationOnClickListener { finish() }
    }

    private fun setupViewModel() {
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        historyViewModel.getDetailHistoryPurchase().observe(this, Observer {
            fill(it)
        })
        historyViewModel.getProductDetailPurchase().observe(this, Observer {
            rv_detail_history_purchase.adapter?.let { adapter ->
                if (adapter is CheckoutProductAdapter) {
                    adapter.setListProductCheckout(it)
                }
            }
        })

        historyViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun fill(data: com.nahltech.kawarung.data.models.historyPurchase.detailHistoryPurchase.Data) {
        val localeID = Locale("in", "ID")
        val formatRupiah = DecimalFormat.getCurrencyInstance(localeID)

        when (data.order_status) {
            "shipping" -> {
                ll_detail_history_purchase.visibility = View.VISIBLE
                pending_shipment_detail_purchase.visibility = View.GONE
                confirm.visibility = View.GONE
                status_detail_history_purchase.text = "Status: Sedang Menghitung Ongkir"
            }
            "confirm" -> {
                ll_detail_history_purchase.visibility = View.VISIBLE
                pending_shipment_detail_purchase.visibility = View.GONE
                confirm.visibility = View.VISIBLE
                status_detail_history_purchase.text = "Status: Menunggu Pembayaran"
                no_invoice_detail_purchase.text =
                    HtmlCompat.fromHtml(
                        "Terimakasih telah berbelanja di Kawarung. Transaksi anda dengan invoice " + "<b>" + data.invoice + "</b> " + "telah dicatat",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
            }
            "in_process" -> {
                ll_detail_history_purchase.visibility = View.VISIBLE
                pending_shipment_detail_purchase.visibility = View.GONE
                confirm.visibility = View.GONE
                status_detail_history_purchase.text = "Status: Menunggu Konfirmasi Admin"
            }
            "pending_shipment" -> {
                ll_detail_history_purchase.visibility = View.VISIBLE
                pending_shipment_detail_purchase.visibility = View.VISIBLE
                confirm.visibility = View.GONE
                status_detail_history_purchase.text = "Status: Proses Pengiriman"
            }
        }

        deadline_invoice.text = data.payment_date
        total_payment_detail_history_purchase.text =
            formatRupiah.format(data.total.toDouble())
        qty_detail_history_purchase.text = data.qty
        price_sub_total_history_purchase.text =
            formatRupiah.format(data.subtotal.toDouble())
        price_postal_fee_history_purchase.text =
            formatRupiah.format(data.shipping_costs.toDouble())
        price_discount_history_purchase.text =
            formatRupiah.format(data.total_discount.toDouble())
        total_checkout_history_purchase.text = formatRupiah.format(data.total.toDouble())
        no_invoice.text = HtmlCompat.fromHtml(
            "Invoice: " + "<b>" + data.invoice + "</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        address_detail_purchase.text = data.address.address
        note_detail_history_purchase.text = data.note
    }

    private fun handleUIState(it: HistoryState) {
        when (it) {
            is HistoryState.IsLoading -> isLoading(it.state)
            is HistoryState.Error -> {
                toast(it.err)
                isLoading(false)
            }
            is HistoryState.Failed -> {
                toast(it.message)
                isLoading(false)
            }
            is HistoryState.ShowToast -> toast(it.message)
            is HistoryState.IsSuccess -> {
                toast(it.what.toString())
            }
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            pg_loading.visibility = View.VISIBLE
            sv_detail_purchase.visibility = View.GONE
        } else {
            pg_loading.visibility = View.GONE
            sv_detail_purchase.visibility = View.VISIBLE
        }
    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}