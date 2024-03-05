//package com.example.exam.ui
//
//import android.annotation.SuppressLint
////import android.text.Layout.Alignment
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material.FloatingActionButton
//import androidx.compose.material.Icon
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.material.TextField
//import androidx.compose.material.TextFieldDefaults
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.rememberUpdatedState
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Alignment.Companion.Center
//import androidx.compose.ui.Alignment.Companion.CenterHorizontally
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.vectorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.exam.Constants
//import com.example.exam.view_model.EntityViewModel
//import com.example.exam.R
//import com.example.exam.model.Event
//import kotlinx.coroutines.android.awaitFrame
//import kotlinx.coroutines.launch
//
//@Composable
//fun ListPageReserved(
//    navController: NavController,
//    viewModel: EntityViewModel
//) {
//    // Fetch reserved events on composition
//    LaunchedEffect(true) {
//        viewModel.fetchReservedEvents(
//            onSuccess = { reservedEvents ->
//                // Handle the reserved events data as needed
//                // You can update UI or perform any other actions
//            },
//            onError = { error ->
//                // Handle the error, show an error message or perform any other actions
//            }
//        )
//    }
//
//    // Observe the LiveData for reserved events
//    val reservedEvents by viewModel.entities.observeAsState(emptyList())
//
//    // Display a loading indicator while fetching data
//    if (reservedEvents.isEmpty()) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    } else {
//        // Call the EntitiesListReserved composable and pass the fetched reserved events
//        EntitiesListReserved(
//            entities = reservedEvents,
//            navController = navController,
//            viewModel = viewModel
//        )
//    }
//}
//
//
//@Composable
//fun EntitiesListReserved(
//    entities: List<Event>,
//    navController: NavController,
//    viewModel: EntityViewModel
//) {
//    LazyColumn(
//        modifier = Modifier.background(Color.White)
//    ) {
//        itemsIndexed(entities) { index, entity ->
//            ListItemReserved(
//                entity = entity,
//                navController = navController,
//                viewModel = viewModel
//            )
//            Spacer(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(12.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun ListItemReserved(
//    entity: Event,
//    navController: NavController,
//    viewModel: EntityViewModel
//) {
//    var showDialog by remember { mutableStateOf(false) }
//    var showNoInternet by remember { mutableStateOf(false) }
//    var showReservationSuccess by remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//
//    // LaunchedEffect to call checkInternet in a coroutine
//    LaunchedEffect(true) {
//        val isConnected = viewModel.checkInternet()
//
//        if (!isConnected) {
//            showNoInternet = true
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .background(Color.LightGray)
//            .height(120.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .clickable { navController.navigate(Constants.detailNavigation(entity.id)) }
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(12.dp)
//        ) {
//            // Name
//            Text(
//                text = "Name: " + entity.name,
//                color = Color.Black,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(vertical = 2.dp)
//            )
//            Text(
//                text = "Organizer: " + entity.organizer,
//                color = Color.Black,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(vertical = 2.dp)
//            )
//            Text(
//                text = "Category: " + entity.category,
//                color = Color.Black,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(vertical = 2.dp)
//            )
//
//            // Row containing buttons
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                horizontalArrangement = Arrangement.End,
//                verticalAlignment = Alignment.Bottom
//            ) {
//
//                // Button to reserve a spot
//                Button(
//                    onClick = {
//                        coroutineScope.launch {
//                            val isConnected = viewModel.checkInternet()
//
//                            if (isConnected) {
//                                // Call reserve function here
//                                viewModel.reserveSpot(entity.id)
//                                showReservationSuccess = true
//                            } else {
//                                showNoInternet = true
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .padding(end = 8.dp)
//                        .size(120.dp, 48.dp),  // Adjust the size of the button
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
//                ) {
//                    Text("Reserve", fontSize = 10.sp) // Adjust the text size
//                }
//            }
//        }
//
//    }
//
//
//
//    if (showNoInternet) {
//        AlertDialog(
//            onDismissRequest = {
//                showNoInternet = false
//            },
//            title = {
//                Text("No server connection")
//            },
//            text = {
//                Text("Can't perform this operation !")
//            },
//            confirmButton = {
//
//            },
//            dismissButton = {
//                Button(
//                    onClick = {
//                        showNoInternet = false
//                    }
//                ) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//}
