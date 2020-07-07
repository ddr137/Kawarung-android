package com.nahltech.kawarung.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nahltech.kawarung.R
import com.nahltech.kawarung.data.models.cart.Product
import kotlinx.android.synthetic.main.item_list_cart.view.*
import java.text.DecimalFormat
import java.util.*

class CartProductAdapter(
    private var product: MutableList<com.nahltech.kawarung.data.models.cart.Product>,
    private var context: Context
) : RecyclerView.Adapter<CartProductAdapter.ViewHolder>(
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_cart, parent, false)
        )
    }

    fun setListProductCart(r: List<com.nahltech.kawarung.data.models.cart.Product>) {
        product.clear()
        product.addAll(r)
        notifyDataSetChanged()
    }

    override fun getItemCount() = product.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(product[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {

            val localeID = Locale("in", "ID")
            val formatRupiah = DecimalFormat.getCurrencyInstance(localeID)

            itemView.title_product_cart.text = product.name
            itemView.img_cart.load("https://warunkkita.com/images/product/${product.product_id}/${product.image}")
            itemView.price_product_cart.text = formatRupiah.format(product.discount_price!!.toDouble())
            itemView.total_qty_cart.text = "Jumlah: ${product.qty}" + " ${product.unit}"

            when {
                product.discount.equals("0") -> {
                    itemView.price_strikeThrough.visibility = View.GONE
                    itemView.label_discount_cart.visibility = View.GONE
                }
                else -> {
                    itemView.price_strikeThrough.visibility = View.VISIBLE
                    itemView.label_discount_cart.visibility = View.VISIBLE
                    itemView.price_strikeThrough.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    itemView.price_strikeThrough.text = formatRupiah.format(product.price!!.toDouble())
                    itemView.label_discount_cart.text = product.discount + "%"
                }
            }
        }
    }
}