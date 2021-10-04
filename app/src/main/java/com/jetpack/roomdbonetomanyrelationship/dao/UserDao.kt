package com.jetpack.roomdbonetomanyrelationship.dao

import androidx.room.*
import com.jetpack.roomdbonetomanyrelationship.entity.Library
import com.jetpack.roomdbonetomanyrelationship.entity.User
import com.jetpack.roomdbonetomanyrelationship.entity.UserAndLibrary

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(item: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibrary(item: List<Library>)

    @Transaction
    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUserAndLibraries(userId: Int): List<UserAndLibrary>
}