package com.android.imagesearch.data.repository

import com.android.imagesearch.data.api.FlickrApiService
import javax.inject.Inject

class FlickrRepository @Inject constructor(
    private val apiService: FlickrApiService
) {
    suspend fun searchPhotos(query: String) = apiService.searchPhotos(query)
}

