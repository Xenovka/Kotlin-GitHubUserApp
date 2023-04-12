package com.dicoding.githubuser.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import com.dicoding.githubuser.databinding.FragmentFollowBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FollowingFragment : Fragment(R.layout.fragment_follow) {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var userListAdapter: UserListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowBinding.bind(view)

        userListAdapter = UserListAdapter()
        userListAdapter.notifyDataSetChanged()

        binding.apply {
            rvFollow.layoutManager = LinearLayoutManager(activity)
            rvFollow.setHasFixedSize(true)
            rvFollow.adapter = userListAdapter
        }

        loading(true)

        val pref = SettingPreferences.getInstance(context?.dataStore!!)
        viewModel = obtainViewModel(requireActivity() as AppCompatActivity, pref)
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkActive: Boolean ->
            if (isDarkActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val username = arguments?.getString(UserDetailActivity.EXTRA_USERNAME).toString()
        viewModel.setUserFollowings(username)
        viewModel.getFollowing().observe(viewLifecycleOwner) {
            if (it != null) {
                userListAdapter.setUsers(it)
                loading(false)
                if(it.size <= 0) {
                    binding.tvNothing.visibility = View.VISIBLE
                    binding.tvNothing.text = resources.getString(R.string.no_followings)
                }

                userListAdapter.setOnItemClickCallback(object: UserListAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: UserSearchResponseItems) {
                        val userDetailIntent = Intent(requireActivity(), UserDetailActivity::class.java)
                        userDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.login)
                        startActivity(userDetailIntent)
                    }
                })
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity, preferences: SettingPreferences): MainViewModel {
        val factory = ViewModelFactory.getInstance(preferences)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbFollow.visibility = View.VISIBLE
        } else {
            binding.pbFollow.visibility = View.INVISIBLE
        }
    }
}