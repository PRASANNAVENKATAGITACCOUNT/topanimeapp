package com.project.animeappassignment.domain.remote

import com.project.animeappassignment.domain.remote.dto.topanime.AnimeDetailsDto
import com.project.animeappassignment.domain.remote.dto.topanime.DataDto
import com.project.animeappassignment.domain.remote.dto.topanime.TopAnimesDto
import com.project.animeappassignment.model.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject


class RemoteRepositoryImpl @Inject constructor(
    val api: JikanAPI
) : RemoteRepository {
    override suspend fun getTopAnimes(): TopAnimesDto {
        return api.getAllTopAnime()
    }

    override suspend fun getAnimeDetails(animeId: Long): AnimeDetailsDto {
        return api.getAnimeDetails(animeId)
    }
}