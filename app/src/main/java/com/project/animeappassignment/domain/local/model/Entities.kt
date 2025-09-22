package com.project.animeappassignment.domain.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity
data class DataEntity (
    @PrimaryKey(autoGenerate = true)
    val dataId:Long,
    val mal_id: Long,
    val title: String?="",
    val episodes: Int?=0,
    val ratings: String?="",
    val synopsis: String?=""
)

@Entity
data class ImagesEntity(
    @PrimaryKey(autoGenerate = true)
    val imagesId: Long,
    val dataId:Long=0L,
    val imageUrl : String=""
)

@Entity
data class TrailerEntity(
    @PrimaryKey(autoGenerate = true)
    val trailerId: Long,
    val dataId:Long=0L,
    val youtube_id : String=""
)

@Entity
data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    val genreId: Long,
    val dataId : Long=0L,
    val mal_id: Long,
    val name : String=""
)

data class DataWithGenres(
    @Embedded val data: DataEntity,
    @Relation(
        parentColumn = "dataId",
        entityColumn = "dataId"
    )
    val genrelists: List<GenreEntity>
)

