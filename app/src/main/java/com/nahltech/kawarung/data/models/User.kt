package com.nahltech.kawarung.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("shopping_count") var shoppingCount: String? = null,
    @SerializedName("agent") var agent: String? = null,
    @SerializedName("api_token") var api_token: String? = null,
    @SerializedName("registered") var registered: String? = null
)