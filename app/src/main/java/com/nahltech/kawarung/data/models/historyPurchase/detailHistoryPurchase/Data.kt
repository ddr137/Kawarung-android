package com.nahltech.kawarung.data.models.historyPurchase.detailHistoryPurchase

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("address") val address: Address,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("id") val id: String,
    @SerializedName("invoice") val invoice: String,
    @SerializedName("note") val note: String,
    @SerializedName("orderProducts") val orderProducts: OrderProducts,
    @SerializedName("order_status") val order_status: String,
    @SerializedName("payment_date") val payment_date: String,
    @SerializedName("payment_method") val payment_method: String,
    @SerializedName("qty") val qty: String,
    @SerializedName("shipping_costs") val shipping_costs: String,
    @SerializedName("subtotal") val subtotal: String,
    @SerializedName("total") val total: String,
    @SerializedName("total_discount") val total_discount: String,
    @SerializedName("updated_at") val updated_at: String
)