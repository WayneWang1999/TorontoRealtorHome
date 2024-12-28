package com.example.torontorealtorhome.ui.screen.viewmodels

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddScreenViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
): ViewModel(){
    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _latitude = MutableStateFlow(0.0)
    val latitude: StateFlow<Double> = _latitude

    private val _longitude = MutableStateFlow(0.0)
    val longitude: StateFlow<Double> = _longitude

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _bedrooms = MutableStateFlow(0)
    val bedrooms: StateFlow<Int> = _bedrooms

    private val _bathrooms = MutableStateFlow(2)
    val bathrooms: StateFlow<Int> = _bathrooms

    private val _area = MutableStateFlow(800)
    val area: StateFlow<Int> = _area

    private val _price = MutableStateFlow(0)
    val price: StateFlow<Int> = _price

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl

    private val _type = MutableStateFlow("House")
    val type: StateFlow<String> = _type

    private val _isAvailable = MutableStateFlow(true)
    val isAvailable: StateFlow<Boolean> = _isAvailable

    fun updateAddress(value: String) = _address.update { value }
    fun updateDescription(value: String) = _description.update { value }
    fun updateBedrooms(value: Int) = _bedrooms.update { value }
    fun updateBathrooms(value: Int) = _bathrooms.update { value }
    fun updateArea(value: Int) = _area.update { value }
    fun updatePrice(value: Int) = _price.update { value }
    fun updateImageUrl(value: String) = _imageUrl.update { value }
    fun updateType(value: String) = _type.update { value }
    fun updateIsAvailable(value: Boolean) = _isAvailable.update { value }


    suspend fun fetchCoordinates(address: String) {
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(appContext, Locale.getDefault())
                val addresses = geocoder.getFromLocationName(address, 1)

                if (addresses != null && addresses.isNotEmpty()) {
                    val location = addresses[0]
                    val lat = location.latitude
                    val lng = location.longitude
                    _latitude.update { lat }
                    _longitude.update { lng }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}