package com.example.fontcraftpro.di

import android.content.Context
import androidx.room.Room
import com.example.fontcraftpro.data.local.AppDatabase
import com.example.fontcraftpro.data.local.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "fontcraftpro.db"
        ).build()

    @Provides
    fun provideProjectDao(database: AppDatabase): ProjectDao = database.projectDao()
}
