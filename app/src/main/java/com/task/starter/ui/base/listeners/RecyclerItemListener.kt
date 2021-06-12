package com.task.starter.ui.base.listeners

import com.task.starter.data.dto.recipes.RecipesItem

interface RecyclerItemListener {
    fun onItemSelected(recipe : RecipesItem)
}
