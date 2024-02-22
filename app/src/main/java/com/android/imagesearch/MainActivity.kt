package com.android.imagesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.runtime.Composable

import androidx.lifecycle.ViewModelProvider
import com.android.imagesearch.ui.SearchScreen
import com.android.imagesearch.ui.theme.ImageSearchTheme
import com.android.imagesearch.ui.viewmodel.FlickrViewModel
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.imagesearch.data.model.PhotoItemList
import com.android.imagesearch.ui.composables.ImageDetailView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the ViewModel
        val flickrViewModel: FlickrViewModel by viewModels()

        setContent {
            ImageSearchTheme {
                AppNavigation(flickrViewModel)

            }
        }
    }
}

@Composable
fun AppNavigation(flickrViewModel: FlickrViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "searchScreen") {
        composable("searchScreen") {
            SearchScreen(navController = navController, viewModel = flickrViewModel)
        }
        composable(
            "imageDetail/{photoTitle}",
            arguments = listOf(navArgument("photoTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            // Decode the title from the URL
            val photoTitle = backStackEntry.arguments?.getString("photoTitle")?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            // Retrieve the photo item by title from the view model
            val photoItem = photoTitle?.let { flickrViewModel.getPhotoItemByTitle(it) }
            // Pass the retrieved photo item to the detail view composable
            if (photoItem != null) {
                ImageDetailView(photoItem = photoItem)
            } else {
                // Handle the case where the photo item is not found
                // This could be showing an error message or navigating back
            }
        }
    }
}
