package com.nahltech.kawarung.data.network

import com.nahltech.kawarung.data.models.*
import com.nahltech.kawarung.data.models.address.City
import com.nahltech.kawarung.data.models.address.District
import com.nahltech.kawarung.data.models.address.Province
import com.nahltech.kawarung.data.models.address.Village
import com.nahltech.kawarung.data.models.best_selling.Product
import com.nahltech.kawarung.data.models.cart.Cart
import com.nahltech.kawarung.data.models.cart.Data
import com.nahltech.kawarung.data.models.cart.DataX
import com.nahltech.kawarung.data.models.cart.OrderProducts
import com.nahltech.kawarung.utils.WrappedListResponse
import com.nahltech.kawarung.utils.WrappedResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    /** Image Slider **/
    @GET("api/banner-sliders")
    fun getBannerSlider(): Call<List<Banner>>

    /** Auth **/
    @FormUrlEncoded
    @POST("api/auth/login")
    fun login(
        @Field("email_phone") emailPhone: String,
        @Field("password") password: String
    ): Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/auth/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("ulangi_password") retryPassword: String
    ): Call<WrappedResponse<Register>>

    /** Get Profile **/
    @GET("api/users/profile")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<WrappedResponse<User>>

    /** Change Image **/
    @Multipart
    @POST("api/users/{id}/change-image")
    fun changeImage(
        //@Header("Authorization") token : String,
        @Path("id") id : String,
        @Part image: MultipartBody.Part
    ): Call<WrappedResponse<ResponseImageUploader>>

    /** Edit Account **/
    @FormUrlEncoded
    @POST("api/users/{id}")
    fun editAccount(
        @Header("Authorization") token : String,
        @Path("id") id : String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Call<WrappedResponse<User>>

    /** Get Province, district, village, city **/
    @GET("api/get-address/provinces")
    fun provinces(): Observable<WrappedListResponse<Province>>

    @GET("api/get-address/cities/{id}")
    fun cities(@Path("id") id: Long): Observable<WrappedListResponse<City>>

    @GET("api/get-address/districts/{id}")
    fun districts(@Path("id") id: Long): Observable<WrappedListResponse<District>>

    @GET("api/get-address/villages/{id}")
    fun villages(@Path("id") id: Long): Observable<WrappedListResponse<Village>>

    /** Edit Address **/
    @FormUrlEncoded
    @POST("api/users/{id}/change-address")
    fun editAddress(
        @Header("Authorization") token : String,
        @Path("id") id : String,
        @Field("province_name") provinceId: String,
        @Field("city_name") cityId: String,
        @Field("districts_name") districtId: String,
        @Field("village_name") villageId: String,
        @Field("address") completeAddress: String,
        @Field("postal_code") postalCode: String
    ): Call<WrappedResponse<User>>

    /** Change Password **/
    @FormUrlEncoded
    @POST("api/users/{id}/change-password")
    fun changePassword(
        @Header("Authorization") token : String,
        @Path("id") id : String,
        @Field("old_password") oldPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_password") confirmPassword: String
    ): Call<WrappedResponse<User>>

    /** Account Bank **/
    @FormUrlEncoded
    @POST("api/users/{id}/change-bank")
    fun accountBank(
        @Header("Authorization") token : String,
        @Path("id") id : String,
        @Field("bank_name") bankName: String,
        @Field("account_number") accountNumber: String,
        @Field("owner_name") ownerName: String,
        @Field("branch") branch: String
    ): Call<WrappedResponse<User>>

    /** Get Product **/
    @GET("api/product/{product_category_name}")
    fun listProductBestSelling(@Path("product_category_name") programCategoryId : String) : Call<WrappedListResponse<Product>>

    @GET("api/product/new")
    fun listProductNew() : Call<WrappedListResponse<Product>>

    /** By Category **/
    @FormUrlEncoded
    @POST("api/product/category")
    fun accountBank(
        @Field("category_id") categoryId: String
    ): Call<WrappedListResponse<Product>>

    /** Buy Product **/
    @FormUrlEncoded
    @POST("api/users/{id}/product/buy")
    fun buyProduct(
        @Path("id") id : String,
        @Header("Authorization") token : String,
        @Field("product_id") productId: String,
        @Field("qty") qty: String
    ): Call<BuyProduct>

    /** Cart **/
    @GET("api/users/{id}/cart")
    fun listCart(
        @Path("id") id_user : String,
        @Header("Authorization") token : String
    ) : Call<Cart<Data<OrderProducts<DataX>>>>

    /** Buy Product **/
    @FormUrlEncoded
    @POST("api/users/{id}/product/buy")
    fun deleteProductCart(
        @Path("id") id : String,
        @Header("Authorization") token : String,
        @Field("order_id") orderId: String
    ): Call<Product>
}