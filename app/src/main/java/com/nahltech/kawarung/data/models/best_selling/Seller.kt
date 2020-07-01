package com.nahltech.kawarung.data.models.best_selling

import com.google.gson.annotations.SerializedName

data class Seller (
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("last_logout") var lastLogout: String? = null,
    @SerializedName("image") var image: String? = null
)