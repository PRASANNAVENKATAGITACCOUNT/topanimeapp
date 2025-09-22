package com.project.animeappassignment.model

data class TopAnimesState(
    val isLoading: Boolean=false,
    val topAnime: TopAnimes?=null,
    val error: String=""
){
    override fun toString(): String {
        return " Loading: $isLoading,  topAnime: $topAnime  Error $error"
    }
}
