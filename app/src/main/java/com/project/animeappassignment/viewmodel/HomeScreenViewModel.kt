package com.project.animeappassignment.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.animeappassignment.common.Constants.BASE_URL
import com.project.animeappassignment.common.NetworkConnectivityObserver
import com.project.animeappassignment.common.NetworkStatus
import com.project.animeappassignment.domain.use_case.GetAnimesUseCase
import com.project.animeappassignment.model.TopAnimesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import  com.project.animeappassignment.common.Resource
import com.project.animeappassignment.domain.remote.JikanAPI
import com.project.animeappassignment.domain.use_case.GetAnimeLocallyUseCase
import com.project.animeappassignment.domain.use_case.SaveAnimeLocallyUseCase
import com.project.animeappassignment.model.Data
import com.project.animeappassignment.model.TopAnimes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@HiltViewModel
class HomeScreenViewModel @Inject constructor (
    val getAnimesUseCase: GetAnimesUseCase,
    val getAnimeLocallyUseCase: GetAnimeLocallyUseCase,
    val saveAnimeLocallyUseCase: SaveAnimeLocallyUseCase,
     networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = networkConnectivityObserver.observe()
        .map { it == NetworkStatus.Available }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    private val _state = mutableStateOf(TopAnimesState())
    val state  : State<TopAnimesState> = _state

    val localSyncState = mutableStateOf<String?>(null)


    init {
        getTopAnimeAPI()
        if(isConnected.value) {
            getTopAnimeAPI()
        }else{
            getTopAnimeLocal()
        }
    }

    fun getTopAnimeAPI(){
        getAnimesUseCase().onEach { result->
            when(result){
                is Resource.Loading ->{
                    _state.value= TopAnimesState(
                        isLoading = true,
                        topAnime = null,
                        error=""
                    )
                }
                is Resource.Success ->{
                    _state.value= TopAnimesState(
                        isLoading = false,
                        topAnime = result.data,
                        error=""
                    )
                    Log.d("Data_Inserted", " calling sync data ${result.data} ")
                    saveDataLocally(result.data?.data)
                }
                is Resource.Error ->{
                    _state.value= TopAnimesState(
                        topAnime = null,
                        error= result.errorMessage ?: "Some Error Occured"
                    )
                }
            }

        }.launchIn(viewModelScope)
    }


    fun getTopAnimeLocal(){
        getAnimeLocallyUseCase().onEach { result->

            when(result){
                is Resource.Loading ->{
                    _state.value= TopAnimesState(
                        isLoading = true,
                        topAnime = null,
                        error=""
                    )
                }
                is Resource.Success ->{
                    _state.value= TopAnimesState(
                        isLoading = false,
                        topAnime = TopAnimes(
                            data=result.data
                        ),
                        error=""
                    )
                }
                is Resource.Error ->{
                    _state.value= TopAnimesState(
                        topAnime = null,
                        error= result.errorMessage ?: "Some Error Occured"
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun saveDataLocally(data: List<Data>?) {
        Log.d("Data_Inserted", "saveDataLocally:$data ")
        saveAnimeLocallyUseCase(data).onEach { result->
            when(result){
                is Resource.Loading ->{
                    localSyncState.value = ""
                }
                is Resource.Success ->{
                    localSyncState.value = result.data
                }
                is Resource.Error ->{
                    localSyncState.value = ""
                }
            }

        }.launchIn(viewModelScope)

    }

}