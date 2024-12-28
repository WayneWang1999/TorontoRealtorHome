package com.example.torontorealtorhome.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.torontorealtorhome.ui.screen.viewmodels.AddScreenViewModel
import com.example.torontorenthome.models.House
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun AddScreen(
    onSubmit: (House) -> Unit,
    addScreenViewModel: AddScreenViewModel = hiltViewModel()
) {
    val address = addScreenViewModel.address.collectAsState().value
    val description = addScreenViewModel.description.collectAsState().value
    val bedrooms = addScreenViewModel.bedrooms.collectAsState().value
    val bathrooms = addScreenViewModel.bathrooms.collectAsState().value
    val area = addScreenViewModel.area.collectAsState().value
    val price = addScreenViewModel.price.collectAsState().value
    val imageUrl = addScreenViewModel.imageUrl.collectAsState().value
    val type = addScreenViewModel.type.collectAsState().value
    val isAvailable = addScreenViewModel.isAvailable.collectAsState().value
    val latitude = addScreenViewModel.latitude.collectAsState().value
    val longitude = addScreenViewModel.longitude.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = address,
            onValueChange = { addScreenViewModel.updateAddress(it) },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Trigger fetching coordinates based on the address
                kotlinx.coroutines.CoroutineScope(Dispatchers.Main).launch {
                    addScreenViewModel.fetchCoordinates(address)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fetch Coordinates")
        }

        // Display fetched latitude and longitude
        Text("Latitude: $latitude")
        Text("Longitude: $longitude")

        TextField(
            value = description,
            onValueChange = { addScreenViewModel.updateDescription(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = bedrooms.toString(),
            onValueChange = { addScreenViewModel.updateBedrooms(it.toIntOrNull() ?: 0) },
            label = { Text("Bedrooms") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = bathrooms.toString(),
            onValueChange = { addScreenViewModel.updateBathrooms(it.toIntOrNull() ?: 0) },
            label = { Text("Bathrooms") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = area.toString(),
            onValueChange = { addScreenViewModel.updateArea(it.toIntOrNull() ?: 0) },
            label = { Text("Area (sq ft)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = price.toString(),
            onValueChange = { addScreenViewModel.updatePrice(it.toIntOrNull() ?: 0) },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = imageUrl,
            onValueChange = { addScreenViewModel.updateImageUrl(it) },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = type,
            onValueChange = { addScreenViewModel.updateType(it) },
            label = { Text("Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Available")
            Switch(
                checked = isAvailable,
                onCheckedChange = { addScreenViewModel.updateIsAvailable(it) }
            )
        }

        Button(
            onClick = {
                val house = House(
                    address = address,
                    latitude = latitude,
                    longitude = longitude,
                    description = description,
                    bedrooms = bedrooms,
                    bathrooms = bathrooms,
                    area = area,
                    price = price,
                    imageUrl = if (imageUrl.isNotBlank()) listOf(imageUrl) else emptyList(),
                    type = type,
                    isAvailable = isAvailable
                )
                onSubmit(house)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add House")
        }
    }
}
