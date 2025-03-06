package com.example.wake_up_radio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wake_up_radio.ui.theme.Wake_up_radioTheme


import androidx.compose.runtime.Composable



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

class ModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Model_logic(context) as T
    }
}

@Composable
fun NavigationGraph() {
    // Create the NavController to manage navigation
    val navController = rememberNavController()

    // Set up the navigation host
    NavHost(navController = navController, startDestination = "home") {
        // Define Composables for each route
        composable("home") { RadioPlayerScreen(navController) }
        composable("first_screen") { First_Screen(navController) }
    }
}

@Composable
fun RadioPlayerScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val myViewModel: Model_logic = viewModel(factory = ModelFactory(context)) // Create ViewModel instance

    val isPlaying by myViewModel::isPlaying // Observes state

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { myViewModel.pause_play() }) // Call function using instance
        {
            Text(if (isPlaying) "Stop" else "Play") // Use instance property
        }
        Button(onClick = { navController.navigate("first_screen") }) {
            Text("Go to Second Screen")
        }
    }
}

@Composable
fun First_Screen(navController: NavController) {
    // Simple UI for second screen
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the second screen!")
        Button(onClick = { navController.navigate("home") }) {
            Text("Go to Second Screen")
        }
    }
}