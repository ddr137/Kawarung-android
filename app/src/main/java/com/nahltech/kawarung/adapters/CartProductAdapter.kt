package com.nahltech.kawarung.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahltech.kawarung.R
import kotlinx.android.synthetic.main.item_list_cart.view.*

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
        holder.bind(product[position], context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(product: com.nahltech.kawarung.data.models.cart.Product, context: Context) {
            itemView.title_product_cart.text = product.name
        }
    }
}