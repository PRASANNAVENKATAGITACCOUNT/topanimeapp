package com.project.animeappassignment.domain.remote

import com.project.animeappassignment.domain.remote.dto.topanime.AnimeDetailsDto
import com.project.animeappassignment.domain.remote.dto.topanime.DataDto
import com.project.animeappassignment.domain.remote.dto.topanime.TopAnimesDto
import com.project.animeappassignment.model.Data
import retrofit2.http.GET
import retrofit2.http.Path


interface JikanAPI {

    @GET("top/anime")
    suspend fun getAllTopAnime(): TopAnimesDto

    @GET("anime/{id}")
    suspend fun getAnimeDetails(@Path("id") anime_id : Long) : AnimeDetailsDto

}