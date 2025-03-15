package com.example.wake_up_radio

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wake_up_radio.ui.theme.Wake_up_radioTheme

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
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { RadioPlayerScreen(navController) }
        composable("first_screen") { FirstScreen(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioPlayerScreen(navController: NavController) {
    val context = LocalContext.current
    val myViewModel: Model_logic = viewModel(factory = ModelFactory(context))
    val isPlaying by myViewModel::isPlaying

    val radioNames = context.resources.getStringArray(R.array.radio_names)
    val radioLinks = context.resources.getStringArray(R.array.radio_links)
    var selectedOption by remember { mutableStateOf(radioNames[0]) }

    val namesNew = myViewModel.get_radio_names()
    val linksNew = myViewModel.get_radio_links()
    var selectedOptionNew by remember { mutableStateOf(namesNew.firstOrNull() ?: "No stations available") }

    var expandedFavorites by remember { mutableStateOf(false) }
    var expandedAll by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomDropdownMenu(
            label = "Choose your Radio",
            options = namesNew,
            selectedOption = selectedOptionNew,
            onSelect = { index ->
                selectedOptionNew = namesNew[index]
                myViewModel.change_radio(linksNew[index])
            },
            expanded = expandedFavorites,
            onExpandChange = { expandedFavorites = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomDropdownMenu(
            label = "Choose from default stations",
            options = radioNames.toList(),
            selectedOption = selectedOption,
            onSelect = { index ->
                selectedOption = radioNames[index]
                myViewModel.change_radio(radioLinks[index])
            },
            expanded = expandedAll,
            onExpandChange = { expandedAll = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { myViewModel.pause_play() },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isPlaying) "Stop" else "Play")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("first_screen") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Radio Manager")
        }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add your favorite Radio!", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField("Enter Radio URL", userLink) { userLink = it }
        CustomTextField("Enter Radio Name", userRadioName) { userRadioName = it }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (isPlaying) myViewModel.pause_play()
                myViewModel.add_Radio_Station(userLink, userRadioName)
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add your Radio")
        }

        Spacer(modifier = Modifier.height(22.dp))
        Text("Remove one of added Radios", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isPlaying) myViewModel.pause_play()
                val index = namesNew.indexOf(delRadio)
                if (index != -1) myViewModel.remove_Radio_Station(linksNew[index], delRadio)
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete selected radio")
        }

        Spacer(modifier = Modifier.height(56.dp))

        Button(
            onClick = { navController.navigate("home") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Home Screen")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (Int) -> Unit,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandChange
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .clickable { onExpandChange(true) },
            label = { Text(label) },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, "Expand", Modifier.clickable { onExpandChange(true) })
            }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandChange(false) }) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(index)
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp)
    )
}
