package com.cypress.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cypress.models.AlbumItem

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(albumItem: AlbumItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albumItems: List<AlbumItem>)

    @Query("SELECT * FROM album_tbl WHERE albumId =:albumId")
    fun getAlbum(albumId: Int): LiveData<List<AlbumItem>>

}