package com.example.passwordmanagerapp.state

import com.example.passwordmanagerapp.dto.PasswordManagerDTO

val dummyData1 = PasswordManagerDTO("Facebook", "user1", "password1", "user1@example.com")
val dummyData2 = PasswordManagerDTO("Twitter", "user2", "password2", "user2@example.com")
val dummyData3 = PasswordManagerDTO("Instagram", "user3", "password3", "user3@example.com")
val dummyData4 = PasswordManagerDTO("LinkedIn", "user4", "password4", "user4@example.com")


data class MainActivityUiState(
    val list: MutableList<PasswordManagerDTO> = mutableListOf(dummyData1, dummyData2, dummyData3,
        dummyData4)
)

