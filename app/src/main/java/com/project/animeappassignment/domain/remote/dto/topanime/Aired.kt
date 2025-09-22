package com.project.animeappassignment.domain.remote.dto.topanime

import com.project.animeappassignment.model.Prop

data class Aired(
    val from: String,
    val prop: Prop,
    val string: String,
    val to: String
)