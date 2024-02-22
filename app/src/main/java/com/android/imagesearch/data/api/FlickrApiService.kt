package com.android.imagesearch.data.api

import com.android.imagesearch.data.model.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {
    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun searchPhotos(@Query("tags") query: String): FlickrResponse
}