package com.android.imagesearch.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.imagesearch.ui.composables.PhotoGrid
import com.android.imagesearch.ui.composables.SearchBar
import com.android.imagesearch.ui.viewmodel.FlickrViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SearchScreen(navController: NavController, viewModel: FlickrViewModel) {
    val searchQuery = remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Trigger search when the query changes
    LaunchedEffect(searchQuery.value) {
        viewModel.searchPhotos(searchQuery.value)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onSearch = { query ->
                viewModel.searchPhotos(query)
            }
        )
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            }
            errorMessage != null -> {
                // Display error message
                Text(
                    text = errorMessage.toString(),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Red
                )
            }
            photos.isNotEmpty() -> {
                PhotoGrid(photos = photos) { photoItem ->
                    // URL-encode the title to ensure it can be passed as a safe URL parameter
                    val encodedTitle = URLEncoder.encode(photoItem, StandardCharsets.UTF_8.toString())
                    navController.navigate("imageDetail/$encodedTitle")

                }
            }
            else -> {
                // No photos to display, you can show a message if needed
                Text(
                    text = "No photos to display",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Red
                )
            }
        }
    }
}


