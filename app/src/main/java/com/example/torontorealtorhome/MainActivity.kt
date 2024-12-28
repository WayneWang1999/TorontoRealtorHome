package com.example.torontorealtorhome

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.torontorealtorhome.ui.screen.AccountScreen
import com.example.torontorealtorhome.ui.screen.AddScreen
import com.example.torontorealtorhome.ui.screen.ListScreen
import com.example.torontorealtorhome.ui.screen.MapScreen
import com.example.torontorealtorhome.ui.screen.SignUpScreen
import com.example.torontorealtorhome.ui.screen.models.BottomNavItem
import com.example.torontorealtorhome.ui.screen.models.Routes
import com.example.torontorealtorhome.ui.screen.viewmodels.UserStateViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MyApp() //
        }

        if (!isLocationPermissionGranted()) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun MyApp() {

    val navController = rememberNavController()
    val currentRoute = currentRoute(navController)

    Scaffold(
        //Note:The BottomNavigationBar adjusts its visibility based on the current route
        // (FILTER and DETAIL screens do not show it). This provides a better user experience.
        bottomBar = {
            if (currentRoute !in listOf(Routes.Filter.route) && !isDetailRoute(currentRoute)) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHostContainer(
            navController,
            Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = currentRoute(navController)
    // List of navigation items
    val navItems = listOf(
        BottomNavItem(Routes.Map.route, Icons.Filled.LocationOn, "Map"),
        BottomNavItem(Routes.List.route, Icons.AutoMirrored.Filled.List, "List"),
        BottomNavItem(Routes.Add.route, Icons.Filled.AddBox, "Add"),
        BottomNavItem(Routes.Account.route, Icons.Filled.AccountCircle, "Account"),


    )

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigateSingleTopTo(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    modifier: Modifier,
) {
    val userStateViewModel: UserStateViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Routes.Map.route,
        modifier = modifier
    ) {


        composable(Routes.Map.route) {
            MapScreen(
                navController,
                userStateViewModel,
            )
        }
        composable(Routes.List.route) {
            ListScreen(
                navController,
                userStateViewModel,
            )
        }
        composable(Routes.Add.route) {
            AddScreen(
                onSubmit = { house ->
                    // Log the house object
                    Log.d("AddScreen", "House submitted: $house")
                    // Insert house into Firestore
                    val firestore = FirebaseFirestore.getInstance()
                    val currentUser = userStateViewModel.currentUser
                    val currentUserId=currentUser.value!!.uid
                    firestore.collection("houses")
                        .add(house)
                        .addOnSuccessListener { documentReference ->
                            val houseId = documentReference.id
                            Log.d("AddScreen", "House added successfully with ID: $houseId")

                            // Update the current user's realtor house IDs
                            if (currentUser != null) {
                                firestore.collection("realtors")
                                    .document(currentUserId) // Assuming currentUser.id gives the user ID
                                    .update("realtorHouseIds", FieldValue.arrayUnion(houseId))
                                    .addOnSuccessListener {
                                        userStateViewModel.fetchRealtorHouseIds(currentUserId)
                                        Log.d("AddScreen", "House ID added to realtor's house list successfully.")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("AddScreen", "Failed to add house ID to realtor's house list: ${exception.message}", exception)
                                    }
                            }
                            // Navigate back after successful insertion
                            navController.popBackStack()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("AddScreen", "Failed to add house: ${exception.message}", exception)
                        }
                }

            )
        }

        composable(Routes.Account.route) {
            AccountScreen(
                navController,
                userStateViewModel
            )
        }


        composable(Routes.SignUp.route) {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                navController
            )
        }

    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(this@navigateSingleTopTo.graph.startDestinationId) {
            saveState = true
        }
    }

@Composable
fun currentRoute(navController: NavHostController): String? {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    return currentBackStackEntry?.destination?.route
}

fun isDetailRoute(route: String?): Boolean {
    return route?.startsWith("detail/") == true
}

