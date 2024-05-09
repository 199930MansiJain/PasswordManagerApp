package com.example.passwordmanagerapp.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import com.example.passwordmanagerapp.state.MainActivityUiState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun BottomSheetScreen(
    fromList: Boolean,
    userPasswordEntities: UserPasswordEntities?,
    mainActivityUiState: MainActivityUiState,
    onActionListener: (ActionType, UserPasswordEntities) -> Unit
) {
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
                if (fromList) {
                    if (userPasswordEntities != null) {
                        ViewUserDataPasswordDetails(
                            uiState = mainActivityUiState,
                            onActionListener = { actionType, userPasswordEntities ->
                                onActionListener(actionType, userPasswordEntities)
                            },
                            userPasswordEntities = userPasswordEntities
                        )
                    }

                } else {
                    BottomSheetContent(onActionListener = { actionType, userPasswordEntities ->
                        onActionListener(actionType, userPasswordEntities)

                    })
                }
            }
        }, containerColor = Color(0xffF9F9F9)
    )
}


@Composable
fun BottomSheetContent(onActionListener: (ActionType, UserPasswordEntities) -> Unit) {
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

        val userDatEntities = UserPasswordEntities(
            accountName = accountName,
            password = password,
            userName = userNameOrEmail,
            emailId = "dcd"
        )
        Button(
            onClick = {
                onActionListener(ActionType.ADD, userDatEntities)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black) // Set background color
        ) {
            Text(text = "Add New Account", color = Color.White)
        }
    }


}

@Composable
fun ViewUserDataPasswordDetails(
    uiState: MainActivityUiState,
    onActionListener: (ActionType, UserPasswordEntities) -> Unit,
    userPasswordEntities: UserPasswordEntities
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Log.e("ugfuef", userPasswordEntities.toString())
        Text(
            text = "Account Details",
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color(0xff3F7DE3),
                fontWeight = FontWeight.SemiBold
            )
        )

        UserPasswordDetailsView(
            modifier = Modifier.padding(5.dp),
            userHeaderData = "Account Type",
            userValueData = userPasswordEntities.accountName
        )
        UserPasswordDetailsView(
            modifier = Modifier.padding(5.dp),
            userHeaderData = "UserName/Email",
            userValueData = userPasswordEntities.userName
        )
        UserPasswordDetailsView(
            modifier = Modifier.padding(5.dp),
            userHeaderData = "Account Type",
            userValueData = userPasswordEntities.emailId
        )

        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    onActionListener(ActionType.EDIT, userPasswordEntities)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xffF04646))
            ) {
                Text(text = "Edit",color = Color.White)
            }
            Button(
                onClick = {
                    onActionListener(ActionType.DELETE, userPasswordEntities)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Delete", color = Color.White)
            }
        }

    }
}

@Composable
fun UserPasswordDetailsView(modifier: Modifier, userHeaderData: String, userValueData: String) {
    Row(modifier) {
        Text(
            text = userHeaderData,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            style = TextStyle(
                fontSize = 15.sp,
                color = Color(0xffCCCCCC),
                fontWeight = FontWeight.Medium
            )
        )

        Text(
            text = userValueData,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        )
    }


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