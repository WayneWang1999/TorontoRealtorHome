package com.example.torontorealtorhome.data

import android.util.Log
import com.example.torontorenthome.models.House
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class HouseRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val _houseList = MutableStateFlow<List<House>>(emptyList())
    val houseList: StateFlow<List<House>> = _houseList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var housesListener: ListenerRegistration? = null

    // Fetch houses with real-time updates
    fun startListeningForHouses() {
        _isLoading.value = true
        housesListener = firestore.collection("houses")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("HouseRepository", "Listen failed: ${exception.message}", exception)
                    _isLoading.value = false
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val houseList = snapshot.documents.mapNotNull { it.toObject(House::class.java) }
                    _houseList.value = houseList
                }

                _isLoading.value = false
            }
    }

    // Stop listening to Firestore updates
    fun stopListeningForHouses() {
        housesListener?.remove()
        housesListener = null
    }
}