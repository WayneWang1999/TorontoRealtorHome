package com.example.torontorealtorhome.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.torontorealtorhome.ui.screen.models.Routes
import com.example.torontorealtorhome.ui.screen.viewmodels.ListScreenViewModel
import com.example.torontorealtorhome.ui.screen.viewmodels.UserStateViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(
    navController: NavHostController,
    userStateViewModel: UserStateViewModel,
    mapScreenViewModel: ListScreenViewModel = hiltViewModel()
) {

    val selectedHouse by mapScreenViewModel.selectedHouse.collectAsState()
    val houses by mapScreenViewModel.getListHouses(userStateViewModel).collectAsState()
    val isLoggedIn by userStateViewModel.isLoggedIn.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light Gray Background
    ) {
                // MapView
        Box(modifier = Modifier.fillMaxSize()) {
            // GoogleMap fills the background
            val toronto = LatLng(43.6532, -79.3832)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(toronto, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    mapScreenViewModel.setSelectedHouse(null) // Clear the selected house
                }
            ) {
                // Add markers for houses locations
                houses.forEach { house ->
                    Marker(
                        state = MarkerState(LatLng(house.latitude, house.longitude)),
                        title = house.address,
                        snippet = house.address,
                        onClick = {
                            mapScreenViewModel.setSelectedHouse(house)
                            true // Return true to indicate that the click was handled
                        }
                    )
                }
            }
            selectedHouse?.let { house ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Box(modifier = Modifier.clickable {
                        navController.navigate(Routes.Detail(house.houseId).route){
                            launchSingleTop = true
                        }
                    }){
                        HouseItem(
                            houseId=house.houseId,
                            imageUrl = house.imageUrl,
                            price = house.price,
                            bedrooms = house.bedrooms,
                            address = house.address,
                            bathrooms = house.bathrooms,
                            area = house.area,
                            createTime = house.createTime,
                            isFavorite = true,
                            modifier = Modifier.animateContentSize()
                        )}
                }
            }
        }

    }

}

