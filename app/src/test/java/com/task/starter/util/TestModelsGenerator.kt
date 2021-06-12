package com.task.starter.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.task.starter.data.dto.recipes.Recipes
import com.task.starter.data.dto.recipes.RecipesItem
import java.io.File


class TestModelsGenerator {
    private var recipes: Recipes = Recipes(arrayListOf())

    init {

        val jsonString = getJson("RecipesApiResponse.json")
        val listType = object : TypeToken<List<RecipesItem?>?>() {}.type
        val recipesList: List<RecipesItem> =  Gson().fromJson(jsonString, listType)
        recipesList?.let {
            recipes = Recipes(ArrayList(it))
        }
        print("this is $recipes")
    }

    fun generateRecipes(): Recipes {
        return recipes
    }

    fun generateRecipesModelWithEmptyList(): Recipes {
        return Recipes(arrayListOf())
    }

    fun generateRecipesItemModel(): RecipesItem {
        return recipes.recipesList[0]
    }

    fun getStubSearchTitle(): String {
        return recipes.recipesList[0].name
    }


    /**
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */

    private fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}
