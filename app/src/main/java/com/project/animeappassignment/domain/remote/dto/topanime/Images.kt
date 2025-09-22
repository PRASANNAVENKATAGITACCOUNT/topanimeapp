package com.project.animeappassignment.domain.remote.dto.topanime

import com.project.animeappassignment.model.Jpg
import com.project.animeappassignment.model.Webp

data class Images(
    val jpg: Jpg?=null,
    val webp: Webp?=null
)