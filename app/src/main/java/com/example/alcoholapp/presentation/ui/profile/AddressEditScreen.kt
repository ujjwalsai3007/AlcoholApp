package com.example.alcoholapp.presentation.ui.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.alcoholapp.domain.model.Address
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressEditScreen(
    viewModel: ProfileViewModel,
    address: Address? = null,
    onBackClick: () -> Unit,
    onSaveComplete: () -> Unit
) {
    val isNewAddress = address == null
    val title = if (isNewAddress) "Add Address" else "Edit Address"
    
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    
    // Form fields
    var label by remember { mutableStateOf(address?.label ?: "") }
    var street by remember { mutableStateOf(address?.street ?: "") }
    var city by remember { mutableStateOf(address?.city ?: "") }
    var state by remember { mutableStateOf(address?.state ?: "") }
    var postalCode by remember { mutableStateOf(address?.postalCode ?: "") }
    var isDefault by remember { mutableStateOf(address?.isDefault ?: false) }
    
    // Error states
    var labelError by remember { mutableStateOf(false) }
    var streetError by remember { mutableStateOf(false) }
    var cityError by remember { mutableStateOf(false) }
    var stateError by remember { mutableStateOf(false) }
    var postalCodeError by remember { mutableStateOf(false) }
    
    // Show error toast
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Address label
                OutlinedTextField(
                    value = label,
                    onValueChange = { 
                        label = it
                        labelError = false
                    },
                    label = { Text("Label (e.g. Home, Work)") },
                    leadingIcon = { Icon(Icons.Default.Label, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = labelError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    supportingText = if (labelError) {
                        { Text("Please enter a label") }
                    } else null
                )
                
                // Street
                OutlinedTextField(
                    value = street,
                    onValueChange = { 
                        street = it 
                        streetError = false
                    },
                    label = { Text("Street Address") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = streetError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    supportingText = if (streetError) {
                        { Text("Please enter a street address") }
                    } else null
                )
                
                // City
                OutlinedTextField(
                    value = city,
                    onValueChange = { 
                        city = it 
                        cityError = false
                    },
                    label = { Text("City") },
                    leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = cityError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    supportingText = if (cityError) {
                        { Text("Please enter a city") }
                    } else null
                )
                
                // State and Postal Code in a row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // State
                    OutlinedTextField(
                        value = state,
                        onValueChange = { 
                            state = it 
                            stateError = false
                        },
                        label = { Text("State/Province") },
                        modifier = Modifier.weight(1f),
                        isError = stateError,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Right) }
                        ),
                        supportingText = if (stateError) {
                            { Text("Required") }
                        } else null
                    )
                    
                    // Postal Code
                    OutlinedTextField(
                        value = postalCode,
                        onValueChange = { 
                            postalCode = it 
                            postalCodeError = false
                        },
                        label = { Text("Postal Code") },
                        modifier = Modifier.weight(1f),
                        isError = postalCodeError,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        supportingText = if (postalCodeError) {
                            { Text("Required") }
                        } else null
                    )
                }
                
                // Set as default checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDefault,
                        onCheckedChange = { isDefault = it }
                    )
                    
                    Text(
                        text = "Set as default address",
                        modifier = Modifier.clickable { isDefault = !isDefault }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Save button
                Button(
                    onClick = {
                        // Validate inputs
                        var hasError = false
                        
                        if (label.isBlank()) {
                            labelError = true
                            hasError = true
                        }
                        
                        if (street.isBlank()) {
                            streetError = true
                            hasError = true
                        }
                        
                        if (city.isBlank()) {
                            cityError = true
                            hasError = true
                        }
                        
                        if (state.isBlank()) {
                            stateError = true
                            hasError = true
                        }
                        
                        if (postalCode.isBlank()) {
                            postalCodeError = true
                            hasError = true
                        }
                        
                        if (hasError) return@Button
                        
                        // Create/update address
                        val updatedAddress = Address(
                            id = address?.id ?: "",
                            label = label,
                            street = street,
                            city = city,
                            state = state,
                            postalCode = postalCode,
                            isDefault = isDefault
                        )
                        
                        if (isNewAddress) {
                            viewModel.addAddress(updatedAddress)
                        } else {
                            viewModel.updateAddress(updatedAddress)
                        }
                        
                        // Show success message and navigate back
                        val message = if (isNewAddress) "Address added" else "Address updated"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        onSaveComplete()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Save Address")
                }
            }
            
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
} 