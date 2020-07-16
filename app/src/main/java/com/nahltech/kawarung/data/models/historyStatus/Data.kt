package com.nahltech.kawarung.data.models.historyStatus

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at") val created_at: String,
    @SerializedName("id") val id: Int,
    @SerializedName("status") val status: String,
    @SerializedName("updated_at") val updated_at: String
)