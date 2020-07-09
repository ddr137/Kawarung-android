package com.nahltech.kawarung.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.historyPurchase.HistoryPurchase
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel: ViewModel() {
    private var api = ApiClient.instance()
    private var historyPurchase = MutableLiveData<List<com.nahltech.kawarung.data.models.historyPurchase.Data>>()
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
                    state.value = HistoryState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = HistoryState.IsLoading(false)
            }
        })
    }

    fun getHistoryPurchase() = historyPurchase
    fun getState() = state
}

sealed class HistoryState {
    data class ShowToast(var message: String) : HistoryState()
    data class IsLoading(var state: Boolean = false) : HistoryState()
    data class Error(var err: String?) : HistoryState()
    data class Failed(var message: String) : HistoryState()
    data class IsSuccess(var what: Int? = null) : HistoryState()
}