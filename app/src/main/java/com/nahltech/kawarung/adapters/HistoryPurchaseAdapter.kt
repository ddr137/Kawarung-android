package com.nahltech.kawarung.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahltech.kawarung.R
import com.nahltech.kawarung.data.models.historyPurchase.Data

class HistoryPurchaseAdapter(
    private var history: MutableList<Data>,
    private var context: Context
): RecyclerView.Adapter<HistoryPurchaseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryPurchaseAdapter.ViewHolder {
        return HistoryPurchaseAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_history_purchase, parent, false)
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


        }
    }
}