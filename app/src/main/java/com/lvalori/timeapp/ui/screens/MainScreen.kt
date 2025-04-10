package com.lvalori.timeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lvalori.timeapp.domain.model.TimeModel
import com.lvalori.timeapp.ui.components.TimeDisplay
import com.lvalori.timeapp.ui.components.Stopwatch
import com.lvalori.timeapp.ui.components.ActivityTracker
import com.lvalori.timeapp.util.UserInfo
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var timeModel by remember { 
        mutableStateOf(
            TimeModel(
                dateTime = LocalDateTime.now(ZoneOffset.UTC),
                username = UserInfo.getCurrentUsername(context)
            )
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            timeModel = timeModel.copy(
                dateTime = LocalDateTime.now(ZoneOffset.UTC)
            )
            delay(1000)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            TimeDisplay(timeModel = timeModel)
            Stopwatch()
            ActivityTracker()
        }
    }
}