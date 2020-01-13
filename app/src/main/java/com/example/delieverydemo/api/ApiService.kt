package com.example.delieverydemo.api

import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit =
                Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl(Constants.BASE_URL)
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    /*  @GET("v2/deliveries")
      //query needed if there is any query
      fun getDeliveryList(
          @Query("limit") limit: Int,
          @Query("offset") offset: Int
      ): Observable<ArrayList<DeliveryResponseModel>>*/

    @GET("v2/deliveries")
    //query needed if there is any query
    fun getDeliveryList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ArrayList<DeliveryResponseModel>>
}