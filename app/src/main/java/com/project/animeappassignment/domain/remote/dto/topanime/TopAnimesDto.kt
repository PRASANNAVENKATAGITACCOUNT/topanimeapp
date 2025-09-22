package com.project.animeappassignment.domain.remote.dto.topanime

import com.google.gson.annotations.SerializedName
import com.project.animeappassignment.model.TopAnimes

data class TopAnimesDto(
    val pagination: Pagination,

    @SerializedName("data")
    val data: List<DataDto>

)

fun TopAnimesDto.createTopAnimes() : TopAnimes{
    return TopAnimes(
        data= data.map { it-> it.createData() },
        pagination=pagination
    )
}