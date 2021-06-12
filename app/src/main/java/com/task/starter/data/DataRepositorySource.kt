package com.task.starter.data

import com.task.starter.data.dto.recipes.Recipes
import com.task.starter.data.dto.login.LoginRequest
import com.task.starter.data.dto.login.LoginResponse
import com.task.starter.data.dto.user.User
import kotlinx.coroutines.flow.Flow

interface DataRepositorySource {
    suspend fun requestRecipes(): Flow<Resource<Recipes>>
    suspend fun addToFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun isFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun doLogin(user: User):  Flow<Resource<Boolean>>
    suspend fun registerUser(user: User): Flow<Resource<Boolean>>
    suspend fun isLogin(): Flow<Resource<Boolean>>
    suspend fun updateLoginStatus(): Flow<Resource<Boolean>>
    suspend fun removeAllCacheData(): Flow<Resource<Boolean>>
}
