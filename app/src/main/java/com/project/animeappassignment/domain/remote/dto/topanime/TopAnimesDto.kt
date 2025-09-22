package com.project.animeappassignment.domain.remote.dto.topanime

import com.project.animeappassignment.model.Links
import com.project.animeappassignment.model.Meta
import com.project.animeappassignment.model.TopAnimes

data class TopAnimesDto(
    val `data`: List<DataDto>,
    val links: Links,
    val meta: Meta,
    val pagination: Pagination
)

fun TopAnimesDto.createTopAnimes() : TopAnimes{
    return TopAnimes(
        data= data.map { it-> it.createData() },
        links=links,
        pagination=pagination
    )
}