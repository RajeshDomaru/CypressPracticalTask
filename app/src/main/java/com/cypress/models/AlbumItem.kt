package com.cypress.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_tbl")
data class AlbumItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var albumId: Int = 0,
    var thumbnailUrl: String?,
    var title: String?,
    var url: String?
)