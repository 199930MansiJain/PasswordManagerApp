package com.example.passwordmanagerapp.state

import com.example.passwordmanagerapp.dto.PasswordManagerDTO
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities


data class MainActivityUiState(
    val passwordDataList: List<UserPasswordEntities> = emptyList(),
)

