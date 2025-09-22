package com.project.animeappassignment.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.animeappassignment.domain.local.model.DataEntity
import com.project.animeappassignment.domain.local.model.ImagesEntity
import com.project.animeappassignment.domain.local.model.TrailerEntity
import com.project.animeappassignment.domain.local.model.GenreEntity

@Database(entities =
    [
        DataEntity::class,
        TrailerEntity::class,
        ImagesEntity::class,
        GenreEntity::class,
    ], version = 1, exportSchema = false)
abstract class  AnimeDatabase() : RoomDatabase() {

    abstract fun animeDAO(): AnimesDAO
}
