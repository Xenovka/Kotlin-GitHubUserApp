package com.dicoding.githubuser.ui

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.githubuser.apis.*
import com.dicoding.githubuser.data.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {
    val user = MutableLiveData<UserResponse>()
    val users = MutableLiveData<ArrayList<UserSearchResponseItems>>()
    val followers = MutableLiveData<ArrayList<UserSearchResponseItems>>()
    val following = MutableLiveData<ArrayList<UserSearchResponseItems>>()

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun setSearchUser(query: String) {
        ApiConfig.apiService
            .getUserSearch(query)
            .enqueue(object: Callback<UserSearchResponse> {
                override fun onResponse(
                    call: Call<UserSearchResponse>,
                    response: Response<UserSearchResponse>
                ) {
                    if(response.isSuccessful) {
                        users.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                    Log.d("ERROR", t.message.toString())
                }
            })
    }

    fun setUser(path: String?) {
        ApiConfig.apiService
            .getUser(path)
            .enqueue(object: Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if(response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("ERROR", t.message.toString())
                }
            })
    }

    fun setUserFollowers(path: String?) {
        ApiConfig.apiService
            .getUserFollowers(path)
            .enqueue(object: Callback<ArrayList<UserSearchResponseItems>> {
                override fun onResponse(
                    call: Call<ArrayList<UserSearchResponseItems>>,
                    response: Response<ArrayList<UserSearchResponseItems>>
                ) {
                    if(response.isSuccessful) {
                        followers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserSearchResponseItems>>, t: Throwable) {
                    Log.d("ERROR", t.message.toString())
                }
            })
    }

    fun setUserFollowings(path: String?) {
        ApiConfig.apiService
            .getUserFollowing(path)
            .enqueue(object: Callback<ArrayList<UserSearchResponseItems>> {
                override fun onResponse(
                    call: Call<ArrayList<UserSearchResponseItems>>,
                    response: Response<ArrayList<UserSearchResponseItems>>
                ) {
                    if(response.isSuccessful) {
                        following.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserSearchResponseItems>>, t: Throwable) {
                    Log.d("ERROR", t.message.toString())
                }
            })
    }

    fun getUser(): LiveData<UserResponse> {
        return user
    }

    fun getSearchUsers(): LiveData<ArrayList<UserSearchResponseItems>> {
        return users
    }

    fun getFollowers(): LiveData<ArrayList<UserSearchResponseItems>> {
        return followers
    }

    fun getFollowing(): LiveData<ArrayList<UserSearchResponseItems>> {
        return following
    }
}
