package com.nahltech.kawarung.adapters.history

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nahltech.kawarung.R
import com.nahltech.kawarung.data.models.historyPurchase.DataX
import kotlinx.android.synthetic.main.item_list_product_checkout.view.*
import java.text.DecimalFormat
import java.util.*


class SubItemHistory internal constructor(
    private val subItemList: List<DataX>,
    private var context: Context
) : RecyclerView.Adapter<SubItemHistory.SubItemViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SubItemViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_list_product_checkout, viewGroup, false)
        return SubItemViewHolder(view)
    }

    override fun onBindViewHolder(subItemViewHolder: SubItemViewHolder, i: Int) {
        subItemViewHolder.bind(subItemList[i], context)
    }

    override fun getItemCount(): Int {
        return subItemList.size
    }

    inner class SubItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(dataX: DataX, context: Context) {
            val localeID = Locale("in", "ID")
            val formatRupiah = DecimalFormat.getCurrencyInstance(localeID)

            itemView.title_product_cart.text = dataX.name
            itemView.img_cart.load("https://warunkkita.com/images/product/${dataX.product_id}/${dataX.image}")
            itemView.price_product_cart.text =
                formatRupiah.format(dataX.discount_price.toDouble())
            itemView.total_qty_cart.text = "Jumlah: ${dataX.qty}"

            when (dataX.discount) {
                "0" -> {
                    itemView.price_strikeThrough.visibility = View.GONE
                    itemView.label_discount_cart.visibility = View.GONE
                }
                else -> {
                    itemView.price_strikeThrough.visibility = View.VISIBLE
                    itemView.label_discount_cart.visibility = View.VISIBLE
                    itemView.price_strikeThrough.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    itemView.price_strikeThrough.text =
                        formatRupiah.format(dataX.price.toDouble())
                    itemView.label_discount_cart.text = dataX.discount + "%"
                }
            }
        }
    }

}