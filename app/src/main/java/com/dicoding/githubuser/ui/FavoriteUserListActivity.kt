package com.dicoding.githubuser.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapters.UserListAdapter
import com.dicoding.githubuser.apis.UserSearchResponseItems
import com.dicoding.githubuser.data.SettingPreferences
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.databinding.ActivityFavoriteUserListBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteUserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var fViewModel: FavoriteViewModel
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userListAdapter = UserListAdapter()
        val pref = SettingPreferences.getInstance(dataStore)
        viewModel = obtainViewModel(this@FavoriteUserListActivity, pref)
        fViewModel = obtainFavoriteViewModel(this@FavoriteUserListActivity)
        viewModel.getThemeSettings().observe(this) { isDarkActive: Boolean ->
            if (isDarkActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.apply {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteUserListActivity)
            rvFavorite.setHasFixedSize(true)
            rvFavorite.adapter = userListAdapter
        }

        userListAdapter.setOnItemClickCallback(object: UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserSearchResponseItems) {
                val userDetailIntent = Intent(this@FavoriteUserListActivity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.login)
                startActivity(userDetailIntent)
            }
        })

        loading(true)

        fViewModel.getAllFavorite().observe(this) {
            if(it != null) {
                loading(false)
                if(it.isEmpty()) {
                    binding.tvEmptyFavorite.visibility = View.VISIBLE
                }
                val list = mapFavoriteUser(it)
                userListAdapter.setUsers(list)

            }
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity, preferences: SettingPreferences): MainViewModel {
        val factory = ViewModelFactory.getInstance(preferences)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    private fun obtainFavoriteViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingFavorite.visibility = View.VISIBLE
        } else {
            binding.loadingFavorite.visibility = View.INVISIBLE
        }
    }

    private fun mapFavoriteUser(users: List<Favorite>): ArrayList<UserSearchResponseItems> {
        val favoriteUsers = ArrayList<UserSearchResponseItems>()
        for (user in users) {
            val mappedFavoriteUser = UserSearchResponseItems(user.id, user.login, user.avatarUrl)
            favoriteUsers.add(mappedFavoriteUser)
        }

        return favoriteUsers
    }
}