package com.example.wake_up_radio

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.regex.Pattern
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
                Icon(
                    Icons.Default.ArrowDropDown,
                    "Expand",
                    Modifier.clickable { onExpandChange(true) }
                )
            },
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



@OptIn(ExperimentalMaterial3Api::class)
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

fun openWebsite(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://streamurl.link/"))
    context.startActivity(intent)
}

@Composable
fun Popup(isDialogOpen: Boolean, onDismiss: () -> Unit, text: String) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Mood Radio", style = MaterialTheme.typography.titleLarge) },
            text = { Text(text) },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            },
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.medium
        )
    }
}

fun validateRadioStreamUrl(url: String, name: Boolean): Boolean {
    // Step 1: Check if the URL starts with "http" or "https"
    if(url=="")
    {
        return false
    }
    if(!name)
    {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false
        }

        // Step 2: Validate URL using a regex pattern to ensure it only contains allowed characters.
        val urlPattern = Pattern.compile("^[a-zA-Z0-9-._~:/?#@!$&'()*+,;=]*\$")
        if (!urlPattern.matcher(url).matches()) {
            return false
        }
    }

    // Step 3: Prevent potential injection patterns by looking for common SQL injection keywords
    val disallowedPatterns = listOf("SELECT", "INSERT", "DROP", "DELETE", "--", "/*", "*/", "UNION", "OR", "AND")
    for (pattern in disallowedPatterns) {
        if (url.contains(pattern, ignoreCase = true)) {
            return false
        }
    }
    return true
}
