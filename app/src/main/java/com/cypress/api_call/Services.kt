package com.cypress.api_call

object Services {

    suspend fun getUsers() = ApiClient.client.getUsers()

    suspend fun getAlbums(albumId: Int) = ApiClient.client.getAlbums(albumId)

}