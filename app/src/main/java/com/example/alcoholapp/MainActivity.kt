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
    // Add a composable state that can be updated from other composables
    private val _isLoggedIn = mutableStateOf(false)
    private val isLoggedIn: Boolean
        get() = _isLoggedIn.value
        
    fun onUserSignedOut() {
        _isLoggedIn.value = false
        Log.d("MainActivity", "User signed out, updating login state: ${_isLoggedIn.value}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcoholAppTheme {
                val apiService = ApiService()
                val repository = HomeRepository(apiService)
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(repository)
                )
                
                // Use the mutableState variable declared above
                val isLoggedInState by remember { _isLoggedIn }
                
                // Check initial login state
                LaunchedEffect(Unit) {
                    try {
                        val authManager = FirebaseAuthManager.getInstance()
                        val currentUser = authManager.currentUser.first()
                        _isLoggedIn.value = currentUser != null
                        Log.d("MainActivity", "Initial login state: ${_isLoggedIn.value}")
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error checking login state", e)
                    }
                }
                
                if (isLoggedInState) {
                    MainScreen(homeViewModel = homeViewModel)
                } else {
                    LoginScreen(
                        onLoginSuccess = { 
                            Log.d("MainActivity", "Login successful")
                            _isLoggedIn.value = true 
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
    homeViewModel: HomeViewModel
) {
    val navController = rememberNavController()
    val items = listOf("Home", "Search", "Order", "Profile")
    val cartManager = CartManager.getInstance()
    val cartItemCount by cartManager.totalItems.collectAsState()
    val apiService = remember { ApiService() }

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
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModelFactory = remember { ProfileViewModelFactory(context) }
    val navController = rememberNavController()
    
    // Create a mutable state to track sign-out status
    val hasSignedOut = remember { mutableStateOf(false) }
    
    // Handle sign-out effect
    LaunchedEffect(hasSignedOut.value) {
        if (hasSignedOut.value) {
            // This callback forces the MainActivity to re-check login state
            (context as? MainActivity)?.onUserSignedOut()
        }
    }
    
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
                onSignOut = {
                    // Set the sign-out flag to trigger the LaunchedEffect
                    hasSignedOut.value = true
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                },
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