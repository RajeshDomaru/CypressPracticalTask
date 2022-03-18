package com.cypress.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_tbl")
data class UserItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String?,
    var userId: Int?
)