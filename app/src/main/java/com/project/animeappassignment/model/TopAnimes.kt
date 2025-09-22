package com.project.animeappassignment.model

import com.project.animeappassignment.domain.remote.dto.topanime.Pagination

class TopAnimes(
    val data: List<Data>?,
    val pagination: Pagination?=null
){
    override fun toString(): String {
        return "TopAnimes(data=$data, pagination=$pagination)"
    }
}