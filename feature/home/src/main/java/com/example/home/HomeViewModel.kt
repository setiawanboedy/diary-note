package com.example.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mongo.database.ImageToDeleteDao
import com.example.mongo.database.entity.ImageToDelete
import com.example.mongo.repository.Diaries
import com.example.mongo.repository.MongoDB
import com.example.util.connectivity.ConnectivityObserver
import com.example.util.connectivity.NetworkConnectivityObserver
import com.example.util.model.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val imageToDeleteDao: ImageToDeleteDao
): ViewModel() {
    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    var dateIsSelected by mutableStateOf(false)
        private set

    private lateinit var allDiariesJob: Job
    private lateinit var filteredDiariesJob: Job

    init {
        getDiaries()
        viewModelScope.launch {
            connectivity.observe().collect{
                network = it
            }
        }
    }

    fun getDiaries(zonedDateTime: ZonedDateTime? = null){
        dateIsSelected = zonedDateTime != null
        diaries.value = RequestState.Loading
        if (dateIsSelected && zonedDateTime != null){
            observerFilteredDiaries(zonedDateTime = zonedDateTime)
        }else{
            observeAllDiaries()
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeAllDiaries(){
        allDiariesJob = viewModelScope.launch {
            if (::filteredDiariesJob.isInitialized){
                filteredDiariesJob.cancelAndJoin()
            }
            MongoDB.getAllDiaries().debounce(2000).collect{ result ->
                diaries.value = result
            }
        }
    }

    private fun observerFilteredDiaries(zonedDateTime: ZonedDateTime){
        filteredDiariesJob = viewModelScope.launch {
            if (::allDiariesJob.isInitialized){
                allDiariesJob.cancelAndJoin()
            }
            MongoDB.getFilteredDiaries(zonedDateTime = zonedDateTime).collect{ result ->
                diaries.value = result
            }
        }
    }

    fun deleteAllDiaries(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ){
        if (network == ConnectivityObserver.Status.Available){
            val userIdd = FirebaseAuth.getInstance().currentUser?.uid
            val imageDirectory = "images/${userIdd}"
            val storage = FirebaseStorage.getInstance().reference
            storage.child(imageDirectory)
                .listAll()
                .addOnSuccessListener {
                    it.items.forEach { ref ->
                        val imagePath = "images/${userIdd}/${ref.name}"
                        storage.child(imagePath).delete()
                            .addOnFailureListener {
                                viewModelScope.launch(Dispatchers.IO) {
                                    imageToDeleteDao.addImageToDelete(
                                        ImageToDelete(
                                            remoteImagePath = imagePath
                                        )
                                    )
                                }
                            }
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = MongoDB.deleteAllDiaries()
                        if (result is RequestState.Success){
                            withContext(Dispatchers.Main){
                                onSuccess()
                            }
                        }else if (result is RequestState.Error){
                            withContext(Dispatchers.Main){
                                onError(result.error)
                            }
                        }
                    }
                }
        }else {
            onError(Exception("No Internet Connection."))
        }
    }
}