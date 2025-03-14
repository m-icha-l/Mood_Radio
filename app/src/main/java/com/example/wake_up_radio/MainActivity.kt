package com.example.wake_up_radio

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api

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

class ModelFactory(private val context: Context) : ViewModelProvider.Factory {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioPlayerScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
     // Tworzenie ViewModel
    val myViewModel: Model_logic = viewModel(factory = ModelFactory(context))
    val isPlaying by myViewModel::isPlaying // Obserwacja stanu

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val radioNames = context.resources.getStringArray(R.array.radio_names)
        val radioLinks = context.resources.getStringArray(R.array.radio_links)
        var selectedOption by remember { mutableStateOf(radioNames[0]) }

        val names_new = myViewModel.get_radio_names()
        val links_new = myViewModel.get_radio_links()
        var selectedOption_new by remember { mutableStateOf(names_new.firstOrNull() ?: "No stations available") }

        var expandedFavorites by remember { mutableStateOf(false) }
        var expandedAll by remember { mutableStateOf(false) }

        // ExposedDropdownMenuBox
        ExposedDropdownMenuBox(
            expanded = expandedFavorites,
            onExpandedChange = { expandedFavorites = it }
        ) {
            TextField(
                value = selectedOption_new,
                onValueChange = {},
                readOnly = true, // Zapobiega ręcznej edycji
                modifier = Modifier
                    .menuAnchor() // Wymagane dla Material3
                    .clickable { expandedFavorites = true }, // Kliknięcie otwiera menu
                label = { Text("Wybierz ulubione radio") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand",
                        modifier = Modifier.clickable { expandedFavorites = true } // Kliknięcie również rozwija
                    )
                }
            )
            // Menu Dropdown
            ExposedDropdownMenu(
                expanded = expandedFavorites,
                onDismissRequest = { expandedFavorites = false } // Zamknięcie menu po kliknięciu poza
            ) {
                names_new.forEachIndexed { index, radio ->
                    DropdownMenuItem(
                        text = { Text(radio) },
                        onClick = {
                            selectedOption_new = radio
                            myViewModel.change_radio(links_new[index]) // Pobierz link
                            expandedFavorites = false // Zamknij dropdown
                        }
                    )
                }
            }
        }



        ExposedDropdownMenuBox(
            expanded = expandedAll,
            onExpandedChange = { expandedAll = it }
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true, // Zapobiega ręcznej edycji
                modifier = Modifier
                    .menuAnchor() // Wymagane dla Material3
                    .clickable { expandedAll = true }, // Kliknięcie otwiera menu
                label = { Text("Wybierz radio") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand",
                        modifier = Modifier.clickable { expandedAll = true } // Kliknięcie również rozwija
                    )
                }
            )

            // Menu Dropdown
            ExposedDropdownMenu(
                expanded = expandedAll,
                onDismissRequest = { expandedAll = false } // Zamknięcie menu po kliknięciu poza
            ) {
                radioNames.forEach { radio ->
                    DropdownMenuItem(
                        text = { Text(radio) },
                        onClick = {
                            selectedOption = radio // Ustawienie wybranego radia
                            val index = radioNames.indexOf(radio)
                            myViewModel.change_radio(radioLinks[index])
                            expandedAll = false // Zamknięcie dropdowna po wyborze
                        }
                    )
                }
            }
        }

        // Przycisk Play / Stop
        Button(onClick = { myViewModel.pause_play() }) {
            Text(if (isPlaying) "Stop" else "Play")
        }

        // Przycisk nawigacyjny
        Button(onClick = { navController.navigate("first_screen") }) {
            Text("Idź do drugiego ekranu")
        }
    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun First_Screen(navController: NavController) {
    val context = LocalContext.current
    val myViewModel: Model_logic = viewModel(factory = ModelFactory(context))
    val isPlaying by myViewModel::isPlaying


    val names_new = myViewModel.get_radio_names()
    val links_new = myViewModel.get_radio_links()
    var selectedOption_new by remember { mutableStateOf(names_new.firstOrNull() ?: "No stations available") }

    var expandedFavorites by remember { mutableStateOf(false) }
    var del_radio by remember { mutableStateOf("") }

    // Simple UI for second screen
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var user_link by remember { mutableStateOf("") }
        var user_radio_name by remember { mutableStateOf("") }
        Text("Add your favorite Radio!")
        TextField(
            value = user_link,
            onValueChange = { user_link = it },
            label = { Text("Enter Radio URL") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = user_radio_name,
            onValueChange = { user_radio_name = it },
            label = { Text("Enter Radio NAME") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if(isPlaying)
                {
                    myViewModel.pause_play()
                }
                myViewModel.add_Radio_Station(user_link,user_radio_name)
        }) {
            Text("Add your Radio")
        }

        ExposedDropdownMenuBox(
            expanded = expandedFavorites,
            onExpandedChange = { expandedFavorites = it }
        ) {
            TextField(
                value = selectedOption_new,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .clickable { expandedFavorites = true },
                label = { Text("Wybierz ulubione radio") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand",
                        modifier = Modifier.clickable { expandedFavorites = true }
                    )
                }
            )
            // Menu Dropdown
            ExposedDropdownMenu(
                expanded = expandedFavorites,
                onDismissRequest = { expandedFavorites = false }
            ) {
                names_new.forEachIndexed { index, radio ->
                    DropdownMenuItem(
                        text = { Text(radio) },
                        onClick = {
                            selectedOption_new = radio
                            del_radio = radio
                            expandedFavorites = false
                        }
                    )
                }
            }
        }
        Button(onClick =
        {
            if(isPlaying)
            {
                myViewModel.pause_play()
            }
            if (del_radio.isNotEmpty())
            {
                val index = names_new.indexOf(del_radio)

                if (index != -1) {
                    myViewModel.remove_Radio_Station(links_new[index], del_radio)
                }
            }

        })
        {
            Text("Delete selected radio")
        }
        Button(onClick = { navController.navigate("home") }) {
            Text("Go to Second Screen")
        }
    }
}