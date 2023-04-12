package com.dicoding.githubuser.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.FavoriteRepository
import com.dicoding.githubuser.database.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteViewModel(application: Application): ViewModel() {
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    suspend fun addFavorite(favorite: Favorite) {
        return withContext(Dispatchers.IO) {
            favoriteRepository.addFavorite(favorite)
        }
    }

    suspend fun removeFavorite(id: Int): Int {
        return withContext(Dispatchers.IO) {
            favoriteRepository.removeFavorite(id)
        }
    }

    suspend fun findById(id: Int): Int {
        return withContext(Dispatchers.IO) {
            favoriteRepository.findById(id)
        }
    }

    fun getAllFavorite(): LiveData<List<Favorite>> = favoriteRepository.getAllFavorite()
}