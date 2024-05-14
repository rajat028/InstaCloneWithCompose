package com.example.instaclonewithcompose.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.room.Room
import com.example.instaclonewithcompose.data.room.InstaDB
import com.example.instaclonewithcompose.data.repository.UserRepositoryImpl
import com.example.instaclonewithcompose.domain.repository.UserRepository
import com.example.instaclonewithcompose.domain.usecases.LoginUseCase
import com.example.instaclonewithcompose.domain.usecases.SignUpUseCase
import com.example.instaclonewithcompose.utils.PrefConstants.INSTA_PREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    
    @Provides
    @Singleton
    fun providesDatabase(app: Application): InstaDB {
        return Room.databaseBuilder(
            app,
            InstaDB::class.java,
            InstaDB.DATABASE_NAME
        ).build()
    }
    
    @Provides
    @Singleton
    fun providesNotesRepository(database: InstaDB): UserRepository {
        return UserRepositoryImpl(database.userDao)
    }
    
    @Provides
    @Singleton
    fun providesSignUpUseCase(repository: UserRepository): SignUpUseCase {
        return SignUpUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun providesLogInUseCase(repository: UserRepository): LoginUseCase {
        return LoginUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideSharedPref(app: Application) : SharedPreferences {
        return app.getSharedPreferences(INSTA_PREF, Context.MODE_PRIVATE)
    }
}