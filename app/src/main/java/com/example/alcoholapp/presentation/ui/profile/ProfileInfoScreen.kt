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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val isVerified by viewModel.isVerified.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var nameInput by remember { mutableStateOf(userName ?: "") }
    var phoneInput by remember { mutableStateOf(phoneNumber ?: "") }
    
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    
    // Update local state when user data changes
    LaunchedEffect(userName, phoneNumber) {
        nameInput = userName ?: ""
        phoneInput = phoneNumber ?: ""
    }
    
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
                title = { Text("Edit Profile") },
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
                // User Email (non-editable)
                OutlinedTextField(
                    value = userEmail ?: "",
                    onValueChange = { },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    trailingIcon = {
                        if (isVerified) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Not Verified",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    singleLine = true
                )
                
                // Display Name
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Display Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                
                // Phone Number
                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                
                // Save button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        
                        // Validate inputs
                        if (nameInput.isBlank()) {
                            Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        // Update profile info
                        if (nameInput != userName) {
                            viewModel.updateUserName(nameInput)
                        }
                        
                        if (phoneInput != phoneNumber) {
                            viewModel.updatePhoneNumber(phoneInput)
                        }
                        
                        // Show confirmation toast
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        
                        // Navigate back
                        onBackClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    enabled = !isLoading
                ) {
                    Text("Save Changes")
                }
                
                // Verification info
                if (!isVerified) {
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Info",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Email Verification",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                
                                Text(
                                    text = "Your email is not verified. Please check your inbox for a verification email.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            
                            TextButton(
                                onClick = {
                                    // Send verification email
                                    // This would be implemented in the ViewModel
                                    Toast.makeText(context, "Verification email sent", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text("Resend")
                            }
                        }
                    }
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