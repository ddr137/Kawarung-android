package com.nahltech.kawarung.data.models.historyPurchase

data class Data(
    val address: Address,
    val created_at: String,
    val id: Int,
    val invoice: String,
    val note: String,
    val orderProducts: OrderProducts,
    val order_status: String,
    val payment_date: Any,
    val payment_method: String,
    val qty: String,
    val shipping_costs: String,
    val subtotal: String,
    val total: String,
    val total_discount: String,
    val updated_at: String
)