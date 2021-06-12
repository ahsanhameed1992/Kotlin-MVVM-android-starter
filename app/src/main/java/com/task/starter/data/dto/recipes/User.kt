package com.task.starter.data.dto.recipes


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    @SerializedName("email")
    val email: String = "",
    @SerializedName("latlng")
    val latlng: String = "",
    @SerializedName("name")
    val name: String = ""
) : Parcelable
