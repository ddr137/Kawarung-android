package com.nahltech.kawarung.data.models.best_selling

import com.google.gson.annotations.SerializedName

data class Product (
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("category") var category: Category,
    //@SerializedName("images") var images: List<Int>,
    @SerializedName("stock") var stock: String? = null,
    @SerializedName("unit") var unit: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("discount") var discount: String? = null,
    @SerializedName("discount_price") var discountPrice: String? = null,
    @SerializedName("minimum_buy") var minimumBuy: String? = null,
    @SerializedName("minimum_unit") var minimumUnit: String? = null,
    @SerializedName("fee") var fee: String? = null,
    @SerializedName("sold") var sold: String? = null,
    @SerializedName("review_count") var reviewCount: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
)