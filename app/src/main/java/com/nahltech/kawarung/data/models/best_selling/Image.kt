package com.nahltech.kawarung.data.models.best_selling

import com.google.gson.annotations.SerializedName

data class Image (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("image") var image: String? = null
)