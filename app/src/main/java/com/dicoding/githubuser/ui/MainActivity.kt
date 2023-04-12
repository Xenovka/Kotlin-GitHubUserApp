package com.dicoding.githubuser.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.UserListAdapter
import com.dicoding.githubuser.apis.UserSearchResponseItems
import com.dicoding.githubuser.data.SettingPreferences
import com.dicoding.githubuser.databinding.ActivityMainBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "GitHub User"
        val pref = SettingPreferences.getInstance(dataStore)
        userListAdapter = UserListAdapter()
        viewModel = obtainViewModel(this@MainActivity, pref)

        viewModel.getThemeSettings().observe(this) { isDarkActive: Boolean ->
            if (isDarkActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.apply {
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = userListAdapter

            ivSearchIcon.setOnClickListener {
                searchUser()
            }
        }

        userListAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserSearchResponseItems) {
                val userDetailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.login)
                startActivity(userDetailIntent)
            }
        })

        viewModel.getSearchUsers().observe(this) {
            if (it != null) {
                userListAdapter.setUsers(it)
                loading(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_list_page -> {
                val favoriteUserListIntent = Intent(this, FavoriteUserListActivity::class.java)
                startActivity(favoriteUserListIntent)
                true
            }
            R.id.change_theme_page -> {
                val changeThemeIntent = Intent(this, ThemeSettingActivity::class.java)
                startActivity(changeThemeIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searchUser() {
        binding.apply {
            when{
                TextUtils.isEmpty(etUserName.text.toString().trim()) -> {
                    etUserName.error = "This field can not be empty"
                }
                else -> {
                    val query = etUserName.text.toString()
                    if (query.isEmpty()) return

                    loading(true)
                    viewModel.setSearchUser(query)
                }
            }

        }
    }

    private fun obtainViewModel(activity: AppCompatActivity, preferences: SettingPreferences): MainViewModel {
        val factory = ViewModelFactory.getInstance(preferences)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.INVISIBLE
        }
    }
}