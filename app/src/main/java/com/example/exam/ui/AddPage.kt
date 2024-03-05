package com.example.exam.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.exam.view_model.EntityViewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddPage(
    navController: NavController,
    viewModel: EntityViewModel
) {
    val toastMessage by rememberUpdatedState(newValue = viewModel.toastMessage.collectAsState().value)
    val coroutineScope = rememberCoroutineScope()
    var progressLoading by remember { mutableStateOf(false) }

    val currentName = remember {
        mutableStateOf("")
    }

    val currentSupplier = remember {
        mutableStateOf("")
    }

    val currentDetails = remember {
        mutableStateOf("")
    }

    val currentStatus = remember {
        mutableStateOf("")
    }

    val currentQuantity = remember {
        mutableStateOf("")
    }

    val currentType = remember {
        mutableStateOf("")
    }

    if (toastMessage.isNotBlank()) {
        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_LONG).show()
        viewModel.clearToastMessage()
    }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Scaffold(topBar = { }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = Color.White)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                TextField(
                    value = currentName.value,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    ),
                    onValueChange = { value ->
                        currentName.value = value
                    },
                    label = { Text(text = "Name") }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                TextField(
                    value = currentSupplier.value,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    ),
                    onValueChange = { value ->
                        currentSupplier.value = value
                    },
                    label = { Text(text = "Team") }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                TextField(
                    value = currentDetails.value,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    ),
                    onValueChange = { value ->
                        currentDetails.value = value
                    },
                    label = { Text(text = "Details") }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                TextField(
                    value = currentStatus.value,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    ),
                    onValueChange = { value ->
                        currentStatus.value = value
                    },
                    label = { Text(text = "Status") }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                TextField(
                    value = currentQuantity.value,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    onValueChange = { value ->
                        currentQuantity.value = value
                    },
                    label = { Text(text = "Participants") }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                TextField(
                    value = currentType.value,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    ),
                    onValueChange = { value ->
                        currentType.value = value
                    },
                    label = { Text(text = "Type") }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    onClick = {
                        coroutineScope.launch {
                            progressLoading = true
                            try {
                                viewModel.createEntity(
                                    name = currentName.value,
                                    team = currentSupplier.value,
                                    details = currentDetails.value,
                                    status = currentStatus.value,
                                    participants = currentQuantity.value.toInt(),
                                    type = currentType.value
                                )
                                awaitFrame()
                                progressLoading = false
                                navController.popBackStack()
                            } catch (e: Exception) {
                                viewModel.updateToastMessage("Invalid Fields!")
                                progressLoading = false
                            }
                        }
                    }
                ) {
                    Text(text = "ADD")
                }
            }
        }
    }

    if (progressLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
