package com.tafakkur.diaryapp.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.tafakkur.diaryapp.connectivity.ConnectivityObserver
import com.tafakkur.diaryapp.connectivity.NetworkConnectivityObserver
import com.tafakkur.diaryapp.data.database.ImageToDeleteDao
import com.tafakkur.diaryapp.data.database.entity.ImageToDelete
import com.tafakkur.diaryapp.data.repository.Diaries
import com.tafakkur.diaryapp.data.repository.MongoDB
import com.tafakkur.diaryapp.model.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
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
    private var netwok by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    var dateIsSelected by mutableStateOf(false)
        private set

    private lateinit var allDiariesJob: Job
    private lateinit var filteredDiariesJob: Job

    init {
        getDiaries()
        viewModelScope.launch {
            connectivity.observe().collect{
                netwok = it
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
            MongoDB.getAllDiaries().debounce(2000).collect{result ->
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
        if (netwok == ConnectivityObserver.Status.Available){
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
                                        ImageToDelete(remoteImagePath = imagePath)
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