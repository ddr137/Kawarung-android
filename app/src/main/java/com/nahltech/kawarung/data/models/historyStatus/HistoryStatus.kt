package com.nahltech.kawarung.data.models.historyStatus

import com.google.gson.annotations.SerializedName

data class HistoryStatus(
    @SerializedName("data") val `data`: List<Data>
)