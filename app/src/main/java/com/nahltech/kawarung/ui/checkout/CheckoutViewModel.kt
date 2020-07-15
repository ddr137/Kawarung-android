package com.nahltech.kawarung.ui.checkout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.Responses
import com.nahltech.kawarung.data.models.cart.Cart
import com.nahltech.kawarung.data.models.cart.DataX
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel : ViewModel() {
    private var productCheckout = MutableLiveData<List<DataX>>()
    private var state: SingleLiveEvent<CheckoutState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun checkoutPurchase(
        user_id: String,
        token: String,
        province_name: String,
        city_name: String,
        districts_name: String,
        village_name: String,
        address: String,
        postal_code: String,
        order_id: String,
        note: String,
        payment_method: String,
        total_discount: String,
        subtotal: String
    ) {
        state.value = CheckoutState.IsLoading(true)
        api.checkoutPurchase(
            user_id,
            token,
            province_name,
            city_name,
            districts_name,
            village_name,
            address,
            postal_code,
            order_id,
            note,
            payment_method,
            total_discount,
            subtotal
        )
            .enqueue(object : Callback<Responses> {
                override fun onFailure(call: Call<Responses>, t: Throwable) {
                    state.value = CheckoutState.Error(t.message)
                }

                override fun onResponse(
                    call: Call<Responses>,
                    response: Response<Responses>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body() as Responses
                        if (response.code() == 201) {
                            state.value = CheckoutState.IsSuccess(201)
                        } else {
                            state.value = CheckoutState.Error("Coba lagi beberapa saat. :(")

                        }
                    } else {
                        state.value = CheckoutState.Error("Kesalahan ")
                    }
                    state.value = CheckoutState.IsLoading(false)
                }
            })
    }

    fun fetchProductCheckout(id_user: String, token: String) {
        state.value = CheckoutState.IsLoading(true)
        api.listCart(id_user, token).enqueue(object :
            Callback<Cart> {
            override fun onFailure(
                call: Call<Cart>,
                t: Throwable
            ) {
                state.value = CheckoutState.Error(t.message)
            }

            override fun onResponse(
                call: Call<Cart>,
                response: Response<Cart>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as Cart
                    if (response.code() == 200) {
                        val r = body.data.orderProducts.data
                        productCheckout.postValue(r)
                    }
                } else {
                    state.value = CheckoutState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = CheckoutState.IsLoading(false)
            }
        })
    }

    fun getProductCheckout() = productCheckout
    fun getState() = state

}

sealed class CheckoutState {
    data class ShowToast(var message: String) : CheckoutState()
    data class IsLoading(var state: Boolean = false) : CheckoutState()
    data class Error(var err: String?) : CheckoutState()
    data class Failed(var message: String) : CheckoutState()
    data class IsSuccess(var what: Int? = null) : CheckoutState()
    object Reset : CheckoutState()
}