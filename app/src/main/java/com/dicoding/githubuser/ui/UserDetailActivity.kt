package com.dicoding.githubuser.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.SectionsPagerAdapter
import com.dicoding.githubuser.data.SettingPreferences
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var fViewModel: FavoriteViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_name_1,
            R.string.tab_name_2
        )

        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        viewModel = obtainViewModel(this@UserDetailActivity, pref)
        fViewModel = obtainFavoriteViewModel(this@UserDetailActivity)
        viewModel.getThemeSettings().observe(this) { isDarkActive: Boolean ->
            if (isDarkActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle().apply {
            putString(EXTRA_USERNAME, username)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        supportActionBar?.elevation = 0f

        loading(true)

        viewModel.setUser(username)
        viewModel.getUser().observe(this) {
            if (it != null) {
                binding.apply {
                    Glide.with(this@UserDetailActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(ivProfilePicture)

                    tvUserName.text = it.login
                    tvName.text = it.name
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowings.text = "${it.following} Following"
                }

                loading(false)
            }

            var isFavorite = false

            binding.apply {
                lifecycleScope.launch {
                    if(fViewModel.findById(it.id) > 0) {
                        btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.baseline_favorite_24))
                        isFavorite = true
                    }

                    btnFavorite.setOnClickListener { view ->
                        lifecycleScope.launch {
                            if(isFavorite && fViewModel.findById(it.id) > 0) {
                                fViewModel.removeFavorite(it.id)
                                btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.baseline_favorite_border_24))
                                isFavorite = false
                            } else {
                                fViewModel.addFavorite(Favorite(it.id, it.login, it.avatarUrl))
                                btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.baseline_favorite_24))
                                isFavorite = true
                            }
                        }
                    }
                }
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
            binding.pbDetails.visibility = View.VISIBLE
        } else {
            binding.pbDetails.visibility = View.INVISIBLE
        }
    }
}