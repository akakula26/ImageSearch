package com.android.imagesearch.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.android.imagesearch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: MutableState<String>,
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        value = query.value,
        onValueChange = { newValue ->
            query.value = newValue
            onSearch(newValue)
        },
        placeholder = { Text(stringResource(id = R.string.search_photos)) },
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            if (query.value.isNotEmpty()) {
                onSearch(query.value.trim())
            }
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

