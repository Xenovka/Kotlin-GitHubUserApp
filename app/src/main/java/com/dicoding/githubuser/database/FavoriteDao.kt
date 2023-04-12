package com.dicoding.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Favorite::class)
    fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    fun removeFavorite(id: Int): Int

    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): LiveData<List<Favorite>>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE favorite.id = :id)")
    fun findById(id: Int): Int
}