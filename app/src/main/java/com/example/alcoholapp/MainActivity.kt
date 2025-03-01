package com.example.alcoholapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alcoholapp.data.ApiService
import com.example.alcoholapp.data.repository.HomeRepository
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
                val apiService = ApiService() // baseUrl = "http://10.0.2.2:8080"
                val repository = HomeRepository(apiService)
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(repository)
                )
                MainScreen(homeViewModel)

            }
        }
    }
}

@Composable
fun MainScreen(homeViewModel: HomeViewModel) {
    var selectedItem by remember { mutableStateOf(0) }
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
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> HomeScreen(viewModel = homeViewModel)
            1 -> SearchScreen(modifier = Modifier.padding(innerPadding))
            2 -> OrderScreen(modifier = Modifier.padding(innerPadding))
            3 -> ProfileScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

// This simple HomeScreen is replaced by the actual HomeScreen from presentation layer
// @Composable
// fun HomeScreen(modifier: Modifier = Modifier) {
//     Text(text = "Home Screen", modifier = modifier)
// }

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
    // Preview can't use the actual ViewModel, so we'll just show a placeholder
    AlcoholAppTheme {
        Text(text = "Main Screen Preview")
    }
}