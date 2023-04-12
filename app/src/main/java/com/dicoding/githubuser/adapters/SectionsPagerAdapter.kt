package com.dicoding.githubuser.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuser.ui.FollowerFragment
import com.dicoding.githubuser.ui.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, username: Bundle): FragmentStateAdapter(activity) {

    private var bUsername = Bundle()

    init {
        bUsername = username
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }

        fragment?.arguments = this.bUsername
        return fragment as Fragment
    }

}