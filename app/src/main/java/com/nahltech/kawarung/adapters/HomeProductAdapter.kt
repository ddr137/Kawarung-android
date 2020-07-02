package com.nahltech.kawarung.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahltech.kawarung.R
import com.nahltech.kawarung.data.models.best_selling.Product
import com.nahltech.kawarung.ui.home.DetailProductActivity
import kotlinx.android.synthetic.main.item_product.view.*
import java.text.DecimalFormat
import java.util.*

class HomeProductAdapter(
    private var product: MutableList<Product>,
    private var context: Context
) : RecyclerView.Adapter<HomeProductAdapter.ViewHolder>(
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_product, parent, false)
        )
    }

    fun setListHomeProduct(r : List<Product>){
        product.clear()
        product.addAll(r)
        notifyDataSetChanged()
    }

    override fun getItemCount() = product.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(product[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(product : Product, context: Context){

            val localeID = Locale("in", "ID")
            val formatRupiah = DecimalFormat.getCurrencyInstance(localeID)

            when {
                product.discount.equals("0") -> {
                    itemView.discount_price_product.visibility = View.GONE
                    itemView.label_discount.visibility = View.GONE
                }
                else -> {
                    itemView.discount_price_product.visibility = View.VISIBLE
                    itemView.label_discount.visibility = View.VISIBLE
                    itemView.discount_price_product.text = formatRupiah.format(product.price!!.toDouble())
                    itemView.label_discount.text = product.discount + "%"
                }
            }

            itemView.name_product.text = product.name
            //itemView.image_product.load(bestSelling.images.size)
            itemView.price_product.text = formatRupiah.format(product.discountPrice!!.toDouble()) + "/ ${product.unit}"
            itemView.min_order.text = "Min." +  " ${product.minimumBuy}" + " ${product.unit}"
            //itemView.seller_name.text = product.seller.name
            //zzitemView.seller_address.text = product.seller.address

            itemView.setOnClickListener {
                context.startActivity(Intent(context, DetailProductActivity::class.java).apply {
                    putExtra("slug", product.slug)
                    putExtra("unit", product.unit)
                    putExtra("category", product.category.categoryName)
                    putExtra("name", product.name)
                    putExtra("min", product.minimumBuy)
                    putExtra("stock", product.stock)
                    putExtra("viewer", product.reviewCount)
                    putExtra("created", product.createdAt)
                    putExtra("update", product.updatedAt)
                    putExtra("description", product.description)
                    putExtra("discount", product.discount)
                    putExtra("discount_price", product.discountPrice)
                    putExtra("price", product.price)
                })
            }
        }
    }

}