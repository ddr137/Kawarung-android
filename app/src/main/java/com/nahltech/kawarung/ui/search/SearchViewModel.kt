package com.nahltech.kawarung.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.best_selling.Product
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent
import com.nahltech.kawarung.utils.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel: ViewModel() {
    private var search = MutableLiveData<List<Product>>()
    private var state: SingleLiveEvent<SearchState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun fetchSearch(term: String, filter: String) {
//        state.value = SearchState.IsLoading(true)
        api.searchProduct(term, filter).enqueue(object : Callback<WrappedListResponse<Product>> {
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                state.value = SearchState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if(response.isSuccessful){

                    if(response.code() == 200){
                        val body = response.body() as WrappedListResponse<Product>
                        val r = body.data
                        search.postValue(r)

                        Log.d("Search", r.toString())
                    } else if (response.code() == 204) {
                        state.value = SearchState.Failed("Tidak ada barang yang dicari")
                    }
                }else{
                    state.value = SearchState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
//                state.value = SearchState.IsLoading(false)
            }
        })
    }
    fun getSearch() = search
    fun getState() = state
}

sealed class SearchState {
    data class Error(var err: String?) : SearchState()
    data class ShowToast(var message: String) : SearchState()
    //    data class IsLoading(var state: Boolean) : SearchState()
    data class Success(var what: Int? = null) : SearchState()
    data class IsSuccessRegister(var what: Int? = null) : SearchState()
    data class Failed(var message: String) : SearchState()
    object Reset : SearchState()
}