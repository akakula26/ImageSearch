package com.android.imagesearch.ui.composables


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.android.imagesearch.data.model.PhotoItemList

@Composable
fun PhotoGrid(photos: List<PhotoItemList>, onPhotoClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
    ) {
        items(photos) { photo ->
            PhotoItem(photoItem = photo) {
                onPhotoClick(photo.title)
            }
        }
    }
}


