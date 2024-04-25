package com.alekseeva.smallapp.di

import android.content.Context
import com.alekseeva.smallapp.api.IUserApi
import com.alekseeva.smallapp.api.UserApi
import com.alekseeva.smallapp.repository.IUserRepository
import com.alekseeva.smallapp.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesUserApi(@ApplicationContext context: Context): IUserApi = UserApi(context)

    @Provides
    fun providesUserRepository(api: UserApi): IUserRepository = UserRepository(api)
}