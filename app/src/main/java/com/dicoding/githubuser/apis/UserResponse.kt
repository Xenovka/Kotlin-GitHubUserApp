package com.dicoding.githubuser.apis

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse (
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int
)

data class UserSearchResponse (

	@field:SerializedName("items")
	val items: ArrayList<UserSearchResponseItems>
)

@Parcelize
data class UserSearchResponseItems (
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

): Parcelable

