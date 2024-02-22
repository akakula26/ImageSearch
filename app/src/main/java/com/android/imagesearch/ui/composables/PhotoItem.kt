package com.android.imagesearch.ui.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.android.imagesearch.R
import com.android.imagesearch.data.model.PhotoItemList

@Composable
fun PhotoItem(
    photoItem: PhotoItemList,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        painter = rememberImagePainter(
            data = photoItem.media.m,
            builder = {
                crossfade(true)
                listener(
                    onStart = { Log.d("ImageLoading", "Start loading image: ${photoItem.media.m}") },
                    onSuccess = { _, _ -> Log.d("ImageLoading", "Success loading image") },
                    onError = { _, throwable -> Log.e("ImageLoading", "Error loading image", throwable) }
                )
            }
        )
        ,
        contentDescription = photoItem.title,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f) // Replace with the aspect ratio you want
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onClick)
    )
}

