package com.cypress.models

import androidx.lifecycle.LiveData

data class Photo(val userItem: UserItem, var albumItems: LiveData<List<AlbumItem>>?)
