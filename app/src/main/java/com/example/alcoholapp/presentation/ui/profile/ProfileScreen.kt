package com.example.alcoholapp.presentation.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alcoholapp.R
import com.example.alcoholapp.domain.model.ProfileActionType
import com.example.alcoholapp.domain.model.ProfileSection
import androidx.lifecycle.ViewModelProvider

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToSection: (ProfileActionType) -> Unit = {},
    onSignOut: () -> Unit = {},
    viewModelFactory: ViewModelProvider.Factory? = null
) {
    val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
    val context = LocalContext.current
    
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val profileSections = listOf(
        ProfileSection("My Profile", R.drawable.person, ProfileActionType.PROFILE_INFO),
        ProfileSection("My Addresses", R.drawable.location_icon, ProfileActionType.ADDRESSES),
        ProfileSection("Payment Methods", R.drawable.card, ProfileActionType.PAYMENT_METHODS),
        ProfileSection("Order History", R.drawable.history, ProfileActionType.ORDER_HISTORY),
        ProfileSection("Preferences", R.drawable.settings, ProfileActionType.PREFERENCES),
        ProfileSection("Help & Support", R.drawable.help, ProfileActionType.HELP_SUPPORT),
        ProfileSection("Sign Out", R.drawable.signout, ProfileActionType.SIGN_OUT)
    )
    
    // Show toast for errors
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header section with user details
            HeaderSection(
                userName = userName ?: "Guest User",
                userEmail = userEmail ?: "Sign in to continue",
                onEditClick = { 
                    onNavigateToSection(ProfileActionType.PROFILE_INFO)
                }
            )
            
            // Profile sections
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(profileSections) { section ->
                    ProfileSectionItem(section = section) {
                        when (section.actionType) {
                            ProfileActionType.SIGN_OUT -> {
                                viewModel.signOut()
                                onSignOut()
                            }
                            else -> onNavigateToSection(section.actionType)
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
        
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )
        }
    }
}

@Composable
fun HeaderSection(
    userName: String,
    userEmail: String,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // User name
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )
        
        // User email
        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Edit profile button
        Button(
            onClick = onEditClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Edit Profile",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ProfileSectionItem(
    section: ProfileSection,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            painter = painterResource(id = section.icon),
            contentDescription = section.title,
            modifier = Modifier.size(24.dp),
            tint = if (section.actionType == ProfileActionType.SIGN_OUT) 
                Color.Red else MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Title
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleMedium,
            color = if (section.actionType == ProfileActionType.SIGN_OUT) 
                Color.Red else MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Arrow icon
        if (section.actionType != ProfileActionType.SIGN_OUT) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SignOutCard(onSignOut: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onSignOut() }
    ) {
        // ... existing code ...
    }
} 