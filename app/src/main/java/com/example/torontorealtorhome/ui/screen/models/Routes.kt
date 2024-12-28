package com.example.torontorealtorhome.ui.screen.models

sealed class Routes(val route: String) {
    data object Map : Routes("map")
    data object List : Routes("list")
    data object Add : Routes("add")
    data object Favorites : Routes("favorites")
    data object Account : Routes("account")
    data object SignUp : Routes("signup")
    data object Filter : Routes("filter") // No parameters, so keep it as an object
    data class Detail(val houseId: String) : Routes("detail/$houseId")
    {
        companion object {
            const val ROUTE = "detail/{houseId}"
        }
    }
}