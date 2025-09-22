package com.project.animeappassignment.domain.use_case

import coil.network.HttpException
import com.project.animeappassignment.common.Resource
import com.project.animeappassignment.domain.remote.RemoteRepository
import com.project.animeappassignment.domain.remote.dto.topanime.createData
import com.project.animeappassignment.domain.remote.dto.topanime.createTopAnimes
import com.project.animeappassignment.model.Data
import com.project.animeappassignment.model.TopAnimes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetAnimeDetailsUseCase @Inject constructor(
    val remoteRepository: RemoteRepository
) {
    operator fun invoke(animeId: Long) : Flow<Resource<Data>> = flow {
        try{
            emit(Resource.Loading<Data>("Loading animes"))
            val animeData = remoteRepository.getAnimeDetails(animeId).data.createData()
            emit(Resource.Success(animeData))
        }catch (e: HttpException){
            emit(Resource.Error<Data>(e.localizedMessage ?: " Line 23 GetAnimeDetailsUseCase An unexpected error occured. "))
        }catch (e: IOException){
            emit(Resource.Error<Data>(e.localizedMessage ?: "  Line 25 GetAnimeDetailsUseCase Problem in Internet connection."))
        }catch (e: Exception){
            emit(Resource.Error<Data>(e.localizedMessage ?: "  Line 27 GetAnimeDetailsUseCase This error occured"))
        }
    }
}