package com.alekseeva.smallapp.editprofile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alekseeva.smallapp.AppNavigation
import com.alekseeva.smallapp.R
import com.alekseeva.smallapp.model.UserProfile
import com.alekseeva.smallapp.ui.theme.Pink40
import com.alekseeva.smallapp.ui.theme.Purple80
import com.alekseeva.smallapp.ui.theme.PurpleGrey40
import com.alekseeva.smallapp.ui.theme.PurpleGrey80

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    val nameState = remember { mutableStateOf(String()) }
    val clicked = remember { mutableStateOf(false) }

    when (state) {
        is EditProfileState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Purple80)
            ) { CircularProgressIndicator() }
        }

        is EditProfileState.Saved -> {
            val saved = (state as EditProfileState.Saved).saved
            if (saved) {
                viewModel.setToLoading()
                navController.popBackStack()
            } else {
                Toast.makeText(
                    LocalContext.current, "Something wrong", Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    ScreenEmptyUser(
        navController, nameState, clicked
    ) { user1 -> viewModel.saveUser(user1) }

}

@Composable
fun ScreenEmptyUser(
    navController: NavHostController, nameState: MutableState<String>, clicked: MutableState<Boolean>,
    onSaveClicked: (UserProfile) -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple80)
    ) {
        ClickableText(
            text = AnnotatedString(context.getString(R.string.back)),
            style = TextStyle(
                fontSize = 20.sp,
                color = Pink40,
            ),
            modifier = Modifier.padding(25.dp),
            onClick = { navController.popBackStack() }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = context.getString(R.string.fill_your_data),
                fontSize = 24.sp,
                color = Pink40,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(5.dp)
            )
            StyleEditText(
                context.getString(R.string.name), nameState,
                isError = clicked.value && nameState.value.isEmpty()
            )
            Button(shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Purple80, containerColor = PurpleGrey40
                ),
                onClick = {
                    clicked.value = true
                    if (nameState.value.isNotEmpty()) {
                        val user = UserProfile(nameState.value)
                        onSaveClicked.invoke(user)
                        keyboardController?.hide()
                    }
                }) {
                Text(
                    text = context.getString(R.string.save),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyleEditText(hint: String, editTextValue: MutableState<String>, isError: Boolean) {
    Column {
        OutlinedTextField(
            value = editTextValue.value,
            isError = isError,
            onValueChange = { editTextValue.value = it },
            textStyle = TextStyle(
                color = Pink40,
                fontSize = 24.sp
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp)
                .height(70.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PurpleGrey40,
                unfocusedBorderColor = Purple80,
                cursorColor = PurpleGrey40,
                containerColor = PurpleGrey80,
                unfocusedLabelColor = Purple80,
                focusedLabelColor = PurpleGrey40,
            ),
            label = { Text(text = hint, fontSize = 16.sp) },
            visualTransformation = {
                TransformedText(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(editTextValue.value)
                        }
                    },
                    offsetMapping = OffsetMapping.Identity
                )
            }
        )
    }
}