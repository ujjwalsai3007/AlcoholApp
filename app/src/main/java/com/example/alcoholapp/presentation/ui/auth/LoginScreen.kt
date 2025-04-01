package com.example.alcoholapp.presentation.ui.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoholapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.delay
import android.util.Log
import com.example.alcoholapp.data.FirebaseAuthManager

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = AuthViewModelFactory(LocalContext.current)
    )
) {
    val loginState by viewModel.loginState.collectAsState()
    val authMode by viewModel.authMode.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val aadhar by viewModel.aadhar.collectAsState()
    val verificationCode by viewModel.verificationCode.collectAsState()
    val verificationSent by viewModel.verificationSent.collectAsState()
    val isAgeVerified by viewModel.isAgeVerified.collectAsState()
    
    val context = LocalContext.current
    
    // For Google Sign-In
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleGoogleSignInResult(task, viewModel)
        }
    }
    
    var passwordVisible by remember { mutableStateOf(false) }
    
    // Handle login state changes
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                Log.d("LoginScreen", "Login successful, navigating...")
                delay(500) // Small delay before navigating
                onLoginSuccess()
            }
            is LoginState.Error -> {
                Log.e("LoginScreen", "Login error: ${(loginState as LoginState.Error).message}")
            }
            is LoginState.Loading -> {
                Log.d("LoginScreen", "Login in progress...")
            }
            else -> {
                Log.d("LoginScreen", "Login state: $loginState")
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // App Logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalBar,
                    contentDescription = "App Logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Name
            Text(
                text = "Alcohol App",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Auth Mode Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AuthTab(
                    title = "Login",
                    selected = authMode == AuthMode.LOGIN,
                    onClick = { viewModel.updateAuthMode(AuthMode.LOGIN) }
                )
                AuthTab(
                    title = "Sign Up",
                    selected = authMode == AuthMode.SIGNUP,
                    onClick = { viewModel.updateAuthMode(AuthMode.SIGNUP) }
                )
                AuthTab(
                    title = "Phone",
                    selected = authMode == AuthMode.PHONE,
                    onClick = { viewModel.updateAuthMode(AuthMode.PHONE) }
                )
                AuthTab(
                    title = "Aadhar",
                    selected = authMode == AuthMode.AADHAR,
                    onClick = { viewModel.updateAuthMode(AuthMode.AADHAR) }
                )
            }
            
            // Age Verification Status
            if (isAgeVerified) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "Verified",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Age Verified",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Different Auth Forms based on mode
            AnimatedContent(
                targetState = authMode,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { if (targetState.ordinal > initialState.ordinal) it else -it },
                        animationSpec = tween(300)
                    ) with
                    slideOutHorizontally(
                        targetOffsetX = { if (targetState.ordinal > initialState.ordinal) -it else it },
                        animationSpec = tween(300)
                    )
                }
            ) { mode ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (mode) {
                        AuthMode.LOGIN -> {
                            // Email Login Form
                            OutlinedTextField(
                                value = email,
                                onValueChange = { viewModel.updateEmail(it) },
                                label = { Text("Email") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = password,
                                onValueChange = { viewModel.updatePassword(it) },
                                label = { Text("Password") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Password"
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Forgot Password?",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .clickable { /* Handle forgot password */ }
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Button(
                                onClick = { 
                                    Log.d("LoginScreen", "Attempting email/password login with email: ${email}")
                                    viewModel.loginWithEmailPassword() 
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (loginState is LoginState.Loading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text("Login")
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "Or sign in with",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Button(
                                onClick = { 
                                    Log.d("LoginScreen", "Attempting Google sign-in")
                                    try {
                                        val clientId = context.getString(R.string.default_web_client_id)
                                        Log.d("LoginScreen", "Using client ID: $clientId")
                                        val signInIntent = FirebaseAuthManager.getInstance()
                                            .getGoogleSignInClient(context, clientId)
                                            .signInIntent
                                        Log.d("LoginScreen", "Launching Google sign-in intent")
                                        googleSignInLauncher.launch(signInIntent)
                                    } catch (e: Exception) {
                                        Log.e("LoginScreen", "Error launching Google sign-in", e)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFDB4437) // Google red
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.google_icon),
                                        contentDescription = "Google",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Sign in with Google")
                                }
                            }
                        }
                        
                        AuthMode.SIGNUP -> {
                            // Signup Form
                            OutlinedTextField(
                                value = email,
                                onValueChange = { viewModel.updateEmail(it) },
                                label = { Text("Email") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = password,
                                onValueChange = { viewModel.updatePassword(it) },
                                label = { Text("Password") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Password"
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (!isAgeVerified) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Warning",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Age verification required. Please verify using Aadhar.",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 14.sp
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                OutlinedButton(
                                    onClick = { viewModel.updateAuthMode(AuthMode.AADHAR) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Verify Age with Aadhar")
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            
                            Button(
                                onClick = { viewModel.signUpWithEmailPassword() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                enabled = isAgeVerified
                            ) {
                                if (loginState is LoginState.Loading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text("Sign Up")
                                }
                            }
                        }
                        
                        AuthMode.PHONE -> {
                            // Phone Verification Form
                            if (!verificationSent) {
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { viewModel.updatePhone(it) },
                                    label = { Text("Phone Number") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = "Phone"
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Phone,
                                        imeAction = ImeAction.Done
                                    )
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                if (!isAgeVerified) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = "Warning",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Age verification required. Please verify using Aadhar.",
                                            color = MaterialTheme.colorScheme.error,
                                            fontSize = 14.sp
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    OutlinedButton(
                                        onClick = { viewModel.updateAuthMode(AuthMode.AADHAR) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Verify Age with Aadhar")
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                                
                                Button(
                                    onClick = { viewModel.loginWithPhone() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    enabled = isAgeVerified
                                ) {
                                    if (loginState is LoginState.Loading) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else {
                                        Text("Send Verification Code")
                                    }
                                }
                            } else {
                                // OTP Verification Form
                                Text(
                                    text = "Enter the verification code sent to $phone",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                OutlinedTextField(
                                    value = verificationCode,
                                    onValueChange = { viewModel.updateVerificationCode(it) },
                                    label = { Text("Verification Code") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    )
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.resetVerificationState() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Change Number")
                                    }
                                    
                                    Spacer(modifier = Modifier.width(16.dp))
                                    
                                    Button(
                                        onClick = { viewModel.verifyPhoneCode() },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        if (loginState is LoginState.Loading) {
                                            CircularProgressIndicator(
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        } else {
                                            Text("Verify")
                                        }
                                    }
                                }
                            }
                        }
                        
                        AuthMode.AADHAR -> {
                            // Aadhar Verification Form
                            Text(
                                text = "Age Verification",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            Text(
                                text = "Please enter your 12-digit Aadhar number for age verification. This is required for using the app as it contains age-restricted content.",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            OutlinedTextField(
                                value = aadhar,
                                onValueChange = { 
                                    if (it.length <= 12 && it.all { char -> char.isDigit() }) {
                                        viewModel.updateAadhar(it)
                                    }
                                },
                                label = { Text("Aadhar Number") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Aadhar"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                )
                            )
                            
                            Text(
                                text = "${aadhar.length}/12 digits",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 4.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Button(
                                onClick = { viewModel.verifyAadhar() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if (loginState is LoginState.Loading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text("Verify Age")
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Your data is secure and will only be used for age verification purposes.",
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            if (loginState is LoginState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun handleGoogleSignInResult(
    completedTask: Task<GoogleSignInAccount>, 
    viewModel: AuthViewModel
) {
    try {
        Log.d("LoginScreen", "Processing Google sign-in result")
        val account = completedTask.getResult(ApiException::class.java)
        Log.d("LoginScreen", "Google sign-in account: ${account?.email}")
        
        if (account != null) {
            account.idToken?.let { idToken ->
                Log.d("LoginScreen", "Got ID token, proceeding with Firebase auth")
                viewModel.loginWithGoogle(idToken)
            } ?: run {
                Log.e("LoginScreen", "No ID token received from Google account")
            }
        } else {
            Log.e("LoginScreen", "No Google account received")
        }
    } catch (e: ApiException) {
        Log.e("LoginScreen", "Google sign-in failed", e)
    } catch (e: Exception) {
        Log.e("LoginScreen", "Unexpected error during Google sign-in", e)
    }
}

@Composable
private fun AuthTab(title: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            text = title,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(3.dp)
                .background(
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(1.5.dp)
                )
        )
    }
} 