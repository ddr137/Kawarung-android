package com.nahltech.kawarung.ui.checkout

import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.SingleLiveEvent

class CheckoutViewModel: ViewModel() {
    //private var users = MutableLiveData<User>()
    private var state: SingleLiveEvent<CheckoutState> = SingleLiveEvent()
    private var api = ApiClient.instance()
}

sealed class CheckoutState {
    data class ShowToast(var message: String) : CheckoutState()
    data class IsLoading(var state: Boolean = false) : CheckoutState()
    data class Error(var err: String?) : CheckoutState()
    data class Failed(var message: String) : CheckoutState()
    data class IsSuccess(var what: Int? = null) : CheckoutState()
    object Reset : CheckoutState()
}