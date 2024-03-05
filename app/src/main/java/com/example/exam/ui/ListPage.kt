package com.example.exam.ui

import android.annotation.SuppressLint
import android.util.Log
//import android.text.Layout.Alignment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.exam.Constants
import com.example.exam.view_model.EntityViewModel
import com.example.exam.R
import com.example.exam.model.Car
import kotlinx.coroutines.launch

//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun ListPage(
//    navController: NavController,
//    viewModel: EntityViewModel
//) {
//    val entities = viewModel.entities.observeAsState()
//    val toastMessage by rememberUpdatedState(newValue = viewModel.toastMessage.collectAsState().value)
//
//
//    var showNoInternet by remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//    var progressLoading by remember { mutableStateOf(false) }
//
//
//
//
//    if (toastMessage.isNotBlank()) {
//        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_LONG).show()
//        viewModel.clearToastMessage()
//    }
//
//    Surface(
//        Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//    ) {
//        Scaffold(
//
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
//                        Log.d("ListPage", "Add button clicked")
//                        coroutineScope.launch {
//                            val isConnected = viewModel.checkInternet()
//
//                            if (isConnected) {
//                                navController.navigate(Constants.NAVIGATION_CREATE)
//                            } else {
//                                showNoInternet = true
//                            }
//                        }
//                    }
//                ) {
//                    Icon(
//                        ImageVector.vectorResource(id = R.drawable.add_button),
//                        contentDescription = "",
//                        tint = Color.Black
//                    )
//                }
//            }
//        ) {
//
//            EntitiesList(
//                entities = entities.value.orEmpty(),
//                navController = navController,
//                viewModel = viewModel
//            )
//            if (showNoInternet) {
//                AlertDialog(
//                    onDismissRequest = {
//                        showNoInternet = false
//                    },
//                    title = {
//                        Text("No server connection")
//                    },
//                    text = {
//                        Text("Can't perform this operation !")
//                    },
//                    confirmButton = {
//
//                    },
//                    dismissButton = {
//                        Button(
//                            onClick = {
//                                showNoInternet = false
//                            }
//                        ) {
//                            Text("Cancel")
//                        }
//                    }
//                )
//            }
//        }
//    }
//    if (progressLoading){
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            contentAlignment = Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }
//}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListPage(
    navController: NavController,
    viewModel: EntityViewModel
) {
    val entities = viewModel.entities.observeAsState()
    val toastMessage by rememberUpdatedState(newValue = viewModel.toastMessage.collectAsState().value)

    var showNoInternet by remember { mutableStateOf(false) }
    var progressLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Function to handle retry action
    val retryAction: () -> Unit = {
        coroutineScope.launch {
            val isConnected = viewModel.checkInternet()

            if (isConnected) {
                viewModel.refreshAction()
            } else {
                showNoInternet = true
            }
        }
    }

    // Check if entities are available or if loading is in progress
    val isLoading = entities.value == null && !progressLoading

    if (isLoading) {
        // Show loading indicator if entities are being fetched
        progressLoading = true
    }

    if (toastMessage.isNotBlank()) {
        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_LONG).show()
        viewModel.clearToastMessage()
    }

    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            val isConnected = viewModel.checkInternet()

                            if (isConnected) {
                                navController.navigate(Constants.NAVIGATION_CREATE)
                            } else {
                                showNoInternet = true
                            }
                        }
                    }
                ) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.add_button),
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            }
        ) {
            // Display list of entities if available
            if (entities.value != null) {
                EntitiesList(
                    entities = entities.value!!,
                    navController = navController,
                    viewModel = viewModel
                )
            }

            // Show offline message with retry option if there's no internet connection
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
                        Button(
                            onClick = {
                                showNoInternet = false
                                retryAction()
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                )
            }
        }
    }

    // Show loading indicator if entities are being fetched
    if (progressLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun EntitiesList(
    entities: List<Car>,
    navController: NavController,
    viewModel: EntityViewModel
) {

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.background(Color.White)
    ) {
        itemsIndexed(entities) { index, entity ->

            ListItem(
                entity,
                navController,
                viewModel
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }
    }
}


@Composable
fun ListItem(
    entity: Car,
    navController: NavController,
    viewModel: EntityViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var showNoInternet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { navController.navigate(Constants.detailNavigation(entity.id)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Name
            Text(
                text = "Name: " + entity.name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Organizer
            Text(
                text = "Supplier: " + entity.supplier,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Category
            Text(
                text = "Type: " + entity.type,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val isConnected = viewModel.checkInternet()

                            if (isConnected) {
                                navController.navigate(Constants.editNavigation(entity.id))
                            } else {
                                showNoInternet = true
                            }
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        tint = Color.Black
                    )
                }
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val isConnected = viewModel.checkInternet()

                            if (isConnected) {
                                showDialog = true

                            } else {
                                showNoInternet = true
                            }
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = Color.Black
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(entity.type)
            },
            text = {
                Text("Are you sure you want to delete this?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        viewModel.deleteEntity(entity)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
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
                Text("Can't perform this operation !")
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