package com.nahltech.kawarung.data.models.cart

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category_name") val category_name: String? = null,
    @SerializedName("discount") val discount: String? = null,
    @SerializedName("discount_price") val discount_price: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("image_id") val image_id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("product_id") val product_id: String? = null,
    @SerializedName("qty") val qty: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("stock") val stock: String? = null,
    @SerializedName("subtotal") val subtotal: String? = null,
    @SerializedName("unit") val unit: String? = null
)