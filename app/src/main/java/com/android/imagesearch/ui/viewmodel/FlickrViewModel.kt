package com.android.imagesearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.imagesearch.data.model.PhotoItemList
import com.android.imagesearch.data.repository.FlickrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlickrViewModel @Inject constructor(private val repository: FlickrRepository) : ViewModel() {
    // Holds the list of photos to be displayed
    private val _photos = MutableStateFlow<List<PhotoItemList>>(emptyList())
    val photos: StateFlow<List<PhotoItemList>> = _photos.asStateFlow()

    // Holds the complete list of photo items retrieved from the repository
    var photoItems = listOf<PhotoItemList>()

    // Indicates whether the data is currently loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Holds the error message to be displayed, if any
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Holds a reference to the current search job to allow cancellation
    private var searchJob: Job? = null

    // Initiates a search for photos based on the given query
    fun searchPhotos(query: String) {
        searchJob?.cancel() // Cancel any ongoing search
        searchJob = viewModelScope.launch {
            if (query.isBlank()) {
                _photos.value = emptyList()
                photoItems = emptyList()
                _errorMessage.value = null
                return@launch
            }
            _isLoading.value = true
            _errorMessage.value = null
            delay(500) // Debounce to wait for additional input
            try {
                val flickrResponse = repository.searchPhotos(query)
                photoItems = flickrResponse.items
                _photos.value = photoItems
                if (photoItems.isEmpty()) {
                    _errorMessage.value = "No data found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Unable to fetch data. Something wrong with the network."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Retrieves a photo item by its title
    fun getPhotoItemByTitle(title: String): PhotoItemList? {
        return photoItems.find { it.title == title }
    }
}


