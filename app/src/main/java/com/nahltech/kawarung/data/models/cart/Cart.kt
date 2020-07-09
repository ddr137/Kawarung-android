package com.nahltech.kawarung.data.models.cart

import com.google.gson.annotations.SerializedName

data class Cart<T>(
    @SerializedName("data") val `data`: Data<Any?>
)