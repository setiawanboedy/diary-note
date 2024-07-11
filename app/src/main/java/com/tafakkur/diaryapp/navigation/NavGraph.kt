package com.tafakkur.diaryapp.navigation

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.tafakkur.diaryapp.data.repository.MongoDB
import com.tafakkur.diaryapp.model.GalleryImage
import com.tafakkur.diaryapp.model.Mood
import com.tafakkur.diaryapp.presentation.components.DisplayAlertDialog
import com.tafakkur.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.tafakkur.diaryapp.presentation.screens.auth.AuthenticationViewModel
import com.tafakkur.diaryapp.presentation.screens.home.HomeScreen
import com.tafakkur.diaryapp.presentation.screens.home.HomeViewModel
import com.tafakkur.diaryapp.presentation.screens.write.WriteScreen
import com.tafakkur.diaryapp.presentation.screens.write.WriteViewModel
import com.tafakkur.diaryapp.util.Constants.APP_ID
import com.tafakkur.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.tafakkur.diaryapp.model.RequestState
import com.tafakkur.diaryapp.model.rememberGalleryState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            onDataLoaded = onDataLoaded,
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToWriteArgs = {
                navController.navigate(Screen.Write.passDiaryId(diaryId = it))
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            }
        )
        writeRoute(
            onBackPressed = {
                navController.popBackStack()

            }
        )
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val authenticated by viewModel.authenticated
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

        AuthenticationScreen(
            loadingState = loadingState,
            oneTapState = oneTapState,
            authenticated = authenticated,
            onSuccessfullyFirebaseSign = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated")
                        viewModel.setLoading(false)

                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )
            },
            onFailedFirebaseSign = {
                messageBarState.addError(it)
                viewModel.setLoading(false)
            },
            onDialogDismiss = {
                messageBarState.addError(Exception(it))
                viewModel.setLoading(false)
            },
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            navigateToHome = navigateToHome,
            messageBarState = messageBarState,
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit,
    navigateToWriteArgs: (String) -> Unit,
) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        val viewModel: HomeViewModel = hiltViewModel()
        val diaries by viewModel.diaries
        var deleteAllDialogOpened by remember {
            mutableStateOf(false)
        }
        val context = LocalContext.current

        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading) {
                onDataLoaded()
            }
        }

        HomeScreen(
            diaries = diaries,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = navigateToWrite,
            navigateToWriteArgs = navigateToWriteArgs,
            onDateReset = {viewModel.getDiaries()},
            onDateSelected = {viewModel.getDiaries(zonedDateTime = it)},
            dateIsSelected = viewModel.dateIsSelected,
            onSignOutClicked = {
                signOutDialogOpened = true
            },

            onDeleteAllClicked = {
                deleteAllDialogOpened = true
            },
            drawerState = drawerState,
        )

        LaunchedEffect(key1 = Unit) {
            MongoDB.configureTheRealm()
        }

        DisplayAlertDialog(
            dialogOpened = signOutDialogOpened,
            title = "Sign Out",
            message = "Are you sure !",
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            },
            onCloseClicked = {
                signOutDialogOpened = false
            }
        )

        DisplayAlertDialog(
            dialogOpened = deleteAllDialogOpened,
            title = "Delete All Diaries",
            message = "Are you sure delete all diaries?",
            onYesClicked = {
                viewModel.deleteAllDiaries(
                    onSuccess = {
                        Toast.makeText(context, "All Diaries Deleted.", Toast.LENGTH_SHORT).show()
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onError = {
                        Toast.makeText(context, if (it.message == "No Internet Connection.") "We need Internet Connection for this operation." else it.message, Toast.LENGTH_SHORT).show()
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            onCloseClicked = {deleteAllDialogOpened = false}
            )
    }
}

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