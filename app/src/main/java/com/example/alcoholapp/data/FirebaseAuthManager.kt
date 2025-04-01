package com.example.alcoholapp.data

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class FirebaseAuthManager {
    
    private val auth = FirebaseAuth.getInstance()
    
    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    
    private val _isUserVerified = MutableStateFlow(auth.currentUser?.isEmailVerified == true)
    val isUserVerified: StateFlow<Boolean> = _isUserVerified.asStateFlow()
    
    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
            _isUserVerified.value = firebaseAuth.currentUser?.isEmailVerified == true
        }
    }
    
    // Email & Password Authentication
    suspend fun signInWithEmailPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            Log.d(TAG, "Attempting to sign in with email: $email")
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Log.d(TAG, "Sign in successful for email: $email")
            Result.success(authResult.user!!)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithEmailPassword failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun createUserWithEmailPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            Log.d(TAG, "Attempting to create user with email: $email")
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Log.d(TAG, "User creation successful for email: $email")
            Result.success(authResult.user!!)
        } catch (e: Exception) {
            Log.e(TAG, "createUserWithEmailPassword failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun sendEmailVerification(): Result<Boolean> {
        return try {
            auth.currentUser?.let {
                it.sendEmailVerification().await()
                Result.success(true)
            } ?: Result.failure(Exception("No user logged in"))
        } catch (e: Exception) {
            Log.e(TAG, "sendEmailVerification failed", e)
            Result.failure(e)
        }
    }
    
    // Google Sign-In
    fun getGoogleSignInClient(context: Context, clientId: String): GoogleSignInClient {
        Log.d(TAG, "Creating GoogleSignInClient with clientId: $clientId")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }
    
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            Log.d(TAG, "Attempting to sign in with Google")
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            Log.d(TAG, "Created Google credential")
            val authResult = auth.signInWithCredential(credential).await()
            Log.d(TAG, "Google sign in successful for user: ${authResult.user?.email}")
            Result.success(authResult.user!!)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithGoogle failed", e)
            Result.failure(e)
        }
    }
    
    // Phone Authentication
    fun sendPhoneVerificationCode(
        context: Context,
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        // For phone verification without activity (not recommended but works for testing)
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build()
        )
    }
    
    suspend fun signInWithPhoneCredential(credential: PhoneAuthCredential): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            Result.success(authResult.user!!)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithPhoneCredential failed", e)
            Result.failure(e)
        }
    }
    
    // General auth methods
    fun signOut() {
        auth.signOut()
    }
    
    fun getIntent(): Intent? {
        return auth.pendingAuthResult?.result?.user?.let {
            Intent().apply {
                putExtra("user", it)
            }
        }
    }
    
    companion object {
        private const val TAG = "FirebaseAuthManager"
        
        // Singleton pattern
        private var instance: FirebaseAuthManager? = null
        
        fun getInstance(): FirebaseAuthManager {
            return instance ?: synchronized(this) {
                instance ?: FirebaseAuthManager().also { instance = it }
            }
        }
    }
} 