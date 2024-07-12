package com.example.home.navigation

import android.widget.Toast
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.home.HomeScreen
import com.example.home.HomeViewModel
import com.example.ui.components.DisplayAlertDialog
import com.example.util.Constants.APP_ID
import com.example.util.Screen
import com.example.util.model.RequestState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            com.example.mongo.repository.MongoDB.configureTheRealm()
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