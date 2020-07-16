package com.nahltech.kawarung.adapters.history

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nahltech.kawarung.R
import com.nahltech.kawarung.data.models.historyPurchase.Data
import com.nahltech.kawarung.ui.history.DetailHistoryPurchaseActivity
import kotlinx.android.synthetic.main.item_list_history_purchase.view.*

class HistoryPurchaseAdapter(
    private var history: MutableList<Data>,
    private var context: Context
) : RecyclerView.Adapter<HistoryPurchaseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_history_purchase, parent, false)
        )
    }

    fun setListHistoryPurchase(r: List<Data>) {
        history.clear()
        history.addAll(r)
        notifyDataSetChanged()
    }

    override fun getItemCount() = history.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(history[position], context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(history: Data, context: Context) {
            itemView.date_invoice.text = history.created_at
            itemView.no_invoice.text = HtmlCompat.fromHtml(
                "Invoice: " + "<b>" + history.invoice + "</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            when (history.order_status) {
                "confirm" -> {
                    itemView.status_history.text = "Status: Menunggu Pembayaran"
                }
                "shipping" -> {
                    itemView.status_history.text = "Status: Sedang Menghitung Ongkir"
                }
                "in_process" -> {
                    itemView.status_history.text = "Status: Menunggu Konfirmasi Admin"
                }
            }

            // Create layout manager with initial prefetch item count
            val layoutManager = LinearLayoutManager(
                itemView.rv_sub_item.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager.initialPrefetchItemCount = history.orderProducts.data.size

            // Create sub item view adapter
            val subItemAdapter = SubItemHistory(history.orderProducts.data, context)
            val viewPool = RecyclerView.RecycledViewPool()
            itemView.rv_sub_item.layoutManager = layoutManager
            itemView.rv_sub_item.adapter = subItemAdapter
            itemView.rv_sub_item.setRecycledViewPool(viewPool)

            itemView.detail_history_purchase.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        DetailHistoryPurchaseActivity::class.java
                    ).apply {
                        putExtra("id_purchase", history.id)
                    })
            }

        }
    }
}