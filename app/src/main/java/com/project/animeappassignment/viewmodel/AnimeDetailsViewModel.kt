package com.project.animeappassignment.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.animeappassignment.common.Constants.PARAM_ANIME_ID
import com.project.animeappassignment.common.NetworkConnectivityObserver
import com.project.animeappassignment.common.NetworkStatus
import com.project.animeappassignment.common.Resource
import com.project.animeappassignment.domain.use_case.GetAnimeByIdUseCase
import com.project.animeappassignment.domain.use_case.GetAnimeDetailsUseCase
import com.project.animeappassignment.domain.use_case.GetAnimeLocallyUseCase
import com.project.animeappassignment.model.AnimeDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsViewModel@Inject constructor (
    val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    val getAnimeByIdUseCase: GetAnimeByIdUseCase,
    val networkConnectivityObserver: NetworkConnectivityObserver,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = networkConnectivityObserver.observe()
        .map { it == NetworkStatus.Available }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    private val _state = mutableStateOf(AnimeDataState())
    val state  : State<AnimeDataState> = _state

    private val _uiState = MutableStateFlow(AnimeUiState())
    val uiState: StateFlow<AnimeUiState> = _uiState

    init {
            savedStateHandle.get<String>(PARAM_ANIME_ID)?.let {
                try {
                    Log.d("hjkmm", ": $it")
                    if (isConnected.value) {
                        getSelectedAnimeDetails(anime_id = it.toLong())
                    } else {
                        getAnimeDataLocally(it.toLong())
                    }
                }catch (e:Exception){
                    Log.d("ErrorOnLoading :", "${e.message} ")
                }
            }
    }



    fun setPosterMode() {
        _uiState.update { it.copy(playTrailer = false, loadingImage = isConnected.value) }

    }

    fun setTrailerMode() {
        _uiState.update { it.copy(playTrailer = isConnected.value, loadingImage = false) }
    }

    fun getSelectedAnimeDetails(anime_id: Long){
        getAnimeDetailsUseCase(anime_id).onEach { result->
            when(result){
                is Resource.Loading ->{
                    _state.value= AnimeDataState(
                        isLoading = true,
                        animeData = null,
                        error=""
                    )
                }
                is Resource.Success ->{
                    Log.d("bhffn", " ${result.data}")
                    _state.value= AnimeDataState(
                        isLoading = false,
                        animeData = result.data,
                        error=""
                    )
                }
                is Resource.Error ->{
                    _state.value= AnimeDataState(
                        animeData = null,
                        error= result.errorMessage ?: "Some Error Occured"
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

    fun getAnimeDataLocally(animeId:Long){
        getAnimeByIdUseCase(animeId).onEach { result->
            when(result){
                is Resource.Loading ->{
                    _state.value= AnimeDataState(
                        isLoading = true,
                        animeData = null,
                        error=""
                    )
                }
                is Resource.Success ->{
                    Log.d("bhffn", " ${result}")
                    _state.value= AnimeDataState(
                        isLoading = false,
                        animeData = result.data,
                        error=""
                    )
                }
                is Resource.Error ->{
                    _state.value= AnimeDataState(
                        isLoading = false,
                        animeData = null,
                        error= result.errorMessage ?: "Some Error Occured"
                    )
                }
            }

        }.launchIn(viewModelScope)
    }


}

data class AnimeUiState(
    val playTrailer: Boolean = false,
    val loadingImage: Boolean = true
)