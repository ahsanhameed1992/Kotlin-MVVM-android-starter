package com.task.starter.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.task.starter.*
import com.task.starter.data.Resource
import com.task.starter.data.dto.login.LoginRequest
import com.task.starter.data.dto.login.LoginResponse
import com.task.starter.data.dto.user.User
import com.task.starter.data.error.LOGIN_ERROR
import com.task.starter.data.error.PASS_WORD_ERROR
import javax.inject.Inject

class LocalData @Inject constructor(val context: Context) {

    fun doLogin(user: User): Resource<Boolean> {

        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_USER, 0)
        val cachedUserStr = sharedPref.getString(USER_KEY, "")
        if(!cachedUserStr.isNullOrEmpty()){
            val cachedUser = Gson().fromJson(cachedUserStr,User::class.java)
            if(user.username.equals(cachedUser.username) && user.password.equals(cachedUser.password))
                return Resource.Success(true)
        }
        return Resource.DataError(LOGIN_ERROR)
    }

    fun getCachedFavourites(): Resource<Set<String>> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        return Resource.Success(sharedPref.getStringSet(FAVOURITES_KEY, setOf()) ?: setOf())
    }

    fun isFavourite(id: String): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        val cache = sharedPref.getStringSet(FAVOURITES_KEY, setOf<String>()) ?: setOf()
        return Resource.Success(cache.contains(id))
    }

    fun cacheFavourites(ids: Set<String>): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putStringSet(FAVOURITES_KEY, ids)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }

    fun removeFromFavourites(id: String): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        var set = sharedPref.getStringSet(FAVOURITES_KEY, mutableSetOf<String>())?.toMutableSet() ?: mutableSetOf()
        if (set.contains(id)) {
            set.remove(id)
        }
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
        editor.commit()
        editor.putStringSet(FAVOURITES_KEY, set)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }

    fun isLogin(): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_LOGIN_STATUS, 0)
        val cache = sharedPref.getBoolean(LOGIN_KEY, false)
        return Resource.Success(cache)
    }
    fun updateLoginStatus(): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_LOGIN_STATUS, 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(LOGIN_KEY,true)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }

    fun removeAllData(): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_LOGIN_STATUS, 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(LOGIN_KEY,false)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }

    fun cacheUser(user: User): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_USER, 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val userStr = Gson().toJson(user)
        editor.putString(USER_KEY, userStr)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }

    fun removeUserFromCache(): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_USER, 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
        editor.commit()
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }
}

