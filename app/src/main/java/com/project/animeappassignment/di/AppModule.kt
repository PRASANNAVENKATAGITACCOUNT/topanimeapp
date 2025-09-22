package com.project.animeappassignment.di

import android.content.Context
import androidx.room.Room
import com.project.animeappassignment.common.Constants.BASE_URL
import com.project.animeappassignment.common.NetworkConnectivityObserver
import com.project.animeappassignment.domain.local.AnimeDatabase
import com.project.animeappassignment.domain.local.AnimesDAO
import com.project.animeappassignment.domain.local.LocalRepository
import com.project.animeappassignment.domain.local.LocalRepositoryImpl
import com.project.animeappassignment.domain.remote.JikanAPI
import com.project.animeappassignment.domain.remote.RemoteRepository
import com.project.animeappassignment.domain.remote.RemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideAnimeDatabase(@ApplicationContext context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            "anime_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAnimeDao(database: AnimeDatabase): AnimesDAO {
        return database.animeDAO()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(animesDAO: AnimesDAO): LocalRepository {
        return LocalRepositoryImpl(animesDAO)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }





    @Provides
    @Singleton
    fun providesJikanAPI() : JikanAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(JikanAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesRemoteRepository(jikanAPI: JikanAPI) : RemoteRepository{
        return RemoteRepositoryImpl(jikanAPI)
    }




}