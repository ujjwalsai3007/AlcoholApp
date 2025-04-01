package com.example.alcoholapp.presentation.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.alcoholapp.data.FirebaseAuthManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel(
    private val context: Context
) : ViewModel() {
    
    companion object {
        private const val TAG = "AuthViewModel"
    }
    
    private val authManager = FirebaseAuthManager.getInstance()
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    private val _authMode = MutableStateFlow(AuthMode.LOGIN)
    val authMode: StateFlow<AuthMode> = _authMode.asStateFlow()
    
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    
    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()
    
    private val _aadhar = MutableStateFlow("")
    val aadhar: StateFlow<String> = _aadhar.asStateFlow()
    
    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode.asStateFlow()
    
    private val _verificationSent = MutableStateFlow(false)
    val verificationSent: StateFlow<Boolean> = _verificationSent.asStateFlow()
    
    private val _isAgeVerified = MutableStateFlow(false)
    val isAgeVerified: StateFlow<Boolean> = _isAgeVerified.asStateFlow()
    
    // For phone authentication
    private var verificationId: String = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            _loginState.value = LoginState.Loading
            viewModelScope.launch {
                try {
                    val result = authManager.signInWithPhoneCredential(credential)
                    if (result.isSuccess) {
                        _loginState.value = LoginState.Success
                    } else {
                        _loginState.value = LoginState.Error("Verification failed: ${result.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    _loginState.value = LoginState.Error("Verification failed: ${e.message}")
                }
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            _loginState.value = LoginState.Error("Verification failed: ${e.message}")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            this@AuthViewModel.verificationId = verificationId
            this@AuthViewModel.resendToken = token
            _verificationSent.value = true
            _loginState.value = LoginState.Idle
        }
    }
    
    fun updateAuthMode(mode: AuthMode) {
        _authMode.value = mode
        // Reset states when changing modes
        _loginState.value = LoginState.Idle
    }
    
    fun updateEmail(email: String) {
        _email.value = email
    }
    
    fun updatePassword(password: String) {
        _password.value = password
    }
    
    fun updatePhone(phone: String) {
        _phone.value = phone
    }
    
    fun updateAadhar(aadhar: String) {
        _aadhar.value = aadhar
    }
    
    fun updateVerificationCode(code: String) {
        _verificationCode.value = code
    }
    
    fun updateAgeVerification(isVerified: Boolean) {
        _isAgeVerified.value = isVerified
    }
    
    fun loginWithEmailPassword() {
        if (_email.value.isEmpty() || _password.value.isEmpty()) {
            _loginState.value = LoginState.Error("Email and password cannot be empty")
            return
        }
        
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val result = authManager.signInWithEmailPassword(_email.value, _password.value)
                if (result.isSuccess) {
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Login failed")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    fun signUpWithEmailPassword() {
        if (!_isAgeVerified.value) {
            _loginState.value = LoginState.Error("Age verification is required")
            return
        }
        
        if (_email.value.isEmpty() || _password.value.isEmpty()) {
            _loginState.value = LoginState.Error("Email and password cannot be empty")
            return
        }
        
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val result = authManager.createUserWithEmailPassword(_email.value, _password.value)
                if (result.isSuccess) {
                    // Send email verification
                    authManager.sendEmailVerification()
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Sign up failed")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    fun loginWithGoogle(idToken: String) {
        Log.d(TAG, "Starting Google sign-in with token: ${idToken.take(10)}...")
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                Log.d(TAG, "Calling FirebaseAuthManager.signInWithGoogle")
                val result = authManager.signInWithGoogle(idToken)
                if (result.isSuccess) {
                    Log.d(TAG, "Google sign-in success: ${result.getOrNull()?.email}")
                    _isAgeVerified.value = true // Assuming Google sign-in implies age verification
                    _loginState.value = LoginState.Success
                } else {
                    val error = result.exceptionOrNull()
                    Log.e(TAG, "Google sign-in failure", error)
                    _loginState.value = LoginState.Error(error?.message ?: "Google sign-in failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during Google sign-in", e)
                _loginState.value = LoginState.Error(e.message ?: "Google Sign-In failed")
            }
        }
    }
    
    fun loginWithPhone() {
        if (!_isAgeVerified.value) {
            _loginState.value = LoginState.Error("Age verification is required")
            return
        }
        
        if (_phone.value.isEmpty()) {
            _loginState.value = LoginState.Error("Phone number is required")
            return
        }
        
        _loginState.value = LoginState.Loading
        try {
            // Format phone number with country code if not already formatted
            val formattedPhone = if (_phone.value.startsWith("+")) _phone.value else "+91${_phone.value}"
            authManager.sendPhoneVerificationCode(context, formattedPhone, callbacks)
        } catch (e: Exception) {
            _loginState.value = LoginState.Error(e.message ?: "Failed to send verification code")
        }
    }
    
    fun verifyPhoneCode() {
        if (_verificationCode.value.isEmpty()) {
            _loginState.value = LoginState.Error("Verification code is required")
            return
        }
        
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val credential = PhoneAuthProvider.getCredential(verificationId, _verificationCode.value)
                val result = authManager.signInWithPhoneCredential(credential)
                
                if (result.isSuccess) {
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Verification failed")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Verification failed")
            }
        }
    }
    
    fun verifyAadhar() {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                // In a real app, you would verify the Aadhar number with an API
                if (_aadhar.value.length == 12) {
                    // Simulate API call delay
                    kotlinx.coroutines.delay(1500)
                    _isAgeVerified.value = true
                    _loginState.value = LoginState.Idle
                } else {
                    _loginState.value = LoginState.Error("Valid 12-digit Aadhar number is required")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Aadhar verification failed")
            }
        }
    }
    
    fun resetVerificationState() {
        _verificationSent.value = false
        _verificationCode.value = ""
    }
    
    fun signOut() {
        authManager.signOut()
    }
}

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

enum class AuthMode {
    LOGIN, SIGNUP, PHONE, AADHAR
} 