package com.example.passwordmanagerapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities
import com.example.passwordmanagerapp.state.MainActivityUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {


    private val _state = MutableStateFlow(MainActivityUiState())
    val state: StateFlow<MainActivityUiState> get() = _state.asStateFlow()

    fun insertUserDataToDb(userPasswordEntities: UserPasswordEntities) {
        CoroutineScope(Dispatchers.IO).launch {

            ApplicationClass.database.userDao().insertUser(userPasswordEntities)

            _state.update { it.copy(passwordDataList = getAllUserFromDb()) }

        }

    }

    private suspend fun getAllUserFromDb(): List<UserPasswordEntities> {
        return withContext(Dispatchers.IO) {
            ApplicationClass.database.userDao().getAllUsers()
        }

    }

    fun fetchData() {
        viewModelScope.launch {
            val userList = getAllUserFromDb()
            Log.e("getDataFromDb", userList.toString())
        }
    }

    fun updateUserData(userPasswordEntities: UserPasswordEntities) {
        viewModelScope.launch {
            ApplicationClass.database.userDao().updateUser(userPasswordEntities)
        }
    }

    fun deleteUserById(id: Long) {
        viewModelScope.launch {
            ApplicationClass.database.userDao().deleteUserById(id)
        }
    }
}