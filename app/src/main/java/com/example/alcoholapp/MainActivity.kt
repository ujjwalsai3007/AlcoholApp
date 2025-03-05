package com.example.alcoholapp

import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alcoholapp.data.ApiService
import com.example.alcoholapp.data.repository.HomeRepository
import com.example.alcoholapp.domain.model.Category
import com.example.alcoholapp.presentation.ui.category.CategoryDetailScreen
import com.example.alcoholapp.presentation.ui.category.CategoryViewModel
import com.example.alcoholapp.presentation.ui.category.CategoryViewModelFactory
import com.example.alcoholapp.presentation.ui.home.HomeScreen
import com.example.alcoholapp.presentation.ui.home.HomeViewModel
import com.example.alcoholapp.presentation.ui.home.HomeViewModelFactory
import com.example.alcoholapp.ui.theme.AlcoholAppTheme

class MainActivity : ComponentActivity() {
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
                val categoryViewModel: CategoryViewModel = viewModel(
                    factory = CategoryViewModelFactory(apiService)
                )
                
                MainScreen(
                    homeViewModel = homeViewModel,
                    categoryViewModel = categoryViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    categoryViewModel: CategoryViewModel
) {
    val navController = rememberNavController()
    val items = listOf("Home", "Search", "Order", "Profile")

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
                        label = { Text(item) },
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
            composable("category/{categoryId}") { backStackEntry ->
                val category = homeViewModel.selectedCategory.collectAsState().value
                if (category != null) {
                    CategoryDetailScreen(
                        categoryName = category.name,
                        products = homeViewModel.categoryProducts.collectAsState().value,
                        onAddToCart = { /* Implement cart functionality */ },
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
fun SearchScreen(modifier: Modifier = Modifier) {
    Text(text = "Search Screen", modifier = modifier)
}

@Composable
fun OrderScreen(modifier: Modifier = Modifier) {
    Text(text = "Order Screen", modifier = modifier)
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(text = "Profile Screen", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AlcoholAppTheme {
        Text(text = "Main Screen Preview")
    }
}