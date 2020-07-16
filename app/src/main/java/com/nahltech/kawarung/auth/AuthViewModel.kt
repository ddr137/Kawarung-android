package com.nahltech.kawarung.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nahltech.kawarung.data.models.Register
import com.nahltech.kawarung.data.models.User
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.Constants
import com.nahltech.kawarung.utils.SingleLiveEvent
import com.nahltech.kawarung.utils.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private var state: SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun login(emailPhone: String, password: String) {
        state.value = UserState.IsLoading(true)
        api.login(emailPhone, password).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.Error(t.message)
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<User>
                    if (response.code() == 200) {
                        state.value = UserState.Success(
                            "Bearer ${body.data!!.api_token}",
                            "${body.data!!.id}",
                            "${body.data!!.name}"
                        )
                    } else {
                        state.value = UserState.Failed("Login gagal")
                    }
                } else {
                    state.value = UserState.Error("Email atau Password salah")
                }
                state.value = UserState.IsLoading(false)
            }
        })
    }

    fun fetchFacebook(provider: String, provider_id: String, provider_name: String, provider_email: String) {
        state.value = UserState.IsLoading(true)
        api.postFacebook(provider, provider_id, provider_name, provider_email).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.Error(t.message)
                Log.d("SocmedLogin", "Error")
            }
            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<User>
                    when {
                        response.code() == 200 -> {
                            state.value = UserState.Success(
                                "Bearer ${body.data!!.api_token}",
                                "${body.data!!.id}",
                                "${body.data!!.name}"
                            )
                            Log.d("SocmedLoginAPI", ""+body)
                        }
                        response.code() == 204 -> {
                            state.value = UserState.ErrorSocmed("204", "Akun "+provider+" anda belum terdaftar di Kawarung. Silakan Untuk melanjutkan mendaftar", provider_name, provider_email)
                            Log.d("SocmedLoginAPI", ""+response)
                        }
                        else -> {
                            state.value = UserState.Failed("Login gagal")
                            Log.d("SocmedLoginAPI", ""+body)
                        }
                    }
                } else {
                    state.value = UserState.ErrorSocmed("200", "Akun "+provider+" anda belum terdaftar di Kawarung. Silakan Untuk melanjutkan mendaftar", provider_name, provider_email)
                    Log.d("SocmedLoginAPI", ""+response)
                }
                state.value = UserState.IsLoading(false)
                Log.d("SocmedLoginAPI", ""+response)
            }
        })
    }

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        retryPassword: String
    ) {
        state.value = UserState.IsLoading(true)
        api.register(
            name,
            email,
            phone,
            password,
            retryPassword
        ).enqueue(object : Callback<WrappedResponse<Register>> {
            override fun onFailure(call: Call<WrappedResponse<Register>>, t: Throwable) {
                state.value = UserState.Error("Email atau no hp sudah ada")
            }

            override fun onResponse(
                call: Call<WrappedResponse<Register>>,
                response: Response<WrappedResponse<Register>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<Register>
                    if (response.code() == 200) {
                        state.value = UserState.IsSuccessRegister(200)
                    } else {
                        println(body.message)
                        state.value = UserState.Failed("Daftar gagal. :(")
                    }
                } else {
                    state.value = UserState.Error("Gagal saat mendaftar, coba kembali. :(")
                }
                state.value = UserState.IsLoading(false)
            }
        })
    }

    fun validateRegister(
        name: String,
        email: String,
        phone: String,
        password: String,
        retryPassword: String
    ): Boolean {
        state.value = UserState.Reset
        if (name.isEmpty() || password.isEmpty() || phone.isEmpty() || retryPassword.isEmpty() || email.isEmpty()) {
            state.value = UserState.ShowToast("Mohon isi semua form")
            return false
        }

        if (!Constants.isValidPassword(password)) {
            state.value = UserState.ValidateRegister(password = "Password setidaknya 6 karakter")
            return false
        }

        if (!Constants.isValidPassword(retryPassword)) {
            state.value = UserState.ValidateRegister(password = "Password setidaknya 6 karakter")
            return false
        }
        return true
    }


    fun validateLogin(emailPhone: String, password: String): Boolean {
        state.value = UserState.Reset
        if (emailPhone.isEmpty() || password.isEmpty()) {
            state.value =
                UserState.ShowToast("Mohon isi semua form")
            return false
        }
        if (!Constants.isValidPassword(password)) {
            state.value =
                UserState.Validate(password = "Password setidaknya 6 karakter")
            return false
        }
        return true
    }

    fun getState() = state
}

sealed class UserState {
    data class ErrorSocmed(var status: String, var err: String, var name: String, var email: String) : UserState()
    data class Error(var err: String?) : UserState()
    data class ShowToast(var message: String) : UserState()
    data class Validate(
        var emailPhone: String? = null,
        var password: String? = null
    ) : UserState()

    data class ValidateRegister(
        var name: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var password: String? = null,
        var retryPassword: String? = null
    ) : UserState()

    data class IsLoading(var state: Boolean) : UserState()
    data class Success(var token: String, var id: String, var name: String) : UserState()
    data class SuccessFb(var token: String, var id: String) : UserState()
    data class IsSuccessRegister(var what: Int? = null) : UserState()
    data class Failed(var message: String) : UserState()
    object Reset : UserState()
}