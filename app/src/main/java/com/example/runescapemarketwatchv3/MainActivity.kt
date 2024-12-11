package com.example.runescapemarketwatchv3

//import android.R
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.runescapemarketwatchv3.ViewModels.MainViewModel
import com.example.runescapemarketwatchv3.ViewModels.MainViewModelFactory
import com.example.runescapemarketwatchv3.Repositories.ItemRepository
import com.example.runescapemarketwatchv3.database.AppDatabase
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.example.runescapemarketwatchv3.ViewModels.SortOrder
import com.example.runescapemarketwatchv3.database.initDatabase
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.viewinterop.AndroidView
import com.example.runescapemarketwatchv3.Repositories.MarketNewsRepository
import com.example.runescapemarketwatchv3.ViewModels.MarketNewsViewModel
import com.example.runescapemarketwatchv3.ViewModels.MarketNewsViewModelFactory
import com.example.runescapemarketwatchv3.ui.theme.RuneScapeFontFamily
import com.example.runescapemarketwatchv3.ui.theme.RuneScapeMarketWatchv3Theme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.Image
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import android.graphics.RenderEffect
import androidx.compose.foundation.gestures.detectTapGestures

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.graphics.graphicsLayer


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manual splash for older APIs
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            Thread.sleep(1000) // Simulate splash duration
        }
        setContent {
            RuneScapeMarketWatchv3Theme {
                AppContent()
            }
        }
    }

    @Composable
    fun AppContent() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "welcome_screen") {
            composable("welcome_screen") {
                WelcomeScreen(navController = navController)
            }
            composable("summary_screen") {
                RuneScapeMarketWatchApp(navController = navController)
            }
            composable("market_news_screen") {
                val viewModel: MarketNewsViewModel = viewModel(factory = MarketNewsViewModelFactory(MarketNewsRepository()))
                MarketNewsScreen(viewModel = viewModel, navController = navController)
            }
            composable("detail_screen/{itemId}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")?.toInt() ?: 21787
                ItemDetailScreen(navController, itemId)
            }
            composable("market_news_detail/{newsId}") { backStackEntry ->
                val newsId = backStackEntry.arguments?.getString("newsId")?.toInt() ?: 0
                MarketNewsDetailScreen(navController = navController, newsId = newsId)
            }
        }
    }
}

    // Initialize the database and perform setup
    @Composable
    fun DatabaseInitializer(viewModel: MainViewModel) {
        val context = LocalContext.current  // Get context using LocalContext

        // Ensure that initDatabase() is called only once
        LaunchedEffect(Unit) {
            initDatabase(context)  // Initialize the database
        }
    }

    @Composable
    fun WelcomeScreen(navController: NavController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { navController.navigate("summary_screen") } // Navigate on tap
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.medieval_texture), // Replace with your drawable
                contentDescription = null, // Decorative background, no content description
                modifier = Modifier
                    .fillMaxSize()
                    .blur(16.dp), // Apply blur effect,
                contentScale = ContentScale.Crop // Ensures the image scales to fill the screen
            )

            // Foreground Content
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Fade-in animation for the title/logo
                var visible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    visible = true
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(initialAlpha = 0.3f) + expandVertically(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "Runescape\n\nMarketWatch",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            shadow = Shadow(
                                color = Color.Black, // Border color
                                offset = Offset(4f, 3f), // Shadow offset
                                blurRadius = 2f // Blur radius for softer edges
                            )
                        ),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize * 2.5f,
                        color = Color.Yellow,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tagline or subtitle
                Text(
                    text = "\nTrack the Economy in Real Time",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(
                            color = Color.Black, // Border color
                            offset = Offset(4f, 3f), // Shadow offset
                            blurRadius = 2f // Blur radius for softer edges
                        )
                    ),
                    color = Color.Yellow

                )

                Spacer(modifier = Modifier.height(32.dp))

                // Instruction to begin
                Text(
                    text = "\n\n\n\n\n\nTap to Begin",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(
                            color = Color.Black, // Border color
                            offset = Offset(4f, 3f), // Shadow offset
                            blurRadius = 2f // Blur radius for softer edges
                        )
                    ),
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.25f,
                )
            }
        }
    }





    @Composable
    fun RuneScapeMarketWatchApp(navController: NavController) {
        val repository = ItemRepository(AppDatabase.getDatabase(LocalContext.current).itemDao())
        val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(repository))
        val allItems by viewModel.allItems.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val isFetchingMore by viewModel.isFetchingMore.collectAsState()

        val listState = rememberLazyListState()

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.medieval_texture), // Replace with your drawable
                contentDescription = null, // Decorative background, no content description
                modifier = Modifier
                    .fillMaxSize()
                    .blur(16.dp), // Apply blur effect,
                contentScale = ContentScale.Crop // Ensures the image scales to fill the screen
            )
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 64.dp) // Reserve space for the buttons
            ) {
                items(allItems) { item ->
                    SummaryCard(item = item, onClick = {
                        navController.navigate("detail_screen/${item.id}")
                    })
                }
                item {
                    if (isFetchingMore) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }

            // Bottom buttons for Sort and Market News
            BottomButtons(navController, viewModel)
        }

        // Start seeding data in the background
        LaunchedEffect(Unit) {
            viewModel.startSeeding()
        }

        // Fetch the first batch of items when the app starts
        LaunchedEffect(Unit) {
            viewModel.fetchInitialItems()
        }

        // Trigger loading more items when scrolled to the end
        LaunchedEffect(Unit) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisibleIndex ->
                    if (lastVisibleIndex != null && lastVisibleIndex >= allItems.size - 1 && !isFetchingMore) {
                        viewModel.fetchRemainingItems()
                    }
                }
        }
    }

    @Composable
    fun BottomButtons(navController: NavController, viewModel: MainViewModel) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sort Button
                SortButton(viewModel)

                Spacer(modifier = Modifier.width(8.dp)) // Space between buttons

                // Market News Button
                Button(
                    onClick = { navController.navigate("market_news_screen") },
                    modifier = Modifier
                        .weight(0.8f)
                        .height(56.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    try {
                                        // Apply pressed effect
                                        awaitRelease()
                                    } finally {
                                        // Reset after release
                                    }
                                }
                            )
                        },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "View Market News",
                        fontFamily = RuneScapeFontFamily,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 2f),
                        color = Color.White
                    )
                }
            }
        }
    }



    @Composable
    fun SortButton(viewModel: MainViewModel) {
        var expanded by remember { mutableStateOf(false) }
        val sortOptions = listOf(
            "Price (Low to High)" to SortOrder.PriceLowToHigh,
            "Price (High to Low)" to SortOrder.PriceHighToLow,
            "Name" to SortOrder.Name,
            "Trend" to SortOrder.Trend
        )

        Box(
            modifier = Modifier
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.size(60.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings, // Typical settings icon
                    contentDescription = "Settings Options",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            // DropdownMenu for sorting options
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sortOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.first, color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            viewModel.updateSortOrder(option.second) // Update sort order in ViewModel
                            expanded = false // Close dropdown after selection
                        }
                    )
                }
            }
        }
    }





