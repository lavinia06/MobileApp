package com.example.exam.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.exam.Constants
import com.example.exam.view_model.EntityViewModel
import com.example.exam.model.Car
import kotlinx.coroutines.launch


@Composable
fun ListPageStaff(
    navController: NavController,
    viewModel: EntityViewModel
) {
    var showNoInternet by remember { mutableStateOf(false) }
    var progressLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()


    val getCarsTypes by viewModel.getCarsTypesViewModel.observeAsState()

    LaunchedEffect(key1 = true) {
        try {
            progressLoading = true
            viewModel.getCarsTypes()
        } catch (e: Exception) {
            // Handle exception, show error message, etc.
        } finally {
            progressLoading = false
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        if (progressLoading) {
            // Your loading indicator code
        } else {
            // Display in-progress events in your UI
            if (!getCarsTypes.isNullOrEmpty()) {
                EntitiesListStaff(
                    entities = getCarsTypes!!,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }


        // Display an alert dialog for no internet connection
        if (showNoInternet) {
            AlertDialog(
                onDismissRequest = {
                    showNoInternet = false
                },
                title = {
                    Text("No server connection")
                },
                text = {
                    Text("Can't perform this operation!")
                },
                confirmButton = {

                },
                dismissButton = {
                    Button(
                        onClick = {
                            showNoInternet = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}


//@Composable
//fun EntitiesListStaff(
//    entities: List<Car>,
//    navController: NavController,
//    viewModel: EntityViewModel
//) {
//    // Group cars by type and calculate total quantities for each type
//    val groupedCars = entities.groupBy { it.type }
//    LazyColumn(
//        modifier = Modifier.background(Color.White)
//    ) {
//        groupedCars.forEach { (type, cars) ->
//            // Display each type of car along with its total quantity
//            item {
//                Text(
//                    text = "Type: $type - Total Quantity: ${cars.size}",
//                    color = Color.Black,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp, horizontal = 16.dp)
//                )
//            }
//            // Display the list of cars for this type
//            itemsIndexed(cars) { index, car ->
//                ListItemStuff(
//                    entity = car,
//                    navController = navController,
//                    viewModel = viewModel
//                )
//            }
//        }
//    }
//}
@Composable
fun EntitiesListStaff(
    entities: List<Car>,
    navController: NavController,
    viewModel: EntityViewModel
) {
    var selectedType by remember { mutableStateOf<String?>(null) }

    // Group cars by type and calculate total quantities for each type
    val groupedCars = entities.groupBy { it.type }

    LazyColumn(
        modifier = Modifier.background(Color.White)
    ) {
        groupedCars.forEach { (type, cars) ->
            // Display each type of car along with its total quantity
            item {
                Text(
                    text = "Type: $type - Total Quantity: ${cars.size}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable {
                            // Toggle the selected type when clicked
                            selectedType = if (selectedType == type) null else type
                        }
                )
            }
            // Display the list of cars for this type only if it matches the selected type
            if (type == selectedType) {
                itemsIndexed(cars) { index, car ->
                    ListItemStuff(
                        entity = car,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}


//@Composable
//fun ListItemStuff(
//    entity: Car,
//    viewModel: EntityViewModel,
//    navController: NavController
//) {
//    var showNoInternet by remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//    val stableNavController = rememberUpdatedState(navController)
//    var showSuccess by remember { mutableStateOf(false) }
//
//
//    Box(
//        modifier = Modifier
//            .background(Color.LightGray)
//            .height(120.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .clickable {
//                stableNavController.value.navigate(Constants.detailNavigation(entity.id))
//            }
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(12.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 2.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Name: " + entity.name,
//                    color = Color.Black,
//                    fontWeight = FontWeight.Bold,
//                )
//
//                // Change this line
//                Button(
//                    onClick = {
//                        coroutineScope.launch {
//                            val isConnected = viewModel.checkInternet()
//
//                            if (isConnected) {
//                                // Call enroll function here instead of reserve
//                                viewModel.requestCar(entity.type)
//                                showSuccess = true
//                            } else {
//                                showNoInternet = true
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .padding(end = 8.dp)
//                        .size(80.dp, 40.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
//                ) {
//                    Text("Request", fontSize = 10.sp)
//                }
//
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 2.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Status: " + entity.status,
//                    color = Color.Black,
//                    fontWeight = FontWeight.Bold,
//                )
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 2.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Type: " + entity.type,
//                    color = Color.Black,
//                    fontWeight = FontWeight.Bold,
//                )
//            }
//        }
//    }
//
//    if (showSuccess) {
//        AlertDialog(
//            onDismissRequest = {
//                showSuccess = false
//            },
//            title = {
//                Text("Request Success")
//            },
//            text = {
//                Text("You have successfully requested a spot for this car.")
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showSuccess = false
//                    }
//                ) {
//                    Text("OK")
//                }
//            },
//        )
//    }
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
//                Text("Can't perform this operation!")
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
@Composable
fun ListItemStuff(
    entity: Car,
    viewModel: EntityViewModel,
    navController: NavController
) {
    var showNoInternet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val stableNavController = rememberUpdatedState(navController)
    var showSuccess by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                stableNavController.value.navigate(Constants.detailNavigation(entity.id))
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name: " + entity.name,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )

                // Change this line
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val isConnected = viewModel.checkInternet()

                            if (isConnected) {
                                viewModel.requestCar(entity.type)
                                showSuccess = true
                            } else {
                                showNoInternet = true
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(100.dp, 60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Request", fontSize = 10.sp)
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: " + entity.status,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Type: " + entity.type,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = {
                showSuccess = false
            },
            title = {
                Text("Request Car Success")
            },
            text = {
                Text("You have successfully requested a spot for this car.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccess = false
                    }
                ) {
                    Text("OK")
                }
            },
        )
    }

    if (showNoInternet) {
        AlertDialog(
            onDismissRequest = {
                showNoInternet = false
            },
            title = {
                Text("No server connection")
            },
            text = {
                Text("Can't perform this operation!")
            },
            confirmButton = {

            },
            dismissButton = {
                Button(
                    onClick = {
                        showNoInternet = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
