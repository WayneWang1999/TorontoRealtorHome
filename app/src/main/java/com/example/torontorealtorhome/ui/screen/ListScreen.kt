package com.example.torontorealtorhome.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.torontorealtorhome.ui.screen.models.Routes
import com.example.torontorealtorhome.ui.screen.viewmodels.ListScreenViewModel
import com.example.torontorealtorhome.ui.screen.viewmodels.UserStateViewModel
import com.example.torontorenthome.models.House


@Composable
fun ListScreen(
    navController: NavHostController,
    userStateViewModel: UserStateViewModel,
    listScreenViewModel: ListScreenViewModel = hiltViewModel(),
) {

    val houseList by listScreenViewModel.getListHouses(userStateViewModel).collectAsState()
    val isLoading by listScreenViewModel.isLoading.collectAsState()

    val isLoggedIn by userStateViewModel.isLoggedIn.collectAsState()

    if (isLoggedIn) {
        when {
            isLoading -> LoadingIndicator()
            houseList.isEmpty() -> EmptyRealtorHouseMessage()
            else -> HouseList(
                houses = houseList,
                onHouseClick = { houseId ->
                    navController.navigate(Routes.Detail(houseId).route) {
                        launchSingleTop = true
                    }
                },
//                onFavoriteToggle = { houseId ->
//                    userStateViewModel.toggleFavorite(houseId)
//                }
            )
        }
    } else {
        LoggedOutMessage()
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyRealtorHouseMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No realtor houses found.", fontSize = 16.sp)
    }
}

@Composable
fun LoggedOutMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Please go to Account log in to view your  houses.",
            fontSize = 16.sp,
        )
    }
}

@Composable
fun HouseList(
    houses: List<House>,
    onHouseClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(houses.size) { index ->
            val house = houses[index]

            // Animating the item when it enters the screen
            val scale = remember { androidx.compose.animation.core.Animatable(0.8f) }
            LaunchedEffect(Unit) {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = androidx.compose.animation.core.spring(
                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                        stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                    )
                )
            }

            Box(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value
                    )
                    .clickable { onHouseClick(house.houseId) }
            ) {
                HouseItem(
                    houseId = house.houseId,
                    imageUrl = house.imageUrl,
                    price = house.price,
                    bedrooms = house.bedrooms,
                    address = house.address,
                    bathrooms = house.bathrooms,
                    area = house.area,
                    createTime = house.createTime,
                    isFavorite = true,
                    modifier = Modifier.animateContentSize() // Smooth animation for content size changes
                )
            }
        }
    }
}