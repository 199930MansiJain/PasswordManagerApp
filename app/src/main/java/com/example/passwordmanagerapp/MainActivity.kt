package com.example.passwordmanagerapp

import android.graphics.fonts.FontFamily
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanagerapp.dto.PasswordManagerDTO
import com.example.passwordmanagerapp.state.MainActivityUiState
import com.example.passwordmanagerapp.ui.theme.PasswordManagerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasswordManagerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xffF3F5FA)
                ) {



                }
            }
        }
    }
}

@Composable
fun PasswordsListView(){
    val viewModel = viewModel<HomeScreenViewModel>()
    val homeUiState by viewModel.state.collectAsState()

}


@Preview
@Composable
fun GetViewPreview(){
ViewLogList(mainActivityUiState = )

}

@Composable
fun ViewLogList(
    mainActivityUiState: MainActivityUiState,
) {
    if (mainActivityUiState.list.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            itemsIndexed(items = mainActivityUiState.list) { index, promotion ->
                ViewLogListItem(promotion)
            }
        }
    }
}

@Composable
fun ViewLogListItem(
    passwordManagerDTO: PasswordManagerDTO,
) {
    val componentColor = Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {


            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Text(
                    text = passwordManagerDTO.accountName,
                    color = componentColor
                )
                Text(
                    text = passwordManagerDTO.password,
                    color = componentColor
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

                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "arrow")
            }
        }
    }
}
