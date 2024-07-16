package com.example.write

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.GalleryImage
import com.example.ui.GalleryState
import com.example.util.model.Diary
import com.example.util.model.Mood
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    onBackPressed: () -> Unit,
    uiState: UiState,
    galleryState: GalleryState,
    onImageSelect: (Uri) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onSaveClicked: (Diary) -> Unit,
    onUpdatedDateTime: (ZonedDateTime) -> Unit,
    moodName: () -> String,
    onDescriptionChanged: (String) -> Unit,
    pagerState: PagerState,
    onImageDeleteClicked: (GalleryImage) -> Unit
) {
    var selectedGalleryImage by remember {
        mutableStateOf<GalleryImage?>(null)
    }

    val pagerImageState = rememberPagerState(pageCount = { galleryState.images.size })
    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = uiState.selectedDiary,
                onBackPressed = onBackPressed,
                onDeleteClicked = onDeleteConfirmed,
                moodName = moodName,
                onUpdatedDateTime = onUpdatedDateTime,
            )
        },
        content = { paddingValues ->
            WriteContent(
                paddingValues = paddingValues,
                uiState = uiState,
                pagerState = pagerState,
                title = uiState.title,
                onTitleChanged = onTitleChanged,
                description = uiState.description,
                onDescriptionChanged = onDescriptionChanged,
                onSaveClicked = onSaveClicked,
                galleryState = galleryState,
                onImageSelect = onImageSelect,
                onImageClicked = { selectedGalleryImage = it }
            )

            AnimatedVisibility(visible = selectedGalleryImage != null) {
                Dialog(onDismissRequest = {
                    selectedGalleryImage = null
                }) {
                    if (selectedGalleryImage != null) {

                        ZoomableImage(
                            selectedGalleryImage = selectedGalleryImage!!,
                            pagerImage = { pagerImage ->
                                selectedGalleryImage = pagerImage
                            },
                            onCloseClicked = {
                                selectedGalleryImage = null
                            },
                            onDeleteClicked = {
                                if (selectedGalleryImage != null) {
                                    onImageDeleteClicked(selectedGalleryImage!!)
                                    selectedGalleryImage = null
                                }
                            },
                            galleryState = galleryState,
                            pagerImageState = pagerImageState
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZoomableImage(
    pagerImage: (GalleryImage) -> Unit,
    onCloseClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    galleryState: GalleryState,
    pagerImageState: PagerState,
    selectedGalleryImage: GalleryImage
) {
    Box {
        LaunchedEffect(key1 = selectedGalleryImage) {
                pagerImageState.scrollToPage(
                    galleryState.images.indexOf(
                        selectedGalleryImage
                    )
                )
        }
        HorizontalPager(state = pagerImageState) { pager ->
            LaunchedEffect(key1 = pager) {
                pagerImage(galleryState.images[pager])
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(galleryState.images[pager].image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Gallery Image"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onCloseClicked) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close Icon")
                Text(text = "Close")
            }
            Button(onClick = onDeleteClicked) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon")
                Text(text = "Delete")
            }
        }
    }
}