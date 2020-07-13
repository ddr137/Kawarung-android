package com.nahltech.kawarung.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.BuyProduct
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

    fun fetchProductTrend(programCategoryId: String){
        state.value = HomeState.IsLoading(true)
        api.listProductBestSelling(programCategoryId).enqueue(object : Callback<WrappedListResponse<Product>> {
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                state.value = HomeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedListResponse<Product>
                    if(response.code() == 200){
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
                    if(response.code() == 200){
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

    fun buyProduct(id: String, token: String, productId: String, qty: String) {
        state.value = HomeState.IsLoading(true)
        api.buyProduct(id, token, productId, qty).enqueue(object : Callback<BuyProduct> {
            override fun onFailure(call: Call<BuyProduct>, t: Throwable) {
                println(t.message)
                state.value = HomeState.Error(t.message)
            }

            override fun onResponse(
                call: Call<BuyProduct>,
                response: Response<BuyProduct>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as BuyProduct
                    if (body.status == 1) {
                        state.value = HomeState.IsSuccess(1)
                    } else {
                        state.value = HomeState.Error("ada kesalahan")
                    }
                } else {
                    state.value = HomeState.Error("gagal memasukan ke keranjang")
                }
                state.value = HomeState.IsLoading(false)
            }
        })
    }

    fun validateBuyProduct(productId: String, qty: String): Boolean {
        state.value = HomeState.Reset
        if (productId.isEmpty() || qty.isEmpty()) {
            state.value = HomeState.ShowToast("Mohon isi form qty")
            return false
        }
        return true
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
    data class ValidateBuyProduct(
        var categoryId: String? = null,
        var qty: String? = null
    ) : HomeState()
    object Reset : HomeState()
}