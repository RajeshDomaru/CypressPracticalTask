package com.cypress.view_models

import androidx.lifecycle.ViewModel
import com.cypress.models.AlbumItem
import com.cypress.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(private val albumRepository: AlbumRepository) :
    ViewModel() {

    suspend fun insert(albumItem: AlbumItem) = albumRepository.insert(albumItem)

    suspend fun insertAll(albumItems: List<AlbumItem>) = albumRepository.insertAll(albumItems)

    fun getAlbums(albumId: Int) = albumRepository.getAlbums(albumId)

}