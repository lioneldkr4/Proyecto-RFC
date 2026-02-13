package com.example.rfcmultiplataforma

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(
        size = DpSize(700.dp, 600.dp),
        position = WindowPosition(450.dp, 100.dp)
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "rfcmultiplataforma",
        state = state,
        alwaysOnTop = true
    ) {
        App()
    }
}