package com.cypress.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cypress.models.AlbumItem
import com.cypress.models.UserItem

@Database(entities = [UserItem::class, AlbumItem::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun albumDao(): AlbumDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }

}