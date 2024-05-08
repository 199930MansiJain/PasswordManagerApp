package com.example.passwordmanagerapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import com.example.passwordmanagerapp.state.MainActivityUiState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities

enum class ActionType {
    ADD,
    DELETE,
    EDIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScreen(mainActivityUiState: MainActivityUiState,onActionListener: (ActionType, UserPasswordEntities) -> Unit) {
    var showSheet by remember {
        mutableStateOf(true)
    }
    var fromAddNewPassword by remember {
        mutableStateOf(false)
    }
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        onDismissRequest = {
            showSheet = false
            fromAddNewPassword = false
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .fillMaxWidth()
            ) {
                BottomSheetContent(onActionListener = {
                    actionType, userPasswordEntities ->
                    onActionListener(actionType,userPasswordEntities)

                })
            }
        }, containerColor = Color(0xffF9F9F9)
    )
}


@Composable
fun BottomSheetContent(onActionListener: (ActionType,UserPasswordEntities) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var accountName by remember { mutableStateOf("") }
        var userNameOrEmail by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

            MyTextField(value = "Account Name") {
                accountName = it
            }
            MyTextField(value = "UserName/Email") {
                userNameOrEmail = it
            }
            MyTextField(value = "Password") {
                password = it
            }
        Spacer(modifier = Modifier.height(16.dp))

        val userDatEntities = UserPasswordEntities(accountName = accountName, password = password, userName = userNameOrEmail, emailId = "dcd")
        Button(onClick = {
            onActionListener(ActionType.ADD,userDatEntities)
             }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Add New Account")
        }
    }


}

@Composable
fun EditTextWithRoundedCorners() {


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        placeholder = { Text(value) },
        modifier = Modifier
            .padding(vertical = 8.dp)
            .border(
                BorderStroke(width = 0.6.dp, color = Color(0xffCBCBCB)),
                shape = RoundedCornerShape(30)
            )
            .fillMaxWidth(),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        ), colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}