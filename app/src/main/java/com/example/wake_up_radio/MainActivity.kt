package com.example.wake_up_radio

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wake_up_radio.ui.theme.Wake_up_radioTheme
import com.example.wake_up_radio.CustomDropdownMenu
import com.example.wake_up_radio.CustomTextField
import com.example.wake_up_radio.Popup
import kotlinx.coroutines.delay
import androidx.compose.foundation.background

import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Wake_up_radioTheme {
                NavigationGraph()
            }
        }
    }
}

class ModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Model_logic(context) as T
    }
}

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { RadioPlayerScreen(navController) }
        composable("first_screen") { FirstScreen(navController) }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(true) {
        kotlinx.coroutines.delay(2500)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.lading) // your loading.gif
                .decoderFactory(GifDecoder.Factory())
                .build(),
            contentDescription = "Loading Animation",
            modifier = Modifier.size(150.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioPlayerScreen(navController: NavController) {
    val context = LocalContext.current
    val myViewModel: Model_logic = viewModel(factory = ModelFactory(context))
    val isPlaying by myViewModel::isPlaying

    // Get arrays from resources and ViewModel
    val radioNames = context.resources.getStringArray(R.array.radio_names)
    val radioLinks = context.resources.getStringArray(R.array.radio_links)
    var selectedOption by remember { mutableStateOf(radioNames[0]) }
    val namesNew = myViewModel.get_radio_names()
    val linksNew = myViewModel.get_radio_links()
    var selectedOptionNew by remember { mutableStateOf(namesNew.firstOrNull() ?: "No stations available") }
    var expandedFavorites by remember { mutableStateOf(false) }
    var expandedAll by remember { mutableStateOf(false) }

    // Define a custom button height
    val buttonHeight = 56.dp

    val radioError = myViewModel.radioError
    var isDialogOpen by remember { mutableStateOf(false) }
    var massage by remember { mutableStateOf("") }

    if (radioError != null) {
        Log.d("works","popup triggerd")
        isDialogOpen = true
        massage = "Error: No internet or Bad Link"
    }
    Popup(
        isDialogOpen = isDialogOpen,
        onDismiss = {
            isDialogOpen = false
            myViewModel.radioError = null},
        text = massage
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = if (!isSystemInDarkTheme()) R.drawable.background_day_1 else R.drawable.background_1 ),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Radio Player", style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(0xFF1976D2),
                        titleContentColor = Color.White
                    )
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomDropdownMenu(
                        label = "Your Favorites",
                        options = namesNew,
                        selectedOption = selectedOptionNew,
                        onSelect = { index ->
                            selectedOptionNew = namesNew[index]
                            myViewModel.change_radio(linksNew[index])
                        },
                        expanded = expandedFavorites,
                        onExpandChange = { expandedFavorites = it }
                    )

                    CustomDropdownMenu(
                        label = "Default Stations",
                        options = radioNames.toList(),
                        selectedOption = selectedOption,
                        onSelect = { index ->
                            selectedOption = radioNames[index]
                            myViewModel.change_radio(radioLinks[index])
                        },
                        expanded = expandedAll,
                        onExpandChange = { expandedAll = it }
                    )

                    Button(
                        onClick =
                        {
                            myViewModel.pause_play()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPlaying) Color(0xFFD32F2F) else Color(0xFF1976D2),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = if (isPlaying) "Stop" else "Play")
                    }

                    Button(
                        onClick = { navController.navigate("first_screen") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFA726),
                            contentColor = Color(0xFF333333)
                        )
                    ) {
                        Text("Radio Manager")
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(navController: NavController) {
    val context = LocalContext.current
    val myViewModel: Model_logic = viewModel(factory = ModelFactory(context))
    val isPlaying by myViewModel::isPlaying

    val namesNew = myViewModel.get_radio_names()
    val linksNew = myViewModel.get_radio_links()
    var selectedOptionNew by remember { mutableStateOf(namesNew.firstOrNull() ?: "No stations available") }
    var expandedFavorites by remember { mutableStateOf(false) }
    var delRadio by remember { mutableStateOf("") }
    var userLink by remember { mutableStateOf("") }
    var userRadioName by remember { mutableStateOf("") }
    var isDialogOpen by remember { mutableStateOf(false) }
    var massage by remember { mutableStateOf("") }

    val buttonHeight = 56.dp
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = if (!isSystemInDarkTheme()) R.drawable.background_day_2 else R.drawable.background_2),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Radio Manager", style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(0xFFFFA726),
                        titleContentColor = Color(0xFF333333)
                    )
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(
                        16.dp,
                        alignment = Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Add your favorite Radio!", style = MaterialTheme.typography.titleLarge, color = if (isSystemInDarkTheme()) Color.White else Color.Black)

                    Button(
                        onClick = { openWebsite(context) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight)
                    ) {
                        Text("Search for Radios")
                    }

                    CustomTextField("Enter Radio URL", userLink) { userLink = it }
                    CustomTextField("Enter Radio Name", userRadioName) { userRadioName = it }

                    Button(
                        onClick = {
                            if (isPlaying) myViewModel.pause_play()
                            if(validateRadioStreamUrl(userLink, false))
                            {
                                if(validateRadioStreamUrl(userRadioName, true))
                                {
                                    myViewModel.add_Radio_Station(userLink, userRadioName)
                                    isDialogOpen = true
                                    massage = "Radio added"
                                }
                                else
                                {
                                    userRadioName=""
                                    isDialogOpen = true
                                    massage = "Error: Name has a bad syntax"
                                }
                            }
                            else
                            {
                                userLink = ""
                                userRadioName=""
                                isDialogOpen = true
                                massage = "Error: Link or name has a bad syntax"
                            }

                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight)
                    ) {
                        Text("Add Radio")
                    }

                    Text("Remove one of added Radios", style = MaterialTheme.typography.titleLarge, color = if (isSystemInDarkTheme()) Color.White else Color.Black)

                    CustomDropdownMenu(
                        label = "Choose Radio",
                        options = namesNew,
                        selectedOption = selectedOptionNew,
                        onSelect = { index ->
                            selectedOptionNew = namesNew[index]
                            delRadio = namesNew[index]
                        },
                        expanded = expandedFavorites,
                        onExpandChange = { expandedFavorites = it }
                    )

                    Button(
                        onClick = {
                            if (isPlaying) myViewModel.pause_play()
                            val index = namesNew.indexOf(delRadio)
                            if (index != -1) myViewModel.remove_Radio_Station(
                                linksNew[index],
                                delRadio
                            )
                            isDialogOpen = true
                            massage = "Radio deleted"
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight)
                    ) {
                        Text("Delete selected radio")
                    }

                    Button(
                        onClick = { navController.navigate("home") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFA726),
                            contentColor = Color(0xFF333333)
                        )
                    ) {
                        Text("Home")
                    }

                    Popup(
                        isDialogOpen = isDialogOpen,
                        onDismiss = { isDialogOpen = false },
                        text = massage
                    )
                }
            }
        )
    }
}
