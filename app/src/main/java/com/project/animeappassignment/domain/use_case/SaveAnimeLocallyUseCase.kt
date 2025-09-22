package com.project.animeappassignment.domain.use_case

import coil.network.HttpException
import com.project.animeappassignment.common.Resource
import com.project.animeappassignment.domain.local.LocalRepository
import com.project.animeappassignment.model.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class SaveAnimeLocallyUseCase @Inject constructor (
    val localReposiroty: LocalRepository
) {

    operator fun invoke(data:List<Data>?) : Flow<Resource<String>> = flow {
        try{
            emit(Resource.Loading<String>("Saving animes"))
            val animeData = localReposiroty.saveAllDataEntities(data)
            emit(Resource.Success("Successfully Saved :"))
        }catch (e: HttpException){
            emit(Resource.Error<String>(e.localizedMessage ?: " Line 23 GetAnimesUseCase An unexpected error occured. "))
        }catch (e: IOException){
            emit(Resource.Error<String>(e.localizedMessage ?: "  Line 25 GetAnimesUseCase Problem in Internet connection."))
        }catch (e: Exception){
            emit(Resource.Error<String>(e.localizedMessage ?: "  Line 27 GetAnimesUseCase This error occured"))
        }
    }
}