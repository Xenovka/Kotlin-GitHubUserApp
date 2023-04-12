package com.dicoding.githubuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.apis.UserSearchResponseItems

class UserListAdapter: RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(data: UserSearchResponseItems)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val usersList = ArrayList<UserSearchResponseItems>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivUserImage: ImageView = view.findViewById(R.id.iv_user_image)
        val tvUserName: TextView = view.findViewById(R.id.tv_user_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.github_user, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(usersList[position].avatarUrl)
            .centerCrop()
            .into(holder.ivUserImage)

        holder.tvUserName.text = usersList[position].login

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(usersList[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = usersList.size

    fun setUsers(users: ArrayList<UserSearchResponseItems>) {
        usersList.clear()
        usersList.addAll(users)
        notifyDataSetChanged()
    }
}