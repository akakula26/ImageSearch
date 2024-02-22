package com.android.imagesearch.ui.composables
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.android.imagesearch.data.model.PhotoItemList
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ImageDetailView(photoItem: PhotoItemList) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        // Image
        Image(
            painter = rememberImagePainter(
                data = photoItem.media.m,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = photoItem.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(bottom = 16.dp)
        )
        // Title
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Title: ")
                }
                append(photoItem.title)
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Description
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Description: ")
                }
                append(photoItem.description)
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Author
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Author: ")
                }
                append(photoItem.author)
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Published Date
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Published: ")
                }
                append(formatDateString(photoItem.published))              },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

// Helper function to format the date string
private fun formatDateString(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: "")
    } catch (e: Exception) {
        "Unknown date"
    }
}
