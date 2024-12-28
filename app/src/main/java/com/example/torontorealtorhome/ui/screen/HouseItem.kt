package com.example.torontorealtorhome.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HouseItem(
    houseId: String,
    imageUrl: List<String>, // Image URL for the house image
    price: Int, // Price of the house
    bedrooms: Int, // Number of bedrooms
    address: String, // Description of the house
    bathrooms: Int,
    area: Int,
    createTime: String,
    isFavorite: Boolean,
    modifier: Modifier // Mandatory Modifier parameter
) {
    val formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(price)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HouseImagesPagerWithArrows(imageUrls = imageUrl)
        // Price and Favorite Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Price: $$formattedPrice",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        // Description
        Text(
            text = address,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        // Bedrooms
        Text(
            text = "Bedrooms: $bedrooms   Bathrooms:$bathrooms  Area:$area",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "$createTime ago",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )


    }
}

@Composable
fun HouseImagesPagerWithArrows(imageUrls: List<String>) {
    val pagerState =
        rememberPagerState(pageCount = { imageUrls.size }) // Passing pageCount as a function
    val coroutineScope = rememberCoroutineScope() // To handle coroutines

    Box(modifier = Modifier.fillMaxWidth()) {
        // HorizontalPager to display images
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 8.dp)
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = "House Image",
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Left Arrow
        IconButton(
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage > 0) {
                        pagerState.scrollToPage(pagerState.currentPage - 1)
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous",
                tint = Color.Black
            )
        }

        // Right Arrow
        IconButton(
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage < imageUrls.size - 1) {
                        pagerState.scrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) {
            Icon(
              //  imageVector = Icons.Default.ArrowForward,
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next",
                tint = Color.Black
            )
        }
    }
}