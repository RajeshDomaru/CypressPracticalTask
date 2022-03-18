package com.cypress.api_call

import com.cypress.models.AlbumItem
import com.cypress.models.UserItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(USERS)
    suspend fun getUsers(): Response<ArrayList<UserItem>?>?

    @GET(ALBUMS)
    suspend fun getAlbums(@Query("albumId") albumId: Int): Response<ArrayList<AlbumItem>?>?

}