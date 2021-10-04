package com.jetpack.roomdbonetomanyrelationship.repository

import androidx.lifecycle.LiveData
import com.jetpack.roomdbonetomanyrelationship.dao.UserDao
import com.jetpack.roomdbonetomanyrelationship.entity.Library
import com.jetpack.roomdbonetomanyrelationship.entity.User
import com.jetpack.roomdbonetomanyrelationship.entity.UserAndLibrary

class UserRepository(private val userDao: UserDao) {
    var readAllData: LiveData<List<UserAndLibrary>>? = null

    suspend fun addUser(item: List<User>) {
        userDao.insertUser(item = item)
    }

    suspend fun addLibrary(item: List<Library>) {
        userDao.insertLibrary(item = item)
    }

    fun getUserData(userId: Int): List<UserAndLibrary> {
        return userDao.getUserAndLibraries(userId = userId)
    }
}