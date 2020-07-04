package com.nahltech.kawarung.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.cart.Data
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent
import com.nahltech.kawarung.utils.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel: ViewModel() {
    private var api = ApiClient.instance()
    private var productCart = MutableLiveData<List<com.nahltech.kawarung.data.models.cart.Product>>()
    private var state: SingleLiveEvent<CartState> = SingleLiveEvent()

    fun fetchProductCart(id_user: String, token: String){
        state.value = CartState.IsLoading(true)
        api.listCart(id_user, token).enqueue(object : Callback<WrappedResponse<Data<com.nahltech.kawarung.data.models.cart.Product>>> {
            override fun onFailure(call: Call<WrappedResponse<Data<com.nahltech.kawarung.data.models.cart.Product>>>, t: Throwable) {
                state.value = CartState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Data<com.nahltech.kawarung.data.models.cart.Product>>>, response: Response<WrappedResponse<Data<com.nahltech.kawarung.data.models.cart.Product>>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedResponse<Data<com.nahltech.kawarung.data.models.cart.Product>>
                    if(body.status.equals("1")){
                        val r = body.data!!.product
                        productCart.postValue(r)
                        state.value = CartState.IsSuccess(1)
                    }
                }else{
                    state.value = CartState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = CartState.IsLoading(false)
            }
        })
    }

    fun getProductCart() = productCart
    fun getState()  = state

}

sealed class CartState {
    data class ShowToast(var message : String) : CartState()
    data class IsLoading(var state : Boolean = false) : CartState()
    data class Error(var err : String?) : CartState()
    data class IsSuccess(var what : Int? = null) : CartState()
}
