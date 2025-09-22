package com.project.animeappassignment.domain.local

import com.project.animeappassignment.domain.local.model.DataEntity
import com.project.animeappassignment.model.Data

interface LocalRepository {

   suspend fun getAllDataEntities(): List<Data>

   suspend fun saveAllDataEntities(dataList: List<Data>?)

   suspend fun getDataOnId(dataId: Long) : Data?
}