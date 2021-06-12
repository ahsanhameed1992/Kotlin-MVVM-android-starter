package com.task.starter.data.remote

import com.task.starter.data.Resource
import com.task.starter.data.dto.recipes.Recipes

internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<Recipes>
}
