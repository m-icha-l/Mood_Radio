package com.example.wake_up_radio

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.audio.AudioAttributes
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check if app was launched by the alarm
        val fromAlarm = intent.getBooleanExtra("FROM_ALARM", false)
        setContent {
            RadioPlayerScreen(fromAlarm)
        }

        // Request SCHEDULE_EXACT_ALARM permission for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
    }
}

// Schedule the app to open at a specific time
fun scheduleAppLaunch(context: Context, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, MainActivity::class.java).apply {
        putExtra("FROM_ALARM", true) // Indicate alarm triggered it
    }

    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)

        // If the selected time is in the past, add 1 day
        if (timeInMillis <= System.currentTimeMillis()) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    // Debug log
    Log.d("AlarmDebug", "Alarm scheduled for: ${calendar.time}")

    // Wrap in try-catch to handle SecurityException
    try {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Alarm set for $hour:$minute", Toast.LENGTH_SHORT).show()
    } catch (e: SecurityException) {
        Toast.makeText(context, "Unable to schedule alarm: Permission denied", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun RadioPlayerScreen(fromAlarm: Boolean) {
    val context = LocalContext.current
    var selectedTime by remember { mutableStateOf("Select Time") }
    var alarmActive by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(com.google.android.exoplayer2.C.USAGE_MEDIA)
                    .setContentType(com.google.android.exoplayer2.C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            setMediaItem(
                MediaItem.Builder()
                    .setUri("https://wr2.downtime.fi/kaakko.aac") // Replace with your radio URL
                    .build()
            )
            prepare()
        }
    }

    // Automatically play if launched by the alarm
    var isPlaying by remember { mutableStateOf(fromAlarm) }
    if (fromAlarm && !isPlaying) {
        exoPlayer.play()
        isPlaying = true
    }

    // Dispose ExoPlayer when component is removed
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    // UI: Play/Stop Button
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            if (isPlaying) exoPlayer.pause() else exoPlayer.play()
            isPlaying = !isPlaying
        }) {
            Text(if (isPlaying) "Stop" else "Play")
        }

        // Set Time Button with Time Picker Dialog
        Button(
            onClick = {
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _: TimePicker, hourOfDay: Int, minute: Int ->
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Pick Time")
        }

        // Display the selected time
        Text(text = "Selected Time: $selectedTime", modifier = Modifier.padding(16.dp))

        // RadioButton to toggle alarm active state
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            RadioButton(
                selected = alarmActive,
                onClick = { alarmActive = !alarmActive }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Activate Alarm")

            // Button to schedule the alarm if it's active
            if (alarmActive) {
                Button(
                    onClick = {
                        val (hour, minute) = selectedTime.split(":").let {
                            it[0].toInt() to it[1].toInt()
                        }
                        scheduleAppLaunch(context, hour, minute)
                    },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text("Set Alarm")
                }
            }
        }
    }
}