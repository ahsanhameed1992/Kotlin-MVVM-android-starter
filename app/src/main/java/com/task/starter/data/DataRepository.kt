package com.task.starter.data

import com.task.starter.data.dto.login.LoginRequest
import com.task.starter.data.dto.login.LoginResponse
import com.task.starter.data.dto.recipes.Recipes
import com.task.starter.data.dto.user.User
import com.task.starter.data.local.LocalData
import com.task.starter.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class DataRepository @Inject constructor(private val remoteRepository: RemoteData, private val localRepository: LocalData, private val ioDispatcher: CoroutineContext) : DataRepositorySource {

    override suspend fun requestRecipes(): Flow<Resource<Recipes>> {
        return flow {
            emit(remoteRepository.requestRecipes())
        }.flowOn(ioDispatcher)
    }

    override suspend fun addToFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            localRepository.getCachedFavourites().let {
                it.data?.toMutableSet()?.let { set ->
                    val isAdded = set.add(id)
                    if (isAdded) {
                        emit(localRepository.cacheFavourites(set))
                    } else {
                        emit(Resource.Success(false))
                    }
                }
                it.errorCode?.let { errorCode ->
                    emit(Resource.DataError<Boolean>(errorCode))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.removeFromFavourites(id))
        }.flowOn(ioDispatcher)
    }

    override suspend fun isFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.isFavourite(id))
        }.flowOn(ioDispatcher)
    }

    override suspend fun doLogin(user: User): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.doLogin(user))
        }.flowOn(ioDispatcher)
    }


    override suspend fun registerUser(user: User): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.cacheUser(user))
        }.flowOn(ioDispatcher)
    }




    override suspend fun isLogin(): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.isLogin())
        }.flowOn(ioDispatcher)
    }

    override suspend fun updateLoginStatus(): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.updateLoginStatus())
        }.flowOn(ioDispatcher)
    }

    override suspend fun removeAllCacheData(): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.removeAllData())
        }.flowOn(ioDispatcher)
    }
}
