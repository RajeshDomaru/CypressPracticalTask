package com.cypress.view_models

import androidx.lifecycle.ViewModel
import com.cypress.models.UserItem
import com.cypress.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    suspend fun insert(userItem: UserItem) = userRepository.insert(userItem)

    suspend fun insertAll(userItems: List<UserItem>) = userRepository.insertAll(userItems)

    fun getUsers(limit: Int) = userRepository.getUsers(limit)

}