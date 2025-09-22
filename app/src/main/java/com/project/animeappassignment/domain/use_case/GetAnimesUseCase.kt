package com.project.animeappassignment.domain.use_case

import coil.network.HttpException
import com.project.animeappassignment.common.Resource
import com.project.animeappassignment.domain.remote.RemoteRepository
import com.project.animeappassignment.domain.remote.dto.topanime.createTopAnimes
import com.project.animeappassignment.model.TopAnimes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetAnimesUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    operator fun invoke() : Flow<Resource<TopAnimes>> = flow {
        try{
            emit(Resource.Loading<TopAnimes>("Loading animes"))
            val topAnimes = remoteRepository.getTopAnimes().createTopAnimes()
            emit(Resource.Success(topAnimes))
        }catch (e: HttpException){
            emit(Resource.Error<TopAnimes>(e.localizedMessage ?: " Line 23 GetAnimesUseCase An unexpected error occured. "))
        }catch (e: IOException){
            emit(Resource.Error<TopAnimes>(e.localizedMessage ?: "  Line 25 GetAnimesUseCase Problem in Internet connection."))
        }catch (e: Exception){
            emit(Resource.Error<TopAnimes>(e.localizedMessage ?: "  Line 27 GetAnimesUseCase This error occured"))
        }
    }
}