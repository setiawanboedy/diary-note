package com.example.write.navigation

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.example.util.Screen
import com.example.util.model.Mood
import com.example.write.WriteScreen
import com.example.write.WriteViewModel


@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(
    onBackPressed: () -> Unit,
) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val pagerState = rememberPagerState(pageCount = { Mood.entries.size })
        val viewModel: WriteViewModel = hiltViewModel()
        val uiState = viewModel.uiState
        val context = LocalContext.current
        val galleryState = viewModel.galleryState
        val pageNumber by remember {
            derivedStateOf {
                pagerState.currentPage
            }
        }

        WriteScreen(
            uiState = uiState,
            onTitleChanged = {
                viewModel.setTitle(it)
            },
            onDescriptionChanged = {
                viewModel.setDescription(it)
            },
            pagerState = pagerState,
            moodName = {
                Mood.entries[pageNumber].name
            },
            onDeleteConfirmed = {
                viewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(context, "Success Deleted", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    },
                    onError = {

                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onBackPressed = onBackPressed,
            galleryState = galleryState,
            onImageSelect = {
                val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"
                viewModel.addImage(image = it, imageType = type)
            },
            onSaveClicked = {
                viewModel.upsertDiary(
                    diary = it.apply { mood = Mood.entries[pageNumber].name },
                    onSuccess = {
                        onBackPressed()
                    },
                    onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onUpdatedDateTime = {
                viewModel.setDateTime(zonedDateTime = it)
            },
            onImageDeleteClicked = {galleryState.removeImage(it)}
        )
    }
}