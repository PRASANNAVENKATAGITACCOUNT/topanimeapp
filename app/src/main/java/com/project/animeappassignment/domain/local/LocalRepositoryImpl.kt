package com.project.animeappassignment.domain.local

import android.util.Log
import com.project.animeappassignment.domain.local.model.DataEntity
import com.project.animeappassignment.domain.local.model.GenreEntity
import com.project.animeappassignment.domain.local.model.ImagesEntity
import com.project.animeappassignment.domain.local.model.TrailerEntity
import com.project.animeappassignment.domain.remote.dto.topanime.Genre
import com.project.animeappassignment.domain.remote.dto.topanime.Images
import com.project.animeappassignment.domain.remote.dto.topanime.Trailer
import com.project.animeappassignment.model.Data
import com.project.animeappassignment.model.Jpg
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    val dao: AnimesDAO
) : LocalRepository {
    override suspend fun getAllDataEntities(): List<Data> {
        val dataList = mutableListOf<Data>()
        val genricsList = dao.getAllDataWithGenrics().map { genreEntity ->
            val entity = genreEntity.data
            val genres = genreEntity.genrelists
            val trailer = dao.getTrailerEntity(entity.dataId)
            val image = dao.getImageEntity(entity.dataId)
            val genreList = genres.map { g ->
                Genre(
                    mal_id = g.mal_id,
                    name = g.name,
                )
            }
            val data = Data(
                mal_id = entity.mal_id,
                episodes = entity.episodes,
                title = entity.title,
                synopsis = entity.synopsis,
                rating = entity.ratings,
                images = Images(
                    jpg = Jpg(
                        image_url = image.imageUrl
                    )
                ),
                trailer = Trailer(youtube_id = trailer.youtube_id),
                genres = genreList
            )
            dataList.add(data)
        }
        return dataList
    }

    override suspend fun saveAllDataEntities(dataList: List<Data>?) {
        if(dataList ==null){
            return
        }
        clearAllDataFromDatabase(dao)
        dataList.forEach { data ->
            val dataEntity = DataEntity(
                dataId = 0L,
                mal_id = data.mal_id,
                title = data.title,
                episodes = data.episodes,
                ratings = data.rating,
                synopsis = data.synopsis
            )

            val dataId = dao.insertData(dataEntity)
            Log.d("Data_Inserted", " Data Saved Id : $dataId ")

            val trailer = TrailerEntity(
                trailerId = 0L,
                dataId = dataId,
                youtube_id = data.trailer?.youtube_id ?: ""
            )
            dao.insertTrailerEntity(trailer)
            Log.d("Data_Inserted", " trailer saved : $trailer ")

            val images = ImagesEntity(
                imagesId = 0L,
                dataId = dataId,
                imageUrl = data.images?.jpg?.image_url ?: ""
            )
            dao.insertImageEntity(images)
            Log.d("Data_Inserted", " images saved : $trailer ")
            val genreList = data.genres?.map { genre ->
                GenreEntity(
                    genreId = 0L,
                    dataId = dataId,
                    mal_id = genre?.mal_id ?: 0L,
                    name = genre?.name ?: ""
                )

            } ?: emptyList()
            Log.d("Data_Inserted", " saved  genere: $genreList ")
            dao.insertGenresEntity(genreList)
        }
        Log.d("Data_Inserted", "Saved Successfully: $dataList")

    }


    override suspend fun getDataOnId(dataId: Long): Data? {
        val dataList = mutableListOf<Data>()
        dao.getDataWithIdGenrics(dataId).map { genreEntity ->
            val entity = genreEntity.data
            val genres = genreEntity.genrelists
            val trailer = dao.getTrailerEntity(entity.dataId)
            val image = dao.getImageEntity(entity.dataId)
            val genreList = genres.map { g ->
                Genre(
                    mal_id = g.mal_id,
                    name = g.name,
                )
            }
            val data = Data(
                mal_id = entity.mal_id,
                episodes = entity.episodes,
                title = entity.title,
                synopsis = entity.synopsis,
                rating = entity.ratings,
                images = Images(
                    jpg = Jpg(
                        image_url = image.imageUrl
                    )
                ),
                trailer = Trailer(youtube_id = trailer.youtube_id),
                genres = genreList
            )
            dataList.add(data)
        }
        return dataList[0]
    }


    private suspend fun clearAllDataFromDatabase(dao: AnimesDAO) {
        dao.deleteAllImages()
        dao.deleteAllTrailerEntities()
        dao.deleteAllGenres()
        dao.deleteAllData()
    }




}