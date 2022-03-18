package com.cypress.repository

import com.cypress.db.UserDao
import com.cypress.models.UserItem
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun insert(userItem: UserItem) = userDao.insert(userItem)

    suspend fun insertAll(userItems: List<UserItem>) = userDao.insertAll(userItems)

    fun getUsers(limit: Int) = userDao.getUsers(limit)

}