@Composable
fun SummaryCard(item: Item, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)) // Rounded corners for the glass effect
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        try {
                            // Apply pressed effect
                            awaitRelease()
                        } finally {
                            // Reset after release
                        }
                    }
                )
            }
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f) // Semi-transparent card background
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White.copy(alpha = 0.1f), // Adjust alpha for frosted effect
                    shape = RoundedCornerShape(16.dp)
                )
//                .blur(16.dp) // Apply blur for glassy look
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Item Icon and Details
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(item.icon),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                shadow = Shadow(
                                    color = Color.Black, // Border color
                                    offset = Offset(3f, 2f), // Shadow offset
                                    blurRadius = 2f // Blur radius for softer edges
                                )
                            ),
                            color = Color.Yellow
                        )
                        Text(
                            text = "Price: ${item.current.price}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black.copy(alpha = 0.8f) // Slightly transparent text
                        )
                        Text(
                            text = "30 Day Trend: ${item.day30.trend} ${item.day30.change}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black.copy(alpha = 0.8f) // Slightly transparent text
                        )
                    }
                }

                // Trend Arrow aligned to the right
                TrendArrow(trend = item.day30.trend)
            }
        }
    }
}




@Composable
fun TrendArrow(trend: String) {
    val icon = when {
        trend.contains("positive", ignoreCase = true) -> Icons.Filled.ArrowUpward
        trend.contains("negative", ignoreCase = true) -> Icons.Filled.ArrowDownward
        else -> Icons.Filled.Remove
    }

    val color = when {
        trend.contains("positive", ignoreCase = true) -> Color.Green
        trend.contains("negative", ignoreCase = true) -> Color.Red
        else -> Color.Yellow
    }

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(28.dp) // Size of the arrow
    )
}




