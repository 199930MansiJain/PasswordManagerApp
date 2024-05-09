package com.example.passwordmanagerapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordmanagerapp.room.entities.UserPasswordEntities
import com.example.passwordmanagerapp.screens.ActionType
import com.example.passwordmanagerapp.screens.BottomSheetScreen
import com.example.passwordmanagerapp.state.MainActivityUiState
import com.example.passwordmanagerapp.ui.theme.PasswordManagerAppTheme
import com.example.passwordmanagerapp.viewModel.MainActivityViewModel
import java.util.Locale

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            PasswordManagerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xffF3F5FA)
                ) {
                    val viewModel = viewModel<MainActivityViewModel>()
                    val mainActivityUiState by viewModel.state.collectAsState()
                    Column {
                        Text(
                            modifier = Modifier.padding(top = 20.dp, bottom = 15.dp, start = 20.dp),
                            text = "Password Manager", color = Color(0xff333333),
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                        )
                        Divider(color = Color.LightGray, thickness = 0.7.dp)
                        ViewLogList(viewModel, mainActivityUiState = mainActivityUiState)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomFloatingActionButton(
    onAddButtonClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(
                        constraints.maxWidth - placeable.width,
                        constraints.maxHeight - placeable.height
                    )
                }
            }
            .padding(16.dp)
    ) {
        FloatingActionButton(
            onClick = {
                onAddButtonClicked()
            },
            backgroundColor = Color(0xff3F7DE3),
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun ViewLogList(
    viewModel: MainActivityViewModel,
    mainActivityUiState: MainActivityUiState,
) {

    var isBottomSheetOpened by remember {
        mutableStateOf(false)
    }
    var fromListView by remember {
        mutableStateOf(false)
    }
    var selectedId by remember { mutableStateOf<UserPasswordEntities?>(null) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        if (mainActivityUiState.passwordDataList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopStart)
            ) {
                itemsIndexed(items = mainActivityUiState.passwordDataList) { index, promotion ->
                    ViewLogListItem(promotion, onItemClick = { clickedData ->
                        selectedId = clickedData
                        fromListView = true
                        isBottomSheetOpened = true
                    })
                }
            }
        }

        CustomFloatingActionButton {
            isBottomSheetOpened = true
        }

        if (isBottomSheetOpened) {

            BottomSheetScreen(viewModel = viewModel,
                fromList = fromListView,
                userPasswordEntities = selectedId,
                onActionListener =
                { actionType, userPasswordEntities ->
                    when (actionType) {
                        ActionType.ADD -> {
                            viewModel.insertUserDataToDb(userPasswordEntities)

                            isBottomSheetOpened = false
                            viewModel.fetchData()
                        }

                        ActionType.DELETE -> {
                            viewModel.deleteUserById(userPasswordEntities.id)
                            isBottomSheetOpened = false
                            fromListView = false
                            selectedId = null
                            viewModel.fetchData()
                        }

                        ActionType.EDIT -> {
                            viewModel.updateUserData(userPasswordEntities)
                            isBottomSheetOpened = false
                            fromListView = false
                            selectedId = null
                            viewModel.fetchData()

                        }
                    }
                }, onDismiss = {
                    if (it) {
                        isBottomSheetOpened = false
                        fromListView = false
                        selectedId = null
                        viewModel.fetchData()
                    }
                }
            )
        }
    }
}

@Composable
fun ViewLogListItem(
    passwordManagerDTO: UserPasswordEntities,
    onItemClick: (UserPasswordEntities) -> Unit
) {
    val componentColor = Color.Black
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable {
                onItemClick.invoke(passwordManagerDTO)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),

                    text = passwordManagerDTO.accountName.toLowerCase(Locale.ROOT)
                        .capitalize(Locale.ROOT),
                    color = componentColor,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                    text = "•".repeat(passwordManagerDTO.password.length),
                    color = Color.Gray,
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .padding(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "arrow"
                )
            }
        }
    }
}

