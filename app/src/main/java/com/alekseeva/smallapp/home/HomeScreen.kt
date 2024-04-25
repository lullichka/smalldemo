package com.alekseeva.smallapp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alekseeva.smallapp.AppNavigation
import com.alekseeva.smallapp.R
import com.alekseeva.smallapp.model.UserProfile
import com.alekseeva.smallapp.ui.theme.Purple40
import com.alekseeva.smallapp.ui.theme.Purple80
import com.alekseeva.smallapp.ui.theme.PurpleGrey40

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    when (state) {
        is State.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Purple80)
            ) {
                CircularProgressIndicator()
            }
        }

        is State.NoUser -> {
            HomeScreenWithUser(user = null, navController)
        }

        is State.Success -> {
            val user = (state as State.Success).profile
            HomeScreenWithUser(user = user, navController)
        }

        is State.Error -> {
            val error = (state as State.Error)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Text(
                    text = stringResource(R.string.failed_to_fetch_data, error),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun HomeScreenWithUser(user: UserProfile?, navController: NavHostController) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(Purple80)) {
        Column(
            Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
                .fillMaxSize(0.75f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = user?.name ?: context.getString(R.string.there_is_no_user_yet) ,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.size(40.dp))
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 8.dp,
                ),
                shape = RoundedCornerShape(15), modifier = Modifier
                    .border(
                        width = 3.dp, color = PurpleGrey40, shape = CutCornerShape(20)
                    )
                    .width(220.dp)
                    .height(50.dp), colors = ButtonDefaults.buttonColors(
                    contentColor = Purple80, containerColor = Purple40
                ), onClick = {
                    navController.navigate(AppNavigation.NavigationItem.EditUser.route)
                }) {
                Text(
                    text = context.getString(R.string.edit),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}