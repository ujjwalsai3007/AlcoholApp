package com.example.alcoholapp.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.alcoholapp.R
import com.example.alcoholapp.domain.model.Category
import com.example.alcoholapp.domain.model.Product
import com.example.alcoholapp.domain.model.Brand

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCategoryClick: (Category) -> Unit
) {
    val homeData by viewModel.homeData.observeAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Preload data when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchHomeData()
    }

    // Preload category products when categories are available
    LaunchedEffect(homeData) {
        homeData?.categories?.let { categories ->
            categories.forEach { category ->
                viewModel.preloadCategoryProducts(category)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .wrapContentSize()
                )
            }
            error.isNotEmpty() -> {
                Text(
                    text = error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .wrapContentSize(),
                    color = Color.Red
                )
            }
            homeData != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp)
                ) {
                    TopSection()
                    BannerSection(bannerTitle = "GRAB SOME BUDS")
                    CategoriesSection(
                        categories = homeData?.categories ?: emptyList(),
                        onCategoryClick = { category -> 
                            if (viewModel.isCategoryPreloaded(category)) {
                                onCategoryClick(category)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Top Brands in Spotlight",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(homeData?.brands ?: emptyList()) { brand ->
                            BrandCategory(brand = brand)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Limited Edition",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(homeData?.limitedEditionProducts ?: emptyList()) { product ->
                            LimitedEditionItem(product = product)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun LimitedEditionItem(product: Product) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(240.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.TopEnd),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Limited",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TopSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Location Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Deliver to home-1633 Hampton",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar()
    }
}

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
        )
        if (searchQuery.isEmpty()) {
            Text(
                text = "Search Liquor",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
        }
    }
}

@Composable
fun BannerSection(bannerTitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            AsyncImage(
                model = "http://10.0.2.2:8081/static/images/alcoholbanner.png",
                contentDescription = "Promotional Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = bannerTitle,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(26.dp))
}

@Composable
fun CategoriesSection(categories: List<Category>, onCategoryClick: (Category) -> Unit) {
    Text(
        text = "Categories",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category, onCategoryClick = onCategoryClick)
        }
    }
}

@Composable
fun CategoryItem(category: Category, onCategoryClick: (Category) -> Unit = {}) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .clickable { 
                onCategoryClick(category)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = category.imageUrl,
            contentDescription = category.name,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun BrandCategory(brand: Brand){
    Column(
        modifier = Modifier.width(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = brand.imageUrl,
            contentDescription = brand.name,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = brand.name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ProductCard(product: Product) {
    var quantity by remember { mutableStateOf(0) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                if (!product.inStock) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Out of Stock",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Product Name
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // Product Description
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Rating and Review Count
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "★ ${String.format("%.1f", product.rating)}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " (${product.reviewCount})",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Price and Add to Cart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (product.inStock) {
                    if (quantity == 0) {
                        Button(
                            onClick = { quantity++ },
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text("ADD")
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (quantity > 0) quantity-- },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Text("-", color = Color.White)
                            }
                            Text(
                                text = quantity.toString(),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = Color.White
                            )
                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Text("+", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}



