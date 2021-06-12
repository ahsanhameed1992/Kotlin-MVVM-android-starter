package com.task.starter

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.task.starter.data.dto.recipes.Recipes
import com.task.starter.data.dto.recipes.RecipesItem
import java.io.InputStream

object TestUtil {
    var dataStatus: DataStatus = DataStatus.Success
    var recipes: Recipes = Recipes(arrayListOf())
    fun initData(): Recipes {

        val jsonString = getJson("RecipesApiResponse.json")
        val listType = object : TypeToken<List<RecipesItem?>?>() {}.type
        val recipesList: List<RecipesItem> =  Gson().fromJson(jsonString, listType)

        recipesList.let {
            recipes = Recipes(ArrayList(it))
            return recipes
        }
        return Recipes(arrayListOf())
    }

    private fun getJson(path: String): String {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}