@Composable
fun ItemDetailScreen(navController: NavController, itemId: Int) {
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(ItemRepository(AppDatabase.getDatabase(LocalContext.current).itemDao())))
    val itemDetails by viewModel.itemDetails.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.fetchItemDetails(itemId)
    }

    if (itemDetails != null) {
        val item = itemDetails!!

        // State to track selected chart tab
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Price Trend", "Volume Trend", "Market Comparison")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = Color.Unspecified
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inline implementation of DetailCard
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = rememberAsyncImagePainter(item.icon), contentDescription = null, modifier = Modifier.size(200.dp))
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = item.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "\n${item.description}\n", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Price: ${item.current.price}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "30 Day Trend: ${item.day30.trend} ${item.day30.change}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "90 Day Trend: ${item.day90.trend} ${item.day90.change}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "180 Day Trend: ${item.day180.trend} ${item.day180.change}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Is members-only: ${if (item.members) "Yes" else "No"}", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tabs for chart navigation
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inline implementation of charts
            when (selectedTabIndex) {
                0 -> Chart("Price Trend", listOf(100f, 105f, 102f, 110f, 108f, 115f))
                1 -> Chart("Volume Trend", listOf(200f, 220f, 250f, 240f, 230f, 260f))
                2 -> Chart("Market Comparison", listOf(90f, 95f, 85f, 100f, 105f, 110f))
            }
        }
    } else {
        Text("Loading...")
    }
}


@Composable
fun VolumeTrendChart(dataPoints: List<Float>) {
    Chart(title = "Volume Trend", dataPoints = dataPoints)
}

@Composable
fun MarketComparisonChart(dataPoints: List<Float>) {
    Chart(title = "Market Comparison", dataPoints = dataPoints)
}

@Composable
fun Chart(title: String, dataPoints: List<Float>) {
    val context = LocalContext.current

    // Convert data points to Entries for the chart
    val entries = dataPoints.mapIndexed { index, value -> Entry(index.toFloat(), value) }

    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                description.text = title
                setTouchEnabled(true)
                setPinchZoom(true)

                // Configure X Axis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)

                // Configure Y Axis
                axisRight.isEnabled = false
                axisLeft.textColor = android.graphics.Color.BLACK

                val dataSet = LineDataSet(entries, title).apply {
                    color = android.graphics.Color.BLUE
                    valueTextColor = android.graphics.Color.BLACK
                    setDrawCircles(false)
                }
                data = LineData(dataSet)
                invalidate() // Refresh the chart
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MarketNewsScreen(viewModel: MarketNewsViewModel, navController: NavController) {
        val marketNews by viewModel.marketNews.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Market News") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "Back",
                                tint = Color.Unspecified
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(marketNews) { news ->
                        MarketNewsCard(news) {
                            // Navigate to MarketNewsDetailScreen and pass the news ID
                            navController.navigate("market_news_detail/${news.id}")
                        }
                    }
                }
            }
        }
    }



    @Composable
    fun MarketNewsCard(news: MarketNews, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust padding for consistency
                .clickable { onClick() }, // Trigger the onClick when the card is clicked
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Date: ${news.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Type: ${news.type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3, // Limit description to 3 lines
                    overflow = TextOverflow.Ellipsis // Add ellipsis for truncated text
                )
            }
        }
    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MarketNewsDetailScreen(navController: NavController, newsId: Int) {
        val viewModel: MarketNewsViewModel = viewModel(
            factory = MarketNewsViewModelFactory(MarketNewsRepository())
        )

        val newsItem = viewModel.getNewsById(newsId)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(newsItem?.title ?: "Market News") }, // Fallback if newsItem is null
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "Back",
                                tint = Color.Unspecified
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (newsItem != null) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = newsItem.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Date: ${newsItem.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Type: ${newsItem.type}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = newsItem.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                // Fallback UI for invalid or missing newsItem
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "News item not found.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
