package com.example.torontorealtorhome.ui.screen.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserStateViewModel @Inject constructor(): ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // StateFlow to track if the user is logged in
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // StateFlow to store the user's email
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // realtor's  house IDs
    private val _realtorHouseIds = MutableStateFlow<Set<String>>(emptySet())
    val realtorHouseIds: StateFlow<Set<String>> = _realtorHouseIds

    init {
        val user = auth.currentUser
        _isLoggedIn.value = user != null
        _userEmail.value = user?.email
        _currentUser.value = user // Set initial user
        if (user != null) {
            Log.d("currentUser", "${user.email}")
            fetchRealtorHouseIds(user.uid)
        }
    }
    fun fetchRealtorHouseIds(userId: String) {
        viewModelScope.launch {
            try {
                val document = FirebaseFirestore.getInstance()
                    .collection("realtors")
                    .document(userId)
                    .get()
                    .await()
                val realtorHouseIds = document["realtorHouseIds"] as? List<String> ?: emptyList()
                Log.d("realtorIds","$realtorHouseIds")
                _realtorHouseIds.value = realtorHouseIds.toSet()
            } catch (e: Exception) {
                e.printStackTrace()
                _realtorHouseIds.value = emptySet()
            }
        }
    }
    // Login function
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    _isLoggedIn.value = true
                    _userEmail.value = user?.email
                    _currentUser.value = user // Update current user
                    _errorMessage.value = ""

                    user?.let {
                        fetchRealtorHouseIds(it.uid)
                    }
                } else {
                    _isLoggedIn.value = false
                    _userEmail.value = null
                    _currentUser.value = null // Clear current user
                    _errorMessage.value = "Email or Password not correct!!!"
                }
            }
    }

    // Logout function
    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _userEmail.value = null
        _currentUser.value = null // Clear current user
        _realtorHouseIds.value = emptySet() // Clear realtorHouseIds
       }
}

