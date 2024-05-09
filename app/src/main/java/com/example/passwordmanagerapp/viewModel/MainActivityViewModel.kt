package com.example.passwordmanagerapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanagerapp.ApplicationClass
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities
import com.example.passwordmanagerapp.state.MainActivityUiState
import com.example.passwordmanagerapp.utils.EncryptionUtils
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


    init {
        fetchData()
    }

    fun insertUserDataToDb(userPasswordEntities: UserPasswordEntities) {
        CoroutineScope(Dispatchers.IO).launch {
            val encryptedPassword = EncryptionUtils.encrypt(userPasswordEntities.password)
            val userPasswordEntitiesData = UserPasswordEntities(
                accountName = userPasswordEntities.accountName,
                userNameOrEmail = userPasswordEntities.userNameOrEmail,
                password = encryptedPassword
            )
            ApplicationClass.database.userDao().insertPassword(userPasswordEntitiesData)
            fetchData()
            _state.update { it.copy(passwordDataList = getAllUserFromDb()) }
        }
    }

    fun getDecryptedPasswords(): List<UserPasswordEntities> {
        return _state.value.passwordDataList.map { entity ->
            UserPasswordEntities(
                id = entity.id,
                accountName = entity.accountName,
                userNameOrEmail = entity.userNameOrEmail,
                password = EncryptionUtils.decrypt(entity.password)
            )
        }
    }
    private suspend fun getAllUserFromDb(): List<UserPasswordEntities> {
        return withContext(Dispatchers.IO) {
            ApplicationClass.database.userDao().getAllPasswords()
        }

    }

     fun fetchData() {
        viewModelScope.launch {
            val decryptedPasswordList = getAllUserFromDb().map { entity ->
                UserPasswordEntities(
                    id = entity.id,
                    accountName = entity.accountName,
                    userNameOrEmail = entity.userNameOrEmail,
                    password = EncryptionUtils.decrypt(entity.password)
                )
            }
            _state.update { it.copy(passwordDataList = decryptedPasswordList) }
        }
    }
    fun updateUserData(userPasswordEntities: UserPasswordEntities) {
        viewModelScope.launch {
            val encryptedPassword = EncryptionUtils.encrypt(userPasswordEntities.password)
            val updatedEntity = UserPasswordEntities(
                id = userPasswordEntities.id,
                accountName = userPasswordEntities.accountName,
                userNameOrEmail = userPasswordEntities.userNameOrEmail,
                password = encryptedPassword
            )
            ApplicationClass.database.userDao().updatePassword(updatedEntity)
            fetchData() // Fetch the updated data from the database
        }
    }
    fun deleteUserById(id: Long) {
        viewModelScope.launch {
            ApplicationClass.database.userDao().deletePasswordById(id)
        }
        fetchData()
    }
}