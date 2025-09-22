package com.project.animeappassignment.domain.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.project.animeappassignment.domain.local.model.DataEntity
import com.project.animeappassignment.domain.local.model.DataWithGenres
import com.project.animeappassignment.domain.local.model.GenreEntity
import com.project.animeappassignment.domain.local.model.ImagesEntity
import com.project.animeappassignment.domain.local.model.TrailerEntity

@Dao
interface AnimesDAO {
    @Insert
    suspend fun insertData(data: DataEntity) : Long
    @Insert
    suspend fun insertGenresEntity(data: List<GenreEntity>)
    @Transaction
    @Query("SELECT * FROM DataEntity")
    suspend fun getAllDataWithGenrics(): List<DataWithGenres>

    @Transaction
    @Query("SELECT * FROM DataEntity where mal_id =:data_id")
    suspend fun getDataWithIdGenrics(data_id:Long): List<DataWithGenres>
    @Insert
    suspend fun insertTrailerEntity(trailer: TrailerEntity)
    @Insert
    suspend fun insertImageEntity(imageEntity: ImagesEntity)
    @Query("SELECT * FROM TrailerEntity Where dataId =:data_id")
    suspend fun getTrailerEntity(data_id:Long): TrailerEntity
    @Query("SELECT * FROM ImagesEntity Where dataId =:data_id")
    suspend fun getImageEntity(data_id:Long): ImagesEntity


    @Query("DELETE FROM GenreEntity")
    suspend fun deleteAllGenres()

    @Query("DELETE FROM TrailerEntity")
    suspend fun deleteAllTrailerEntities()


    @Query("DELETE FROM ImagesEntity")
    suspend fun deleteAllImages()

    @Query("DELETE FROM DataEntity")
    suspend fun deleteAllData()




}