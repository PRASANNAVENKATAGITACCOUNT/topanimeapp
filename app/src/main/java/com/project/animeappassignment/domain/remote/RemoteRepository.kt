package com.project.animeappassignment.domain.remote

import com.project.animeappassignment.domain.remote.dto.topanime.AnimeDetailsDto
import com.project.animeappassignment.domain.remote.dto.topanime.DataDto
import com.project.animeappassignment.domain.remote.dto.topanime.TopAnimesDto
import com.project.animeappassignment.model.Data

interface RemoteRepository {

    suspend fun getTopAnimes(): TopAnimesDto

    suspend fun getAnimeDetails(animeId: Long): AnimeDetailsDto
}