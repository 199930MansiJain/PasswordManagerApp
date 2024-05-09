package com.example.passwordmanagerapp.state

import com.example.passwordmanagerapp.room.entities.UserPasswordEntities


data class MainActivityUiState(
    val passwordDataList: List<UserPasswordEntities> = emptyList(),
)

