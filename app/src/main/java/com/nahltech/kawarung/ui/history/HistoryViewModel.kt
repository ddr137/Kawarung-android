package com.nahltech.kawarung.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.cart.Cart
import com.nahltech.kawarung.data.models.cart.DataX
import com.nahltech.kawarung.data.models.historyPurchase.HistoryPurchase
import com.nahltech.kawarung.data.models.historyPurchase.detailHistoryPurchase.Data
import com.nahltech.kawarung.data.models.historyPurchase.detailHistoryPurchase.DetailHistoryPurchase
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    private var api = ApiClient.instance()
    private var historyPurchase = MutableLiveData<List<com.nahltech.kawarung.data.models.historyPurchase.Data>>()
    private var detailHistoryPurchase = MutableLiveData<Data>()
    private var productPurchase = MutableLiveData<List<DataX>>()
    private var state: SingleLiveEvent<HistoryState> = SingleLiveEvent()

    fun fetchHistoryPurchase(id_user: String, token: String) {
        state.value = HistoryState.IsLoading(true)
        api.historyPurchase(id_user, token).enqueue(object :
            Callback<HistoryPurchase> {
            override fun onFailure(
                call: Call<HistoryPurchase>,
                t: Throwable
            ) {
                state.value = HistoryState.Error(t.message)
            }

            override fun onResponse(
                call: Call<HistoryPurchase>,
                response: Response<HistoryPurchase>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as HistoryPurchase
                    if (response.code() == 200) {
                        val r = body.data
                        historyPurchase.postValue(r)
                        state.value = HistoryState.IsSuccess(200)
                    }
                } else {
                    state.value =
                        HistoryState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = HistoryState.IsLoading(false)
            }
        })
    }

    fun fetchDetailHistoryPurchase(token: String, user_id: String, id_purchase: String) {
        state.value = HistoryState.IsLoading(true)
        api.detailHistoryPurchase(token, user_id, id_purchase)
            .enqueue(object : Callback<DetailHistoryPurchase> {
                override fun onFailure(call: Call<DetailHistoryPurchase>, t: Throwable) {
                    println(t.message)
                    state.value = HistoryState.Error(t.message)
                }

                override fun onResponse(
                    call: Call<DetailHistoryPurchase>,
                    response: Response<DetailHistoryPurchase>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body() as DetailHistoryPurchase
                        if (response.code() == 200) {
                            val r = body.data
                            detailHistoryPurchase.postValue(r)
                        }
                    } else {
                        state.value = HistoryState.Failed("Gagal mendapatkan response dari server")
                    }
                    state.value = HistoryState.IsLoading(false)
                }
            })
    }

    fun fetchProductPurchase(token: String, user_id: String, id_purchase: String) {
        state.value = HistoryState.IsLoading(true)
        api.detailHistoryProductPurchase(token, user_id, id_purchase).enqueue(object :
            Callback<Cart> {
            override fun onFailure(
                call: Call<Cart>,
                t: Throwable
            ) {
                state.value = HistoryState.Error(t.message)
            }

            override fun onResponse(
                call: Call<Cart>,
                response: Response<Cart>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as Cart
                    if (response.code() == 200) {
                        val r = body.data.orderProducts.data
                        productPurchase.postValue(r)
                    }
                } else {
                    state.value = HistoryState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = HistoryState.IsLoading(false)
            }
        })
    }

    fun getProductDetailPurchase() = productPurchase
    fun getHistoryPurchase() = historyPurchase
    fun getDetailHistoryPurchase() = detailHistoryPurchase
    fun getState() = state
}

sealed class HistoryState {
    data class ShowToast(var message: String) : HistoryState()
    data class IsLoading(var state: Boolean = false) : HistoryState()
    data class Error(var err: String?) : HistoryState()
    data class Failed(var message: String) : HistoryState()
    data class IsSuccess(var what: Int? = null) : HistoryState()
}