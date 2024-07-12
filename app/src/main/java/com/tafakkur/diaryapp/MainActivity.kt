package com.tafakkur.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.util.Constants.APP_ID
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import com.example.mongo.database.ImageToDeleteDao
import com.example.mongo.database.ImageToUploadDao
import com.example.mongo.database.entity.ImageToDelete
import com.example.mongo.database.entity.ImageToUpload
import com.example.mongo.repository.MongoDB
import com.example.util.Screen
import com.tafakkur.diaryapp.navigation.SetupNavGraph
import com.example.ui.theme.DiaryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageToUploadDao: com.example.mongo.database.ImageToUploadDao

    @Inject
    lateinit var imageToDeleteDao: com.example.mongo.database.ImageToDeleteDao

    private var keepSplashOpened: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(this)
        com.example.mongo.repository.MongoDB.configureTheRealm()
        setContent {

            DiaryAppTheme(dynamicColor = false) {
               val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    }
                )
            }
        }
        cleanupCheck(scope = lifecycleScope, imageToUploadDao = imageToUploadDao, imageToDeleteDao = imageToDeleteDao)
    }
}

private fun cleanupCheck(
    scope: CoroutineScope,
    imageToUploadDao: com.example.mongo.database.ImageToUploadDao,
    imageToDeleteDao: com.example.mongo.database.ImageToDeleteDao
){
    scope.launch(Dispatchers.IO) {
        val result = imageToUploadDao.getAllImages()
        result.forEach {imageToUpload ->
            retryUploadingImageToFirebase(
                imageToUpload = imageToUpload,
                onSuccess = {
                    scope.launch(Dispatchers.IO) {
                        imageToUploadDao.cleanupImage(imageId = imageToUpload.id)
                    }
                }
            )
        }
        val result2 = imageToDeleteDao.getAllImages()
        result2.forEach { imageToDelete ->
            retryDeletingImageFromFirebase(
                imageToDelete = imageToDelete,
                onSuccess = {
                    scope.launch(Dispatchers.IO) {
                        imageToDeleteDao.cleanupImage(imageId = imageToDelete.id)
                    }
                }
            )
        }
    }
}

private fun getStartDestination(): String{
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route
}

fun retryUploadingImageToFirebase(
    imageToUpload: com.example.mongo.database.entity.ImageToUpload,
    onSuccess: () -> Unit
){
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToUpload.remoteImagePath).putFile(
        imageToUpload.imageUri.toUri(),
        storageMetadata {  },
        imageToUpload.sessionUri.toUri()
    ).addOnSuccessListener { onSuccess() }
}

fun retryDeletingImageFromFirebase(
    imageToDelete: com.example.mongo.database.entity.ImageToDelete,
    onSuccess: () -> Unit,
){
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToDelete.remoteImagePath).delete()
        .addOnSuccessListener { onSuccess() }
}