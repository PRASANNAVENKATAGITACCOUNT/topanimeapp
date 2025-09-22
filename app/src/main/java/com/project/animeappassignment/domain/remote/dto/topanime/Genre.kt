package com.project.animeappassignment.domain.remote.dto.topanime

data class Genre(
    val mal_id: Long,
    val name: String?=null,
    val type: String?=null,
    val url: String?=null
)