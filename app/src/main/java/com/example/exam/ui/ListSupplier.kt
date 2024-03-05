import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button

import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.exam.Constants
import com.example.exam.view_model.EntityViewModel
import com.example.exam.model.Car


//@Composable
//fun ListSupplier(
//    navController: NavController,
//    viewModel: EntityViewModel
//) {
//    var showNoInternet by remember { mutableStateOf(false) }
//    var progressLoading by remember { mutableStateOf(false) }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    // Observe in-progress events using LiveData
//    val analitics by viewModel.analitics.observeAsState()
//
//    LaunchedEffect(key1 = true) {
//        try {
//            progressLoading = true
//            viewModel.getAnalitics()
//        } catch (e: Exception) {
//            // Handle exception, show error message, etc.
//        } finally {
//            progressLoading = false
//        }
//    }
//
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Gray)
//    ) {
//        if (progressLoading) {
//            // Your loading indicator code
//        } else {
//            // Display in-progress events in your UI
//            if (analitics.isNullOrEmpty()) {
//                Text(
//                    text = "No orders found.",
//                    color = Color.Black,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(16.dp)
//                )
//            } else {
//                EntitiesListAnalitics(
//                    entities = analitics!!,
//                    navController = navController,
//                    viewModel = viewModel
//                )
//            }
//        }
//
//
//        // Display an alert dialog for no internet connection
//        if (showNoInternet) {
//            AlertDialog(
//                onDismissRequest = {
//                    showNoInternet = false
//                },
//                title = {
//                    Text("No server connection")
//                },
//                text = {
//                    Text("Can't perform this operation!")
//                },
//                confirmButton = {
//
//                },
//                dismissButton = {
//                    Button(
//                        onClick = {
//                            showNoInternet = false
//                        }
//                    ) {
//                        Text("Cancel")
//                    }
//                }
//            )
//        }
//    }
//}

@Composable
fun ListSupplier(
    navController: NavController,
    viewModel: EntityViewModel
){
    val context = LocalContext.current
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    val isNetworkAvailable by remember {
        mutableStateOf(
            connectivityManager?.let { cm ->
                val network = cm.activeNetwork
                val capabilities = cm.getNetworkCapabilities(network)
                capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            } ?: false
        )
    }

    if (!isNetworkAvailable) {
        Text(
            text = "No internet connection. Please try again later.",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    var progressLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Observe analytics using LiveData
    val analitics by viewModel.analitics.observeAsState()

    LaunchedEffect(key1 = true) {
        try {
            progressLoading = true
            viewModel.getAnalitics()
        } catch (e: Exception) {
            // Handle exception, show error message, etc.
        } finally {
            progressLoading = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().background(Color.Gray)
    ) {
        if (progressLoading) {
            // Your loading indicator code
        } else {
            // Display analytics in your UI
            if (analitics.isNullOrEmpty()) {
                Text(
                    text = "No orders found.",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                EntitiesListAnalitics(
                    entities = analitics!!,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }

        // Display an alert dialog for no internet connection
        if (!isNetworkAvailable) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dismiss request
                },
                title = {
                    Text("No internet connection")
                },
                text = {
                    Text("Can't perform this operation!")
                },
                confirmButton = {
                    // Your confirm button code
                },
                dismissButton = {
                    // Your dismiss button code
                }
            )
        }
    }
}

@Composable
fun EntitiesListAnalitics(
    entities: List<Car>,
    navController: NavController,
    viewModel: EntityViewModel
) {



    LazyColumn(
        modifier = Modifier.background(Color.White)
    ) {
        itemsIndexed(entities) { index, entity ->
            ListItemAnalitics(
                entity = entity,
                navController = navController,
                viewModel = viewModel
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
fun ListItemAnalitics(
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
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Supplier: " + entity.supplier,
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
