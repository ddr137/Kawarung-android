package com.nahltech.kawarung.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.best_selling.Product
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent
import com.nahltech.kawarung.utils.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private var api = ApiClient.instance()
    private var bestSelling = MutableLiveData<List<Product>>()
    private var productNew = MutableLiveData<List<Product>>()
    private var state: SingleLiveEvent<HomeState> = SingleLiveEvent()

    fun fetchProductTrend(){
        state.value = HomeState.IsLoading(true)
        api.listProductBestSelling().enqueue(object : Callback<WrappedListResponse<Product>> {
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                state.value = HomeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedListResponse<Product>
                    if(body.status.equals("1")){
                        val r = body.data
                        bestSelling.postValue(r)
                    }
                }else{
                    state.value = HomeState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = HomeState.IsLoading(false)
            }
        })
    }
    fun fetchProductNew(){
        state.value = HomeState.IsLoading(true)
        api.listProductNew().enqueue(object : Callback<WrappedListResponse<Product>> {
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                state.value = HomeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedListResponse<Product>
                    if(body.status.equals("1")){
                        val r = body.data
                        productNew.postValue(r)
                    }
                }else{
                    state.value = HomeState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = HomeState.IsLoading(false)
            }
        })
    }

    fun getProductBestSelling() = bestSelling
    fun getProductNew() = productNew
    fun getState()  = state
}

sealed class HomeState {
    data class ShowToast(var message : String) : HomeState()
    data class IsLoading(var state : Boolean = false) : HomeState()
    data class Error(var err : String?) : HomeState()
    data class IsSuccess(var what : Int? = null) : HomeState()
}