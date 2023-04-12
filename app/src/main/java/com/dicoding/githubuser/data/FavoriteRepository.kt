package com.dicoding.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.database.FavoriteDao
import com.dicoding.githubuser.database.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {

    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<Favorite>> = favoriteDao.getAllFavorite()

    fun addFavorite(favorite: Favorite) {
        executorService.execute {favoriteDao.addFavorite(favorite)}
    }

    fun removeFavorite(id: Int) = favoriteDao.removeFavorite(id)

    fun findById(id: Int) = favoriteDao.findById(id)
}