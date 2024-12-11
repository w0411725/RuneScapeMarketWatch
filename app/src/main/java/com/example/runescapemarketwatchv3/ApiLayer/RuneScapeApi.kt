package com.example.runescapemarketwatchv3.ApiLayer

import com.example.runescapemarketwatchv3.ItemResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface RuneScapeApi {
    @GET("m=itemdb_rs/api/catalogue/detail.json")
    suspend fun getItemDetails(@Query("item") itemId: Int): Response<ItemResponse>
}
