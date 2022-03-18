package com.cypress.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cypress.models.UserItem

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userItem: UserItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userItems: List<UserItem>)

    @Query("SELECT * FROM user_tbl LIMIT :userInitial")
    fun getUsers(userInitial: Int): LiveData<List<UserItem>>

}