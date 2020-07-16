package com.nahltech.kawarung.data.models.historyPurchase.detailHistoryPurchase

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("address") val address: String,
    @SerializedName("address_type") val address_type: Any,
    @SerializedName("city_id") val city_id: String,
    @SerializedName("district_id") val district_id: String,
    @SerializedName("postal_code") val postal_code: String,
    @SerializedName("province_id") val province_id: String,
    @SerializedName("village_id") val village_id: String
)