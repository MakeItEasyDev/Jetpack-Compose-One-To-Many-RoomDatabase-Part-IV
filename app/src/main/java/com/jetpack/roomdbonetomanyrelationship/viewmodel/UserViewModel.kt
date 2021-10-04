package com.jetpack.roomdbonetomanyrelationship.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.jetpack.roomdbonetomanyrelationship.database.UserDatabase
import com.jetpack.roomdbonetomanyrelationship.entity.Library
import com.jetpack.roomdbonetomanyrelationship.entity.User
import com.jetpack.roomdbonetomanyrelationship.entity.UserAndLibrary
import com.jetpack.roomdbonetomanyrelationship.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val _readAllData = MutableLiveData<List<UserAndLibrary>>()
    var readAllData: LiveData<List<UserAndLibrary>> = _readAllData
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getInstance(application).userDao()
        repository = UserRepository(userDao)
    }

    fun getUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _readAllData.postValue(repository.getUserData(userId))
        }
    }

    fun addUser(item: List<User>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(item)
        }
    }

    fun addLibrary(item: List<Library>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLibrary(item)
        }
    }
}

class UserViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(application) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}


















