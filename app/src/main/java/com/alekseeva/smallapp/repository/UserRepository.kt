package com.alekseeva.smallapp.repository

import com.alekseeva.smallapp.api.UserApi
import com.alekseeva.smallapp.model.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IUserRepository {
    suspend fun getUser(): Flow<UserProfile?>
    suspend fun saveUser(user: UserProfile): Boolean
}

class UserRepository @Inject constructor(private val api: UserApi) : IUserRepository {
    override suspend fun getUser() = api.getUser()

    override suspend fun saveUser(user: UserProfile)  = api.saveUser(user)
}