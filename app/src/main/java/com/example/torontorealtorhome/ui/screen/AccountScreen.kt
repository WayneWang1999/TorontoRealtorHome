package com.example.torontorealtorhome.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.torontorealtorhome.ui.screen.models.Routes
import com.example.torontorealtorhome.ui.screen.viewmodels.UserStateViewModel


@Composable
fun AccountScreen(
    navController: NavHostController,
    userStateViewModel: UserStateViewModel
) {
    //from the userState variable this userEmail is get from the firestore db and show in
    //the after login UI
    val isLoggedIn by userStateViewModel.isLoggedIn.collectAsState()
    val userEmail by userStateViewModel.userEmail.collectAsState()
    val errorMessage by userStateViewModel.errorMessage.collectAsState()
    val isLoading by userStateViewModel.isLoading.collectAsState()
    // local variable
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        if (!isLoggedIn) {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            LoginForm(
                email = email,
                password = password,
                isPasswordVisible = isPasswordVisible,
                emailError = emailError,
                passwordError = passwordError,
                onEmailChange = {
                    email = it
                    emailError = null
                },
                onPasswordChange = {
                    password = it
                    passwordError = null
                },
                onPasswordVisibilityToggle = { isPasswordVisible = !isPasswordVisible },
                onLoginClick = {
                    if (email.isBlank()) {
                        emailError = "Email cannot be empty"
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Invalid email address"
                    } else if (password.isBlank()) {
                        passwordError = "Password cannot be empty"
                    } else {
                        userStateViewModel.login(email, password)
                    }
                },
                onSignUpClick = { navController.navigate(Routes.SignUp.route) }
            )

            if (isLoading) {
                //LoadingIndicator()
            }
        } else {
            LoggedInUI(
                userEmail = userEmail ?: "Guest",
                onLogoutClick = { userStateViewModel.logout() }
            )
        }
    }
}

@Composable
fun LoginForm(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    emailError: String?,
    passwordError: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            isError = emailError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        if (emailError != null) {
            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            isError = passwordError != null,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        if (passwordError != null) {
            Text(
                text = passwordError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Text(
            text = "Create an account",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF718BD0),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable(onClick = onSignUpClick),
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun LoggedInUI(
    userEmail: String,
    onLogoutClick: () -> Unit
) {
    Text(
        text = "Welcome, $userEmail!",
        fontSize = 24.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Button(
        onClick = onLogoutClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Logout")
    }
}