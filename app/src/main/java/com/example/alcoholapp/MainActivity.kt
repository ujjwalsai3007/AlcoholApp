package com.example.alcoholapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alcoholapp.data.ApiService
import com.example.alcoholapp.data.CartManager
import com.example.alcoholapp.data.FirebaseAuthManager
import com.example.alcoholapp.data.repository.HomeRepository
import com.example.alcoholapp.domain.model.Address
import com.example.alcoholapp.domain.model.Category
import com.example.alcoholapp.presentation.ui.auth.LoginScreen
import com.example.alcoholapp.presentation.ui.category.CategoryDetailScreen
import com.example.alcoholapp.presentation.ui.category.CategoryViewModel
import com.example.alcoholapp.presentation.ui.category.CategoryViewModelFactory
import com.example.alcoholapp.presentation.ui.home.HomeScreen
import com.example.alcoholapp.presentation.ui.home.HomeViewModel
import com.example.alcoholapp.presentation.ui.home.HomeViewModelFactory
import com.example.alcoholapp.presentation.ui.order.OrderScreen
import com.example.alcoholapp.presentation.ui.profile.AddressEditScreen
import com.example.alcoholapp.presentation.ui.profile.AddressesScreen
import com.example.alcoholapp.presentation.ui.profile.OrderHistoryScreen
import com.example.alcoholapp.domain.model.ProfileActionType
import com.example.alcoholapp.presentation.ui.profile.ProfileInfoScreen
import com.example.alcoholapp.presentation.ui.profile.ProfileViewModel
import com.example.alcoholapp.presentation.ui.profile.ProfileViewModelFactory
import com.example.alcoholapp.presentation.ui.search.SearchScreen
import com.example.alcoholapp.ui.theme.AlcoholAppTheme
import kotlinx.coroutines.flow.first
import com.example.alcoholapp.presentation.ui.profile.ProfileScreen as ProfileScreenMain

class MainActivity : ComponentActivity() {
    
    private var authManager: FirebaseAuthManager? = null
    
    // Track whether we're showing login screen or main screen
    private val _showLoginScreen = mutableStateOf(true)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize auth manager
        authManager = FirebaseAuthManager.getInstance()
        
        // Initial auth check
        val currentUser = authManager?.getCurrentUser()
        _showLoginScreen.value = currentUser == null
        
        setContent {
            AlcoholAppTheme {
                // Get the application context
                val context = LocalContext.current
                
                // Initialize view models
                val repository = HomeRepository(ApiService())
                val homeViewModel = viewModel<HomeViewModel>(
                    factory = HomeViewModelFactory(repository)
                )
                
                // Observe the login screen state
                val showLoginScreen by remember { _showLoginScreen }
                
                if (!showLoginScreen) {
                    MainScreen(
                        homeViewModel = homeViewModel,
                        onSignOut = {
                            Log.d("MainActivity", "Sign out requested from MainScreen")
                            
                            // 1. Sign out from Firebase
                            authManager?.signOut()
                            
                            // 2. Clear any app-specific state (carts, etc)
                            CartManager.getInstance().clearCart()
                            
                            // 3. Show feedback to user
                            Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                            
                            // 4. IMPORTANT: Force show login screen by setting state directly
                            _showLoginScreen.value = true
                            
                            // 5. Force UI refresh by calling setContent again
                            setContent {
                                AlcoholAppTheme {
                                    LoginScreen(
                                        onLoginSuccess = { 
                                            Log.d("MainActivity", "Login successful")
                                            _showLoginScreen.value = false
                                        }
                                    )
                                }
                            }
                        }
                    )
                } else {
                    LoginScreen(
                        onLoginSuccess = { 
                            Log.d("MainActivity", "Login successful")
                            _showLoginScreen.value = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    val items = listOf("Home", "Search", "Order", "Profile")
    val cartManager = CartManager.getInstance()
    val cartItemCount by cartManager.totalItems.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Filled.Home
                                    1 -> Icons.Filled.Search
                                    2 -> Icons.Outlined.ShoppingCart
                                    else -> Icons.Filled.Person
                                },
                                contentDescription = item
                            )
                        },
                        label = { 
                            if (index == 2 && cartItemCount > 0) {
                                Text("Order ($cartItemCount)")
                            } else {
                                Text(item)
                            }
                        },
                        selected = when (index) {
                            0 -> navController.currentDestination?.route?.startsWith("home") == true
                            1 -> navController.currentDestination?.route == "search"
                            2 -> navController.currentDestination?.route == "order"
                            3 -> navController.currentDestination?.route == "profile"
                            else -> false
                        },
                        onClick = {
                            when (index) {
                                0 -> navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                                1 -> navController.navigate("search") {
                                    popUpTo("home")
                                }
                                2 -> navController.navigate("order") {
                                    popUpTo("home")
                                }
                                3 -> navController.navigate("profile") {
                                    popUpTo("home")
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = homeViewModel,
                    onCategoryClick = { category ->
                        homeViewModel.selectCategory(category)
                        navController.navigate("category/${category.id}")
                    }
                )
            }
            composable("category/{categoryId}") { _ ->
                val category = homeViewModel.selectedCategory.collectAsState().value
                if (category != null) {
                    CategoryDetailScreen(
                        categoryName = category.name,
                        products = homeViewModel.categoryProducts.collectAsState().value,
                        onAddToCart = { product -> 
                            cartManager.addToCart(product)
                        },
                        onBackClick = {
                            homeViewModel.clearSelectedCategory()
                            navController.popBackStack()
                        }
                    )
                }
            }
            composable("search") {
                SearchScreen()
            }
            composable("order") {
                OrderScreen()
            }
            composable("profile") {
                ProfileScreen(onSignOut = onSignOut)
            }
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, onSignOut: () -> Unit) {
    val context = LocalContext.current
    val viewModelFactory = remember { ProfileViewModelFactory(context) }
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "profile_main"
    ) {
        composable("profile_main") {
            ProfileScreenMain(
                onNavigateToSection = { actionType ->
                    when (actionType) {
                        ProfileActionType.PROFILE_INFO -> navController.navigate("profile_info")
                        ProfileActionType.ADDRESSES -> navController.navigate("addresses")
                        ProfileActionType.ORDER_HISTORY -> navController.navigate("order_history")
                        else -> {
                            // For other sections like PAYMENT_METHODS, PREFERENCES, HELP_SUPPORT
                            // These would be implemented in a real app
                            Toast.makeText(
                                context, 
                                "Not implemented: ${actionType.name}", 
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onSignOut = onSignOut,
                viewModelFactory = viewModelFactory
            )
        }
        
        composable("profile_info") {
            val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
            ProfileInfoScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable("addresses") {
            val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
            AddressesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onAddAddress = { navController.navigate("address_edit") },
                onEditAddress = { address ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("address", address)
                    navController.navigate("address_edit")
                }
            )
        }
        
        composable("address_edit") {
            val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
            val address = navController.previousBackStackEntry?.savedStateHandle?.get<Address>("address")
            
            AddressEditScreen(
                viewModel = viewModel,
                address = address,
                onBackClick = { navController.popBackStack() },
                onSaveComplete = { navController.popBackStack() }
            )
        }
        
        composable("order_history") {
            val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
            OrderHistoryScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onViewOrder = { orderId ->
                    // In a real app, this would navigate to order details
                    Toast.makeText(
                        context,
                        "Order details for $orderId not implemented",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AlcoholAppTheme {
        Text(text = "Main Screen Preview")
    }
}