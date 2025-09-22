package com.project.animeappassignment.model

data class AnimeDataState (
    val isLoading: Boolean=false,
    val animeData: Data?=null,
    val error: String=""
)