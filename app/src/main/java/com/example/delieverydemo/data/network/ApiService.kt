package com.example.delieverydemo.data.network

import com.example.delieverydemo.ui.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

    @GET("v2/deliveries")
    //query needed if there is any query
    fun getDeliveryList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Observable<List<DeliveryResponseModel>>

    /*@GET("v2/deliveries")
    //query needed if there is any query
    fun getDeliveryList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ArrayList<DeliveryResponseModel>>*/
}