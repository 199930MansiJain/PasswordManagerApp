package com.example.passwordmanagerapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanagerapp.viewModel.MainActivityViewModel
import com.example.passwordmanagerapp.R
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities

enum class ActionType {
    ADD,
    DELETE,
    EDIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScreen(
    viewModel: MainActivityViewModel,
    fromList: Boolean,
    userPasswordEntities: UserPasswordEntities?,
    onActionListener: (ActionType, UserPasswordEntities) -> Unit,
    onDismiss: (Boolean) -> Unit
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
            onDismiss(true)
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

        var showError by remember { mutableStateOf(false) }

        if (showError) {
            Text(
                text = stringResource(R.string.all_fields_must_be_filled),
                modifier = Modifier.padding(vertical = 2.dp),
                color = Color.Red
            )
        }
        MyTextField(value = stringResource(R.string.account_name)) {
            accountName = it
            showError = false
        }
        MyTextField(value = stringResource(R.string.username_email)) {
            userNameOrEmail = it
            showError = false
        }
        MyTextField(value = stringResource(R.string.password)) {
            password = it
            showError = false
        }
        Spacer(modifier = Modifier.height(16.dp))

        val userDatEntities = UserPasswordEntities(
            accountName = accountName,
            password = password,
            userNameOrEmail = userNameOrEmail,
        )
        Button(
            onClick = {
                if (accountName.isBlank() || userNameOrEmail.isBlank() || password.isBlank()) {
                    showError = true
                } else {
                    onActionListener(ActionType.ADD, userDatEntities)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, top = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black) // Set background color
        ) {
            Text(
                text = stringResource(R.string.add_new_account),
                color = Color.White,
                style = TextStyle(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
fun ViewUserDataPasswordDetails(
    onActionListener: (ActionType, UserPasswordEntities) -> Unit,
    userPasswordEntities: UserPasswordEntities
) {
    var isEditing by remember { mutableStateOf(false) }
    val editedUserNameOrEmailId by remember { mutableStateOf(userPasswordEntities.userNameOrEmail) }
    var editedAccountName by remember { mutableStateOf(userPasswordEntities.accountName) }
    var editedUserPassword by remember { mutableStateOf(userPasswordEntities.password) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
    ) {
        Text(
            text = stringResource(R.string.account_details),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 20.dp),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color(0xff3F7DE3),
                fontWeight = FontWeight.Medium
            )
        )

        UserPasswordDetailsView(
            modifier = Modifier.padding(vertical = 9.dp),
            userHeaderData = stringResource(R.string.account_type),
            userValueData = if (isEditing) editedAccountName else userPasswordEntities.accountName,
            isEditing = isEditing,
            onValueChanged = { editedAccountName = it }
        )
        UserPasswordDetailsView(
            modifier = Modifier.padding(vertical = 9.dp),
            userHeaderData = stringResource(R.string.username_email),
            userValueData = if (isEditing) editedUserNameOrEmailId else userPasswordEntities.userNameOrEmail,
            isEditing = isEditing,
            onValueChanged = { editedAccountName = it }
        )
        UserPasswordDetailsView(
            modifier = Modifier.padding(vertical = 9.dp),
            userHeaderData = stringResource(R.string.password),
            userValueData = if (isEditing) editedUserPassword else userPasswordEntities.password,
            isEditing = isEditing,
            onValueChanged = { editedUserPassword = it }
        )

        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Button(
                onClick = {
                    if (isEditing) {


                        onActionListener(
                            ActionType.EDIT,
                            userPasswordEntities.copy(
                                userNameOrEmail = editedUserNameOrEmailId,
                                password = editedUserPassword,
                                accountName = editedAccountName
                            )
                        )

                    }
                    isEditing = !isEditing
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEditing) Color(
                        0xFF0B613E
                    ) else Color.Black
                )
            ) {
                Text(
                    text = if (isEditing) stringResource(R.string.save) else stringResource(R.string.edit),
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.SemiBold)
                )
            }
            Button(
                onClick = {
                    onActionListener(ActionType.DELETE, userPasswordEntities)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffF04646))
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.SemiBold)
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPasswordDetailsView(
    modifier: Modifier,
    userHeaderData: String,
    userValueData: String,
    isEditing: Boolean,
    onValueChanged: (String) -> Unit
) {
    Column(modifier) {
        Text(
            text = userHeaderData,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            style = TextStyle(
                fontSize = 13.sp,
                color = Color(0xffCCCCCC),
                fontWeight = FontWeight.Medium
            )
        )

        if (isEditing) {

            TextField(
                value = userValueData,
                onValueChange = { onValueChanged(it) },
                placeholder = { Text(userHeaderData) },
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .border(
                        BorderStroke(width = 0.6.dp, color = Color(0xffCBCBCB)),
                        shape = RoundedCornerShape(15)
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


        } else {
            if (userHeaderData.equals("Password")) {
                ToggleablePasswordText(userValueData = userValueData)
            } else {
                Text(
                    text = userValueData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
fun ToggleablePasswordText(userValueData: String) {
    var passwordVisible by remember { mutableStateOf(false) }
    val icon =
        if (passwordVisible) painterResource(id = R.drawable.baseline_remove_red_eye_24)
        else painterResource(id = R.drawable.baseline_visibility_off_24)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = if (passwordVisible) userValueData else "•".repeat(userValueData.length),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 3.dp),
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        )
        TextButton(
            onClick = { passwordVisible = !passwordVisible },
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            Icon(
                painter = icon,
                tint = Color(0xffCBCBCB),
                contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }

    Column {
        TextField(
            value = text,
            onValueChange = {
                text = it
                onValueChange(it)
            },
            placeholder = { Text(value, color = Color(0xffCCCCCC)) },
            modifier = Modifier
                .padding(vertical = 12.dp)
                .border(
                    BorderStroke(width = 0.6.dp, color = Color(0xffCBCBCB)),
                    shape = RoundedCornerShape(15)
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
        if (value == "Password") {
            PasswordStrengthIndicator(password = text)
        }
    }
}

enum class PasswordStrength {
    WEAK, MEDIUM, STRONG, VERY_STRONG
}


fun calculatePasswordStrength(password: String): PasswordStrength {

    return when {
        password.length < 3 -> PasswordStrength.WEAK
        password.length < 8 -> PasswordStrength.MEDIUM
        password.any { it.isDigit() } && password.any { it.isLetter() } -> PasswordStrength.STRONG
        else -> PasswordStrength.VERY_STRONG
    }
}
@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = calculatePasswordStrength(password)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(Color.LightGray)
    ) {
        val color = when (strength) {
            PasswordStrength.WEAK -> Color(0xFFC20909)
            PasswordStrength.MEDIUM -> Color(0xFFFFEB3B)
            PasswordStrength.STRONG -> Color(0xFF4CAF50)
            PasswordStrength.VERY_STRONG -> Color(0xFF4CAF50)
        }

        val strengthPercentage = when (strength) {
            PasswordStrength.WEAK -> 25
            PasswordStrength.MEDIUM -> 50
            PasswordStrength.STRONG -> 75
            PasswordStrength.VERY_STRONG -> 100
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(strengthPercentage / 100f)
                .height(8.dp)
                .background(color)
                .clip(RoundedCornerShape(4.dp))
        )    }
}