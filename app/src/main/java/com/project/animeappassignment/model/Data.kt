package com.project.animeappassignment.model

import com.project.animeappassignment.domain.remote.dto.topanime.Genre
import com.project.animeappassignment.domain.remote.dto.topanime.Images
import com.project.animeappassignment.domain.remote.dto.topanime.Trailer

data class Data(
    val mal_id: Long,
    val title: String?,
    val episodes: Int?,
    val rating: String?,
    val images: Images?,
    val trailer: Trailer?,
    val genres: List<Genre?>?,
    val synopsis: String?,
){
    override fun toString(): String {
        return "Data(mal_id=$mal_id, title=$title, episodes=$episodes, rating=$rating, images=$images, trailer=$trailer, genres=$genres, synopsis=$synopsis)"
    }
}
