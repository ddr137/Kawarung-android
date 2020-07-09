package com.nahltech.kawarung.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.best_selling.Product
import com.nahltech.kawarung.data.models.cart.Cart
import com.nahltech.kawarung.data.models.cart.Data
import com.nahltech.kawarung.data.models.cart.DataX
import com.nahltech.kawarung.data.models.cart.OrderProducts
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {
    private var api = ApiClient.instance()
    private var productCart = MutableLiveData<List<DataX>>()
    private var state: SingleLiveEvent<CartState> = SingleLiveEvent()

    fun fetchProductCart(id_user: String, token: String) {
        state.value = CartState.IsLoading(true)
        api.listCart(id_user, token).enqueue(object :
            Callback<Cart<Data<OrderProducts<DataX>>>> {
            override fun onFailure(
                call: Call<Cart<Data<OrderProducts<DataX>>>>,
                t: Throwable
            ) {
                state.value = CartState.Error(t.message)
            }

            override fun onResponse(
                call: Call<Cart<Data<OrderProducts<DataX>>>>,
                response: Response<Cart<Data<OrderProducts<DataX>>>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as Cart<Data<OrderProducts<DataX>>>
                    if (response.code() == 200) {
                        val r = body.data.orderProducts.data
                        productCart.postValue(r)
                        state.value = CartState.IsSuccess(200)
                    }
                } else {
                    state.value = CartState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = CartState.IsLoading(false)
            }
        })
    }

    fun deleteProductCart(id: String, token: String, orderId: String) {
        state.value = CartState.IsLoading(true)
        api.deleteProductCart(id, token, orderId).enqueue(object : Callback<Product> {
            override fun onFailure(call: Call<Product>, t: Throwable) {
                println(t.message)
                state.value = CartState.Error(t.message)
            }

            override fun onResponse(
                call: Call<Product>,
                response: Response<Product>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as Product
                    if (body.status.equals("1")) {
                        state.value = CartState.IsSuccess(1)
                    } else {
                        state.value = CartState.Failed("Login gagal")
                    }
                } else {
                    state.value = CartState.Error("Email atau Password salah")
                }
                state.value = CartState.IsLoading(false)
            }
        })
    }

    fun getProductCart() = productCart
    fun getState() = state

}

sealed class CartState {
    data class ShowToast(var message: String) : CartState()
    data class IsLoading(var state: Boolean = false) : CartState()
    data class Error(var err: String?) : CartState()
    data class Failed(var message: String) : CartState()
    data class IsSuccess(var what: Int? = null) : CartState()
}
