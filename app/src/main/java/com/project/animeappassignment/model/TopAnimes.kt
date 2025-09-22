package com.project.animeappassignment.model

import com.project.animeappassignment.domain.remote.dto.topanime.Pagination

class TopAnimes(
    val data: List<Data>?,
    val links: Links?=null,
    val pagination: Pagination?=null
)