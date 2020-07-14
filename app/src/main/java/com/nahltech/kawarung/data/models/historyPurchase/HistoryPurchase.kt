package com.nahltech.kawarung.data.models.historyPurchase

import com.google.gson.annotations.SerializedName

data class HistoryPurchase(
    @SerializedName("data") val data: List<Data>
)