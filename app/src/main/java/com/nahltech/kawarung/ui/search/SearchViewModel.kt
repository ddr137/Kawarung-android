package com.nahltech.kawarung.ui.search

import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.best_selling.Product
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent
import com.nahltech.kawarung.utils.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel: ViewModel() {
    private var state: SingleLiveEvent<SearchState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun login(term: String) {
        state.value = SearchState.IsLoading(true)
        api.searchProduct(term).enqueue(object : Callback<WrappedListResponse<Product>> {
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                println(t.message)
                state.value = SearchState.Error(t.message)
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Product>>,
                response: Response<WrappedListResponse<Product>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedListResponse<Product>
                    if (body.status.equals("1")) {
                        state.value = SearchState.Success(

                            1)
                    } else {
                        state.value = SearchState.Failed("Login gagal")
                    }
                } else {
                    state.value = SearchState.Error("Email atau Password salah")
                }
                state.value = SearchState.IsLoading(false)
            }
        })
    }
    fun getState() = state
}

sealed class SearchState {
    data class Error(var err: String?) : SearchState()
    data class ShowToast(var message: String) : SearchState()
    data class IsLoading(var state: Boolean) : SearchState()
    data class Success(var what: Int? = null) : SearchState()
    data class IsSuccessRegister(var what: Int? = null) : SearchState()
    data class Failed(var message: String) : SearchState()
    object Reset : SearchState()
}