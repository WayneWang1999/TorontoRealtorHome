package com.example.torontorealtorhome.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    navController: NavHostController
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var reEnterPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var reEnterPasswordError by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Back Button
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up for TORH.ca",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // First Name Input
            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = if (it.isBlank()) "First name cannot be empty" else ""
                },
                label = { Text("First Name") },
                isError = firstNameError.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            if (firstNameError.isNotBlank()) {
                Text(text = firstNameError, color = androidx.compose.ui.graphics.Color.Red, fontSize = 12.sp)
            }

            // Last Name Input
            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    lastNameError = if (it.isBlank()) "Last name cannot be empty" else ""
                },
                label = { Text("Last Name") },
                isError = lastNameError.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            if (lastNameError.isNotBlank()) {
                Text(text = lastNameError, color = androidx.compose.ui.graphics.Color.Red, fontSize = 12.sp)
            }

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (it.isBlank()) "Email cannot be empty" else ""
                },
                label = { Text("Email") },
                isError = emailError.isNotBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            if (emailError.isNotBlank()) {
                Text(text = emailError, color = androidx.compose.ui.graphics.Color.Red, fontSize = 12.sp)
            }

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = if (it.isBlank()) "Password cannot be empty" else ""
                },
                label = { Text("Password") },
                isError = passwordError.isNotBlank(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
//                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
//                        Icon(
//                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                            contentDescription = null
//                        )
//                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            if (passwordError.isNotBlank()) {
                Text(text = passwordError, color = androidx.compose.ui.graphics.Color.Red, fontSize = 12.sp)
            }

            // Re-enter Password Input
            OutlinedTextField(
                value = reEnterPassword,
                onValueChange = {
                    reEnterPassword = it
                    reEnterPasswordError = if (it.isBlank()) {
                        "Re-enter password cannot be empty"
                    } else if (it != password) {
                        "Passwords do not match"
                    } else ""
                },
                label = { Text("Re-enter Password") },
                isError = reEnterPasswordError.isNotBlank(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
//                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
//                        Icon(
//                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
//                            contentDescription = null
//                        )
//                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
            if (reEnterPasswordError.isNotBlank()) {
                Text(text = reEnterPasswordError, color = androidx.compose.ui.graphics.Color.Red, fontSize = 12.sp)
            }

            // Sign-Up Button
            Button(
                onClick = {
                    if (firstName.isBlank()) firstNameError = "First name cannot be empty"
                    if (lastName.isBlank()) lastNameError = "Last name cannot be empty"
                    if (email.isBlank()) emailError = "Email cannot be empty"
                    if (password.isBlank()) passwordError = "Password cannot be empty"
                    if (reEnterPassword.isBlank()) reEnterPasswordError = "Re-enter password cannot be empty"
                    if (password != reEnterPassword) reEnterPasswordError = "Passwords do not match"

                    if (firstNameError.isBlank() && lastNameError.isBlank() &&
                        emailError.isBlank() && passwordError.isBlank() && reEnterPasswordError.isBlank()
                    ) {
                        // Call Firebase Auth to create a user
                        val auth = FirebaseAuth.getInstance()
                        val firestore = FirebaseFirestore.getInstance()

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userId = task.result?.user?.uid
                                    if (userId != null) {
                                        // Save additional user details to Firestore
                                        val user = hashMapOf(
                                            "firstName" to firstName,
                                            "lastName" to lastName,
                                            "email" to email,
                                            "userId" to userId,
                                            "realtorHouseIds" to emptyList<String>()
                                        )
                                        firestore.collection("realtors").document(userId)
                                            .set(user)
                                            .addOnSuccessListener {
                                                Log.d("SignUpScreen", "User registered successfully")
                                                navController.navigate("account")
                                                // Navigate to next screen or show success
                                            }
                                            .addOnFailureListener { e ->
                                                Log.d("SignUpScreen", "Firestore Error: ${e.message}")
                                                // Show error to the user
                                            }
                                    }
                                } else {
                                    Log.d("SignUpScreen", "Auth Error: ${task.exception?.message}")
                                    // Show error to the user
                                }
                            }
                    } else {
                        Log.d("SignUpScreen", "there is a error input")
                        // Show error to the user (e.g., Snackbar or Toast)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
        }
    }
}