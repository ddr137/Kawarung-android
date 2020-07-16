package com.nahltech.kawarung.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahltech.kawarung.R
import com.nahltech.kawarung.data.models.historyStatus.Data
import kotlinx.android.synthetic.main.item_list_history_status.view.*

class HistoryStatusAdapter(
    private var history: MutableList<Data>,
    private var context: Context
) : RecyclerView.Adapter<HistoryStatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_history_status, parent, false)
        )
    }

    fun setListHistoryStatus(r: List<Data>) {
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
            when (history.status) {
                "in_process" -> {
                    itemView.title_history_status.text = "Menunggu Konfirmasi Admin"
                }
                "shipping" -> {
                    itemView.title_history_status.text = "Sedang Menghitung Ongkir"
                }
                "pending" -> {
                    itemView.title_history_status.text = "Pending"
                }
                "reject_by_admin" -> {
                    itemView.title_history_status.text = "Pesanan Tidak Disetujui Oleh Admin"
                }
                "pending_shipment" -> {
                    itemView.title_history_status.text = "Proses Pengiriman"
                }
                "finish" -> {
                    itemView.title_history_status.text = "Pemesanan Selesai"
                }
                "return" -> {
                    itemView.title_history_status.text = "Pemesanan Ditolak Pembeli"
                }
                "refund" -> {
                    itemView.title_history_status.text = "Pemesanan Tidak Sesuai, Uang Telah Dikembalikan Ke Pembeli"
                }
                "cancel" -> {
                    itemView.title_history_status.text = "Pemesanan Dibatalkan"
                }
                "confirm" -> {
                    itemView.title_history_status.text = "Menunggu Konfirmasi Pembayaran"
                }
            }
            itemView.date_history_status.text = history.created_at
        }
    }
}
