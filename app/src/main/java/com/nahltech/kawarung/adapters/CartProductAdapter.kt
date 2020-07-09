package com.nahltech.kawarung.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nahltech.kawarung.R
import com.nahltech.kawarung.data.models.cart.DataX
import com.nahltech.kawarung.ui.cart.CartActivity
import com.nahltech.kawarung.ui.cart.CartViewModel
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.item_list_cart.view.*
import java.text.DecimalFormat
import java.util.*


class CartProductAdapter(
    private var product: MutableList<DataX>,
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

    fun setListProductCart(r: List<DataX>) {
        product.clear()
        product.addAll(r)
        notifyDataSetChanged()
    }

    override fun getItemCount() = product.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(product[position], context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(product: DataX, context: Context) {

            val localeID = Locale("in", "ID")
            val formatRupiah = DecimalFormat.getCurrencyInstance(localeID)

            itemView.title_product_cart.text = product.name
            itemView.img_cart.load("https://warunkkita.com/images/product/${product.product_id}/${product.image}")
            itemView.price_product_cart.text =
                formatRupiah.format(product.discount_price.toDouble())
            itemView.total_qty_cart.text = "Jumlah: ${product.qty}"

            when (product.discount) {
                "0" -> {
                    itemView.price_strikeThrough.visibility = View.GONE
                    itemView.label_discount_cart.visibility = View.GONE
                }
                else -> {
                    itemView.price_strikeThrough.visibility = View.VISIBLE
                    itemView.label_discount_cart.visibility = View.VISIBLE
                    itemView.price_strikeThrough.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    itemView.price_strikeThrough.text =
                        formatRupiah.format(product.price.toDouble())
                    itemView.label_discount_cart.text = product.discount + "%"
                }
            }

            itemView.delete_product_cart.setOnClickListener {
                val cartViewModel: CartViewModel =
                    ViewModelProvider(ViewModelStoreOwner { ViewModelStore() }).get(CartViewModel::class.java)
                val idUser = Constants.getIdUser(context)
                val token = Constants.getToken(context)
                val idProduct = product.id.toString()
                cartViewModel.deleteProductCart(idUser, token, idProduct)
                Toast.makeText(context, "Berhasil di hapus", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, CartActivity::class.java).apply {
                    (context as Activity).finish()
                })
            }
        }
    }
}