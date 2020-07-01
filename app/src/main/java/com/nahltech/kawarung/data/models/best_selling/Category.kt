package com.nahltech.kawarung.data.models.best_selling

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("category_id") var categoryId: String? = null,
    @SerializedName("category_name") var categoryName: String? = null
)