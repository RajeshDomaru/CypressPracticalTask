package com.cypress.repository

import com.cypress.db.AlbumDao
import com.cypress.models.AlbumItem
import javax.inject.Inject

class AlbumRepository @Inject constructor(private val albumDao: AlbumDao) {

    suspend fun insert(albumItem: AlbumItem) = albumDao.insert(albumItem)

    suspend fun insertAll(albumItems: List<AlbumItem>) = albumDao.insertAll(albumItems)

    fun getAlbums(albumId: Int) = albumDao.getAlbum(albumId)

}