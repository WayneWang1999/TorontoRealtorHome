package com.example.torontorealtorhome.ui.screen.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.torontorealtorhome.data.HouseRepository
import com.example.torontorenthome.models.House
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val houseRepository: HouseRepository
) : ViewModel() {
    val houseList: StateFlow<List<House>> = houseRepository.houseList
    val isLoading: StateFlow<Boolean> = houseRepository.isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    // Accept `realtorHouseIds` from UserStateViewModel dynamically
    fun getListHouses(userStateViewModel: UserStateViewModel): StateFlow<List<House>> {
        return houseList
            .combine(userStateViewModel.realtorHouseIds) { houses, realtorHouseIds ->
                houses.filter { it.houseId in realtorHouseIds }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    init {
        viewModelScope.launch {
            houseList.collect { houses ->
                Log.d("ListScreenViewModel", "houseList: $houses")
            }
        }
        fetchHouses()
    }

    private fun fetchHouses() {
        viewModelScope.launch {
            houseRepository.fetchHouses()
        }
    }
}